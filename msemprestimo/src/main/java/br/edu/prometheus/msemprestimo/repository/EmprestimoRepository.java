package br.edu.prometheus.msemprestimo.repository;

import br.edu.prometheus.msemprestimo.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    List<Emprestimo> findByUsuarioIdOrderByDataEmprestimoDesc(UUID usuarioId);
}