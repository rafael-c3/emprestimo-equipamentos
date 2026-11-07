package br.edu.prometheus.msequipamento.dto;

import br.edu.prometheus.msequipamento.entity.Equipamento;

import java.util.UUID;

public record EquipamentoResponse(
        UUID id,
        String nome,
        String tipo,
        String patrimonio,
        boolean disponivel
) {
    public static EquipamentoResponse de(Equipamento equipamento) {
        return new EquipamentoResponse(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getTipo(),
                equipamento.getPatrimonio(),
                equipamento.isDisponivel()
        );
    }
}