package br.edu.prometheus.msemprestimo.exception;

public class ServicoDependenciaIndisponivelException extends RuntimeException {

    public ServicoDependenciaIndisponivelException(String servico) {
        super("Não foi possível se comunicar com o serviço " + servico);
    }
}