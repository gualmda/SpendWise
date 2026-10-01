package br.edu.ifpb.spendwise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.spendwise.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    Optional<Comentario> findByTransacaoId(Long transacaoId);
    boolean existsByTransacaoId(Long transacaoId);
}
