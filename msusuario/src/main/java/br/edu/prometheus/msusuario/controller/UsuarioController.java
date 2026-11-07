package br.edu.prometheus.msusuario.controller;

import br.edu.prometheus.msusuario.dto.CriarUsuarioRequest;
import br.edu.prometheus.msusuario.dto.UsuarioResponse;
import br.edu.prometheus.msusuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(
            @Valid @RequestBody CriarUsuarioRequest request
    ) {
        UsuarioResponse usuario = usuarioService.criar(request);

        URI location = URI.create("/api/v1/usuarios/" + usuario.id());

        return ResponseEntity
                .created(location)
                .body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}