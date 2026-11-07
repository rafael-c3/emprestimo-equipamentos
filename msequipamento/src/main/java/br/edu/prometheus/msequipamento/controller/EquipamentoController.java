package br.edu.prometheus.msequipamento.controller;

import br.edu.prometheus.msequipamento.dto.CriarEquipamentoRequest;
import br.edu.prometheus.msequipamento.dto.EquipamentoResponse;
import br.edu.prometheus.msequipamento.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @PostMapping
    public ResponseEntity<EquipamentoResponse> criar(
            @Valid @RequestBody CriarEquipamentoRequest request
    ) {
        EquipamentoResponse equipamento = equipamentoService.criar(request);
        URI location = URI.create("/api/v1/equipamentos/" + equipamento.id());

        return ResponseEntity.created(location).body(equipamento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponse> buscarPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(equipamentoService.buscarPorId(id));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<EquipamentoResponse>> listarDisponiveis() {
        return ResponseEntity.ok(equipamentoService.listarDisponiveis());
    }

    @PutMapping("/{id}/reservas/{emprestimoId}")
    public ResponseEntity<EquipamentoResponse> reservar(
            @PathVariable("id") UUID equipamentoId,
            @PathVariable UUID emprestimoId
    ) {
        return ResponseEntity.ok(
                equipamentoService.reservar(equipamentoId, emprestimoId)
        );
    }

    @DeleteMapping("/{id}/reservas/{emprestimoId}")
    public ResponseEntity<Void> liberar(
            @PathVariable("id") UUID equipamentoId,
            @PathVariable UUID emprestimoId
    ) {
        equipamentoService.liberar(equipamentoId, emprestimoId);
        return ResponseEntity.noContent().build();
    }
}