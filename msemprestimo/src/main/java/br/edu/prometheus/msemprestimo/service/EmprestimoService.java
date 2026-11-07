package br.edu.prometheus.msemprestimo.service;

import br.edu.prometheus.msemprestimo.client.EquipamentoClient;
import br.edu.prometheus.msemprestimo.client.UsuarioClient;
import br.edu.prometheus.msemprestimo.dto.CriarEmprestimoRequest;
import br.edu.prometheus.msemprestimo.dto.EmprestimoResponse;
import br.edu.prometheus.msemprestimo.entity.Emprestimo;
import br.edu.prometheus.msemprestimo.entity.StatusEmprestimo;
import br.edu.prometheus.msemprestimo.exception.EquipamentoIndisponivelException;
import br.edu.prometheus.msemprestimo.exception.EquipamentoNaoEncontradoException;
import br.edu.prometheus.msemprestimo.exception.EmprestimoNaoEncontradoException;
import br.edu.prometheus.msemprestimo.exception.ServicoDependenciaIndisponivelException;
import br.edu.prometheus.msemprestimo.exception.UsuarioNaoEncontradoException;
import br.edu.prometheus.msemprestimo.repository.EmprestimoRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioClient usuarioClient;
    private final EquipamentoClient equipamentoClient;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            UsuarioClient usuarioClient,
            EquipamentoClient equipamentoClient
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioClient = usuarioClient;
        this.equipamentoClient = equipamentoClient;
    }

    public EmprestimoResponse solicitar(CriarEmprestimoRequest request) {
        UUID emprestimoId = UUID.randomUUID();

        validarUsuario(request.usuarioId());
        consultarEquipamento(request.equipamentoId());
        reservarEquipamento(request.equipamentoId(), emprestimoId);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(emprestimoId);
        emprestimo.setUsuarioId(request.usuarioId());
        emprestimo.setEquipamentoId(request.equipamentoId());
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        try {
            return EmprestimoResponse.de(emprestimoRepository.save(emprestimo));
        } catch (RuntimeException exception) {
            // Se não gravar o empréstimo, tenta desfazer a reserva remota.
            tentarLiberarEquipamento(request.equipamentoId(), emprestimoId);
            throw exception;
        }
    }

    public EmprestimoResponse buscarPorId(UUID id) {
        return EmprestimoResponse.de(obterEmprestimo(id));
    }

    public List<EmprestimoResponse> buscarPorUsuario(UUID usuarioId) {
        return emprestimoRepository
                .findByUsuarioIdOrderByDataEmprestimoDesc(usuarioId)
                .stream()
                .map(EmprestimoResponse::de)
                .toList();
    }

    public EmprestimoResponse devolver(UUID id) {
        Emprestimo emprestimo = obterEmprestimo(id);

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            return EmprestimoResponse.de(emprestimo);
        }

        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO) {
            throw new IllegalStateException(
                    "Somente empréstimos ativos podem ser devolvidos"
            );
        }

        liberarEquipamento(
                emprestimo.getEquipamentoId(),
                emprestimo.getId()
        );

        emprestimo.setDataDevolucao(LocalDateTime.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        return EmprestimoResponse.de(emprestimoRepository.save(emprestimo));
    }

    private void validarUsuario(UUID usuarioId) {
        try {
            usuarioClient.buscarPorId(usuarioId);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new UsuarioNaoEncontradoException(usuarioId);
            }
            throw new ServicoDependenciaIndisponivelException("msusuario");
        }
    }

    private void consultarEquipamento(UUID equipamentoId) {
        try {
            equipamentoClient.buscarPorId(equipamentoId);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new EquipamentoNaoEncontradoException(equipamentoId);
            }
            throw new ServicoDependenciaIndisponivelException("msequipamento");
        }
    }

    private void reservarEquipamento(UUID equipamentoId, UUID emprestimoId) {
        try {
            equipamentoClient.reservar(equipamentoId, emprestimoId);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new EquipamentoNaoEncontradoException(equipamentoId);
            }
            if (exception.status() == 409) {
                throw new EquipamentoIndisponivelException(equipamentoId);
            }
            throw new ServicoDependenciaIndisponivelException("msequipamento");
        }
    }

    private void liberarEquipamento(UUID equipamentoId, UUID emprestimoId) {
        try {
            equipamentoClient.liberar(equipamentoId, emprestimoId);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new EquipamentoNaoEncontradoException(equipamentoId);
            }
            if (exception.status() == 409) {
                throw new EquipamentoIndisponivelException(equipamentoId);
            }
            throw new ServicoDependenciaIndisponivelException("msequipamento");
        }
    }

    private void tentarLiberarEquipamento(UUID equipamentoId, UUID emprestimoId) {
        try {
            equipamentoClient.liberar(equipamentoId, emprestimoId);
        } catch (RuntimeException ignored) {
            // O erro original da gravação será retornado.
        }
    }

    private Emprestimo obterEmprestimo(UUID id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new EmprestimoNaoEncontradoException(id));
    }
}