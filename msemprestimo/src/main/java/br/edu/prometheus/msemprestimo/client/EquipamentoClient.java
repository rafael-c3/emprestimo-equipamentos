package br.edu.prometheus.msemprestimo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "msequipamento")
public interface EquipamentoClient {

    @GetMapping("/api/v1/equipamentos/{id}")
    EquipamentoRemoto buscarPorId(@PathVariable("id") UUID id);

    @PutMapping("/api/v1/equipamentos/{id}/reservas/{emprestimoId}")
    EquipamentoRemoto reservar(
            @PathVariable("id") UUID equipamentoId,
            @PathVariable("emprestimoId") UUID emprestimoId
    );

    @DeleteMapping("/api/v1/equipamentos/{id}/reservas/{emprestimoId}")
    void liberar(
            @PathVariable("id") UUID equipamentoId,
            @PathVariable("emprestimoId") UUID emprestimoId
    );

    record EquipamentoRemoto(
            UUID id,
            String nome,
            String tipo,
            String patrimonio,
            boolean disponivel
    ) {
    }
}