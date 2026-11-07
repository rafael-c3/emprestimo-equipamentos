package br.edu.prometheus.msequipamento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tb_equipamentos")
public class Equipamento {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "tipo", nullable = false, length = 80)
    private String tipo;

    @Column(name = "patrimonio", nullable = false, unique = true, length = 80)
    private String patrimonio;

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel;

    /*
     * Guarda o identificador da última reserva.
     * Quando disponivel é false, identifica a reserva ativa.
     * Ao liberar, ele é mantido para reconhecer uma repetição da mesma reserva.
     */
    @Column(name = "reserva_emprestimo_id", columnDefinition = "BINARY(16)")
    private UUID reservaEmprestimoId;

    public Equipamento() {
    }

    public Equipamento(
            UUID id,
            String nome,
            String tipo,
            String patrimonio,
            boolean disponivel
    ) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.patrimonio = patrimonio;
        this.disponivel = disponivel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public UUID getReservaEmprestimoId() {
        return reservaEmprestimoId;
    }

    public void setReservaEmprestimoId(UUID reservaEmprestimoId) {
        this.reservaEmprestimoId = reservaEmprestimoId;
    }
}