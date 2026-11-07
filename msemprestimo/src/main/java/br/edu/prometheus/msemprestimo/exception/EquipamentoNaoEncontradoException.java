package br.edu.prometheus.msemprestimo.exception;

import java.util.UUID;

public class EquipamentoNaoEncontradoException extends RuntimeException {

    public EquipamentoNaoEncontradoException(UUID id) {
        super("Equipamento não encontrado: " + id);
    }
}