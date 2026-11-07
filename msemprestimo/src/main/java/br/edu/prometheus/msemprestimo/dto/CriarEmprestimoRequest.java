package br.edu.prometheus.msemprestimo.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarEmprestimoRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        UUID usuarioId,

        @NotNull(message = "O ID do equipamento é obrigatório")
        UUID equipamentoId
) {
}