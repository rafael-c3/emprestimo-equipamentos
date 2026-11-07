package br.edu.prometheus.msemprestimo.exception;

import java.util.UUID;

public class EquipamentoIndisponivelException extends RuntimeException {

    public EquipamentoIndisponivelException(UUID id) {
        super("Equipamento indisponível ou reservado por outro empréstimo: " + id);
    }
}