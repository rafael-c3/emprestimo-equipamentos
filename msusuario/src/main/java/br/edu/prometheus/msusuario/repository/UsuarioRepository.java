package br.edu.prometheus.msusuario.repository;

import br.edu.prometheus.msusuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByMatricula(String matricula);
}