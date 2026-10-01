package br.edu.ifpb.spendwise.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.spendwise.model.Comentario;
import br.edu.ifpb.spendwise.model.Transacao;
import br.edu.ifpb.spendwise.repository.ComentarioRepository;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TransacaoService transacaoService;

    public ComentarioService(ComentarioRepository comentarioRepository,
                             TransacaoService transacaoService) {
        this.comentarioRepository = comentarioRepository;
        this.transacaoService = transacaoService;
    }

    @Transactional
    public Comentario criar(Long transacaoId, Comentario comentario) {
        Transacao transacao = transacaoService.buscar(transacaoId);
        if (comentarioRepository.existsByTransacaoId(transacaoId)) {
            throw new IllegalArgumentException("A transação já possui comentário");
        }

        comentario.setId(null);
        comentario.setTransacao(transacao);
        Comentario salvo = comentarioRepository.save(comentario);
        transacao.setComentario(salvo);
        return salvo;
    }

    public Comentario buscar(Long id) {
        return comentarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado"));
    }

    @Transactional
    public Comentario atualizar(Comentario dados) {
        Comentario existente = buscar(dados.getId());
        existente.setTexto(dados.getTexto());
        return comentarioRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Comentario comentario = buscar(id);
        comentario.getTransacao().setComentario(null);
        comentarioRepository.delete(comentario);
    }
}
