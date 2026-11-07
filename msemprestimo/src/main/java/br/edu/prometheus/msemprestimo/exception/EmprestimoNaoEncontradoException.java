package br.edu.prometheus.msemprestimo.exception;

import java.util.UUID;

public class EmprestimoNaoEncontradoException extends RuntimeException {

    public EmprestimoNaoEncontradoException(UUID id) {
        super("Empréstimo não encontrado: " + id);
    }
}