package br.edu.ifpb.spendwise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.spendwise.model.Correntista;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {
    boolean existsByLogin(String login);
    Optional<Correntista> findByLogin(String login);
    List<Correntista> findAllByOrderByNomeAsc();
}
