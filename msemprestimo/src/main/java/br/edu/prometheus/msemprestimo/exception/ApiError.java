package br.edu.prometheus.msemprestimo.exception;

import java.time.Instant;

public record ApiError(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho
) {
}