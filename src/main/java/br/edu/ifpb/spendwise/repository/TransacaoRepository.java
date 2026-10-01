package br.edu.ifpb.spendwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.spendwise.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
