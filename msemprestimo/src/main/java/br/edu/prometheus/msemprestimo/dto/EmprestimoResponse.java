package br.edu.prometheus.msemprestimo.dto;

import br.edu.prometheus.msemprestimo.entity.Emprestimo;
import br.edu.prometheus.msemprestimo.entity.StatusEmprestimo;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmprestimoResponse(
        UUID id,
        UUID usuarioId,
        UUID equipamentoId,
        LocalDateTime dataEmprestimo,
        LocalDateTime dataDevolucao,
        StatusEmprestimo status
) {
    public static EmprestimoResponse de(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getUsuarioId(),
                emprestimo.getEquipamentoId(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatus()
        );
    }
}