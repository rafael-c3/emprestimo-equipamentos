package br.edu.prometheus.msemprestimo.controller;

import br.edu.prometheus.msemprestimo.dto.CriarEmprestimoRequest;
import br.edu.prometheus.msemprestimo.dto.EmprestimoResponse;
import br.edu.prometheus.msemprestimo.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> solicitar(
            @Valid @RequestBody CriarEmprestimoRequest request
    ) {
        EmprestimoResponse emprestimo =
                emprestimoService.solicitar(request);

        URI location = URI.create(
                "/api/v1/emprestimos/" + emprestimo.id()
        );

        return ResponseEntity.created(location).body(emprestimo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponse> buscarPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(emprestimoService.buscarPorId(id));
    }

    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<List<EmprestimoResponse>> buscarPorUsuario(
            @PathVariable UUID usuarioId
    ) {
        return ResponseEntity.ok(
                emprestimoService.buscarPorUsuario(usuarioId)
        );
    }

    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoResponse> devolver(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(emprestimoService.devolver(id));
    }
}