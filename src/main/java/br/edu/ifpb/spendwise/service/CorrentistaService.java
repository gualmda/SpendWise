package br.edu.ifpb.spendwise.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.spendwise.model.Correntista;
import br.edu.ifpb.spendwise.repository.CorrentistaRepository;

@Service
public class CorrentistaService {

    private final CorrentistaRepository correntistaRepository;

    public CorrentistaService(CorrentistaRepository correntistaRepository) {
        this.correntistaRepository = correntistaRepository;
    }

    public List<Correntista> listar() {
        return correntistaRepository.findAllByOrderByNomeAsc();
    }

    public Correntista buscar(Long id) {
        return correntistaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Correntista não encontrado"));
    }

    public Optional<Correntista> autenticar(String login, String senha) {
        return correntistaRepository.findByLogin(login)
            .filter(correntista -> correntista.getSenha().equals(senha));
    }

    @Transactional
    public Correntista salvar(Correntista correntista) {
        if (correntistaRepository.existsByLogin(correntista.getLogin())) {
            throw new IllegalArgumentException("Login já cadastrado");
        }

        correntista.setId(null);
        correntista.setAdmin(false);
        return correntistaRepository.save(correntista);
    }
}
