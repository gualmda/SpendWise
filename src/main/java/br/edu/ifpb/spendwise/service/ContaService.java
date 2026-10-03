package br.edu.ifpb.spendwise.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.spendwise.model.Conta;
import br.edu.ifpb.spendwise.model.Correntista;
import br.edu.ifpb.spendwise.model.TipoConta;
import br.edu.ifpb.spendwise.repository.ContaRepository;
import br.edu.ifpb.spendwise.repository.CorrentistaRepository;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final CorrentistaRepository correntistaRepository;

    public ContaService(ContaRepository contaRepository, CorrentistaRepository correntistaRepository) {
        this.contaRepository = contaRepository;
        this.correntistaRepository = correntistaRepository;
    }

    @Transactional(readOnly = true)
    public List<Conta> listar(Long usuarioId, boolean admin) {
        List<Conta> contas = admin
            ? contaRepository.findAllByOrderByDescricaoAsc()
            : contaRepository.findAllByCorrentistaIdOrderByDescricaoAsc(usuarioId);
        contas.forEach(conta -> conta.getTransacoes().size());
        return contas;
    }

    public Conta buscar(Long id) {
        return contaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }

    public Conta buscarAcessivel(Long id, Long usuarioId, boolean admin) {
        Conta conta = buscar(id);
        if (!admin && !conta.getCorrentista().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Conta não pertence ao correntista logado");
        }
        return conta;
    }

    @Transactional
    public Conta criar(Conta conta, Long correntistaId, Long usuarioId, boolean admin) {
        Long donoId = admin ? correntistaId : usuarioId;
        if (donoId == null) {
            throw new IllegalArgumentException("Selecione um correntista");
        }

        Correntista correntista = correntistaRepository.findById(donoId)
            .orElseThrow(() -> new IllegalArgumentException("Correntista não encontrado"));

        conta.setId(null);
        conta.setCorrentista(correntista);
        if (conta.getTipo() == TipoConta.CORRENTE) {
            conta.setDiaFechamento(null);
        }
        return contaRepository.save(conta);
    }
}
