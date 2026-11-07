package br.edu.prometheus.msequipamento.exception;

import java.time.Instant;

public record ApiError(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho
) {
}