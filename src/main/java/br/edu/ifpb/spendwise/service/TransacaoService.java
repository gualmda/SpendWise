package br.edu.ifpb.spendwise.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.spendwise.model.Categoria;
import br.edu.ifpb.spendwise.model.Conta;
import br.edu.ifpb.spendwise.model.Transacao;
import br.edu.ifpb.spendwise.repository.CategoriaRepository;
import br.edu.ifpb.spendwise.repository.TransacaoRepository;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ContaService contaService;

    public TransacaoService(TransacaoRepository transacaoRepository,
                            CategoriaRepository categoriaRepository,
                            ContaService contaService) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.contaService = contaService;
    }

    public Transacao buscar(Long id) {
        return transacaoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
    }

    public Transacao buscarAcessivel(Long id, Long usuarioId, boolean admin) {
        Transacao transacao = buscar(id);
        contaService.buscarAcessivel(transacao.getConta().getId(), usuarioId, admin);
        return transacao;
    }

    @Transactional
    public Transacao criar(Transacao transacao, Long contaId, Long categoriaId,
                           Long usuarioId, boolean admin) {
        Conta conta = contaService.buscarAcessivel(contaId, usuarioId, admin);
        Categoria categoria = buscarCategoria(categoriaId);

        if (!categoria.isAtiva()) {
            throw new IllegalArgumentException("A categoria selecionada está desativada");
        }

        transacao.setId(null);
        transacao.setConta(conta);
        transacao.setCategoria(categoria);
        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao atualizar(Transacao dados, Long contaId, Long categoriaId,
                               Long usuarioId, boolean admin) {
        Transacao existente = buscarAcessivel(dados.getId(), usuarioId, admin);
        Conta conta = contaService.buscarAcessivel(contaId, usuarioId, admin);
        Categoria categoria = buscarCategoria(categoriaId);

        boolean mesmaCategoria = existente.getCategoria().getId().equals(categoria.getId());
        if (!categoria.isAtiva() && !mesmaCategoria) {
            throw new IllegalArgumentException("A categoria selecionada está desativada");
        }

        existente.setData(dados.getData());
        existente.setDescricao(dados.getDescricao());
        existente.setValor(dados.getValor());
        existente.setMovimento(dados.getMovimento());
        existente.setConta(conta);
        existente.setCategoria(categoria);
        return transacaoRepository.save(existente);
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
    }
}
