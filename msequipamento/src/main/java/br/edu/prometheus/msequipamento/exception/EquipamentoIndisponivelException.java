package br.edu.prometheus.msequipamento.exception;

import java.util.UUID;

public class EquipamentoIndisponivelException extends RuntimeException {

    public EquipamentoIndisponivelException(UUID equipamentoId) {
        super("Equipamento indisponível ou reservado por outro empréstimo: " + equipamentoId);
    }
}