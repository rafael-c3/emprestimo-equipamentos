package br.edu.prometheus.msusuario.service;

import br.edu.prometheus.msusuario.dto.CriarUsuarioRequest;
import br.edu.prometheus.msusuario.dto.UsuarioResponse;
import br.edu.prometheus.msusuario.entity.Usuario;
import br.edu.prometheus.msusuario.exception.UsuarioNaoEncontradoException;
import br.edu.prometheus.msusuario.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponse criar(CriarUsuarioRequest request) {
        String email = request.email().trim();
        String matricula = request.matricula().trim();

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new DataIntegrityViolationException(
                    "Já existe um usuário cadastrado com este e-mail"
            );
        }

        if (usuarioRepository.existsByMatricula(matricula)) {
            throw new DataIntegrityViolationException(
                    "Já existe um usuário cadastrado com esta matrícula"
            );
        }

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                request.nome().trim(),
                email,
                matricula
        );

        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        return UsuarioResponse.de(usuario);
    }
}