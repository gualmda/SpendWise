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
    public List<Conta> listar() {
        List<Conta> contas = contaRepository.findAllByOrderByDescricaoAsc();
        contas.forEach(conta -> conta.getTransacoes().size());
        return contas;
    }

    public Conta buscar(Long id) {
        return contaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }

    @Transactional
    public Conta criar(Conta conta, Long correntistaId) {
        Correntista correntista = correntistaRepository.findById(correntistaId)
            .orElseThrow(() -> new IllegalArgumentException("Correntista não encontrado"));

        conta.setId(null);
        conta.setCorrentista(correntista);
        if (conta.getTipo() == TipoConta.CORRENTE) {
            conta.setDiaFechamento(null);
        }
        return contaRepository.save(conta);
    }
}
