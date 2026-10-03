package br.edu.ifpb.spendwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.spendwise.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findAllByOrderByDescricaoAsc();
    List<Conta> findAllByCorrentistaIdOrderByDescricaoAsc(Long correntistaId);
}
