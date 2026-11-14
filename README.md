# Sistema de Empréstimo de Equipamentos

Projeto acadêmico de microsserviços para gerenciar o empréstimo de notebooks,
projetores e tablets.

## Arquitetura

| Aplicação | Responsabilidade | Porta | Banco |
|---|---|---:|---|
| discovery-server | Registro e descoberta via Eureka | 8761 | Não possui |
| api-gateway | Entrada e roteamento das chamadas | 8080 | Não possui |
| msusuario | Cadastro e consulta de usuários | 8081 | msusuario_db |
| msequipamento | Cadastro, consulta e reserva de equipamentos | 8082 | msequipamento_db |
| msemprestimo | Solicitação, consulta e devolução de empréstimos | 8083 | msemprestimo_db |

Cada microsserviço de domínio mantém seu próprio banco. O empréstimo armazena
os IDs de usuário e equipamento, sem relacionamentos ou chaves estrangeiras
entre bancos.

## Tecnologias

- Java 21
- Spring Boot 4.0.9
- Spring Cloud 2025.1.4
- Spring Data JPA
- MySQL
- Flyway
- Eureka
- Spring Cloud Gateway Server WebFlux
- OpenFeign
- Spring Cloud LoadBalancer

## Pré-requisitos

- JDK 21
- MySQL em execução local
- IntelliJ IDEA ou outro ambiente Java com Maven

Cada microsserviço de domínio tem sua própria configuração de conexão no
`application.yml`. Ajuste usuário e senha de acordo com o MySQL da máquina.

## Como executar

Inicie o MySQL e execute as aplicações nesta ordem:

1. `discovery-server`
2. `msusuario`
3. `msequipamento`
4. `msemprestimo`
5. `api-gateway`

O painel do Eureka fica em:

```text
http://localhost:8761

## Etapa 2 — Condição do equipamento e resiliência

Os equipamentos possuem uma condição, representada pelos valores `BOM` e
`AVARIADO`. A condição pode ser atualizada pelo endpoint:

```http
PATCH /api/v1/equipamentos/{id}/condicao
Content-Type: application/json

{
  "condicao": "AVARIADO"
}

Durante a devolução, o msemprestimo usa OpenFeign para atualizar a condição no
msequipamento. Se essa atualização falhar por indisponibilidade técnica, a
devolução não é concluída e a API retorna 503 Service Unavailable. Se o
equipamento não existir, a API retorna 404 Not Found.

O circuit breaker atualizarCondicaoEquipamento, configurado com Resilience4j,
usa janela de 5 chamadas, exige pelo menos 3 chamadas para avaliar falhas,
abre com taxa de falha de 50%, permanece aberto por 30 segundos e permite
2 chamadas no estado semiaberto.

Cenários verificados
- Devolução com condição BOM.
- Devolução com condição AVARIADO.
- Equipamento inexistente retorna 404.
- msequipamento indisponível retorna 503, sem concluir a devolução.
- Recuperação da devolução após o msequipamento voltar a ficar disponível.
