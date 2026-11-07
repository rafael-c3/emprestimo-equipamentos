package br.edu.prometheus.msequipamento.service;

import br.edu.prometheus.msequipamento.dto.CriarEquipamentoRequest;
import br.edu.prometheus.msequipamento.dto.EquipamentoResponse;
import br.edu.prometheus.msequipamento.entity.Equipamento;
import br.edu.prometheus.msequipamento.exception.EquipamentoIndisponivelException;
import br.edu.prometheus.msequipamento.exception.EquipamentoNaoEncontradoException;
import br.edu.prometheus.msequipamento.repository.EquipamentoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    @Transactional
    public EquipamentoResponse criar(CriarEquipamentoRequest request) {
        String patrimonio = request.patrimonio().trim();

        if (equipamentoRepository.existsByPatrimonio(patrimonio)) {
            throw new DataIntegrityViolationException(
                    "Já existe um equipamento com este patrimônio"
            );
        }

        Equipamento equipamento = new Equipamento(
                UUID.randomUUID(),
                request.nome().trim(),
                request.tipo().trim(),
                patrimonio,
                true
        );

        return EquipamentoResponse.de(equipamentoRepository.save(equipamento));
    }

    @Transactional(readOnly = true)
    public EquipamentoResponse buscarPorId(UUID id) {
        return EquipamentoResponse.de(obterEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<EquipamentoResponse> listarDisponiveis() {
        return equipamentoRepository.findByDisponivelTrueOrderByNomeAsc()
                .stream()
                .map(EquipamentoResponse::de)
                .toList();
    }

    @Transactional
    public EquipamentoResponse reservar(UUID equipamentoId, UUID emprestimoId) {
        int atualizadas = equipamentoRepository.reservarSeDisponivel(
                equipamentoId,
                emprestimoId
        );

        if (atualizadas == 1) {
            return buscarPorId(equipamentoId);
        }

        Equipamento equipamento = obterEntidade(equipamentoId);

        // Repetir a mesma reserva não altera o equipamento novamente.
        if (emprestimoId.equals(equipamento.getReservaEmprestimoId())) {
            return EquipamentoResponse.de(equipamento);
        }

        throw new EquipamentoIndisponivelException(equipamentoId);
    }

    @Transactional
    public void liberar(UUID equipamentoId, UUID emprestimoId) {
        int atualizadas =
                equipamentoRepository.liberarSeReservadoPeloEmprestimo(
                        equipamentoId,
                        emprestimoId
                );

        if (atualizadas == 1) {
            return;
        }

        Equipamento equipamento = obterEntidade(equipamentoId);

        // Uma repetição da liberação não produz outra alteração.
        if (equipamento.isDisponivel()) {
            return;
        }

        // Não libera a reserva pertencente a outro empréstimo.
        throw new EquipamentoIndisponivelException(equipamentoId);
    }

    private Equipamento obterEntidade(UUID id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new EquipamentoNaoEncontradoException(id));
    }
}