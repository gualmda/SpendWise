package br.edu.ifpb.spendwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.spendwise.model.Correntista;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {
    boolean existsByLogin(String login);
    List<Correntista> findAllByOrderByNomeAsc();
}
