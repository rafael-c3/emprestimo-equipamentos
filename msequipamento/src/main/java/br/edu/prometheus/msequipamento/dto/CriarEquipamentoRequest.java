package br.edu.prometheus.msequipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarEquipamentoRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "O tipo é obrigatório")
        @Size(max = 80, message = "O tipo deve ter no máximo 80 caracteres")
        String tipo,

        @NotBlank(message = "O patrimônio é obrigatório")
        @Size(max = 80, message = "O patrimônio deve ter no máximo 80 caracteres")
        String patrimonio
) {
}