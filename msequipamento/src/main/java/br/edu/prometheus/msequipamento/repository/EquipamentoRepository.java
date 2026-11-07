package br.edu.prometheus.msequipamento.repository;

import br.edu.prometheus.msequipamento.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID> {

    boolean existsByPatrimonio(String patrimonio);

    List<Equipamento> findByDisponivelTrueOrderByNomeAsc();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Equipamento e
               set e.disponivel = false,
                   e.reservaEmprestimoId = :emprestimoId
             where e.id = :equipamentoId
               and e.disponivel = true
               and (
                    e.reservaEmprestimoId is null
                    or e.reservaEmprestimoId <> :emprestimoId
               )
            """)
    int reservarSeDisponivel(
            @Param("equipamentoId") UUID equipamentoId,
            @Param("emprestimoId") UUID emprestimoId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Equipamento e
               set e.disponivel = true
             where e.id = :equipamentoId
               and e.disponivel = false
               and e.reservaEmprestimoId = :emprestimoId
            """)
    int liberarSeReservadoPeloEmprestimo(
            @Param("equipamentoId") UUID equipamentoId,
            @Param("emprestimoId") UUID emprestimoId
    );
}