package br.edu.ifpb.spendwise.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.spendwise.model.Categoria;
import br.edu.ifpb.spendwise.model.Correntista;
import br.edu.ifpb.spendwise.model.Natureza;
import br.edu.ifpb.spendwise.repository.CategoriaRepository;
import br.edu.ifpb.spendwise.repository.CorrentistaRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final CorrentistaRepository correntistaRepository;

    public DataInitializer(CategoriaRepository categoriaRepository,
                           CorrentistaRepository correntistaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.correntistaRepository = correntistaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        criarCategoriasIniciais();
        criarAdministradorInicial();
    }

    private void criarCategoriasIniciais() {
        if (categoriaRepository.count() > 0) {
            return;
        }

        adicionar("Salário", Natureza.ENTRADA, 1);
        adicionar("Cashback", Natureza.ENTRADA, 2);
        adicionar("Resgate Investimento", Natureza.ENTRADA, 3);
        adicionar("Outras Entradas", Natureza.ENTRADA, 4);

        adicionar("Saúde e Remédios", Natureza.SAIDA, 1);
        adicionar("Academia e Personal", Natureza.SAIDA, 2);
        adicionar("Carros e Uber", Natureza.SAIDA, 3);
        adicionar("Educação e Cursos", Natureza.SAIDA, 4);
        adicionar("Lazer e Turismo", Natureza.SAIDA, 5);
        adicionar("Condomínio", Natureza.SAIDA, 6);
        adicionar("Energia", Natureza.SAIDA, 7);
        adicionar("Celular", Natureza.SAIDA, 8);
        adicionar("Internet", Natureza.SAIDA, 9);
        adicionar("Itens Pessoais", Natureza.SAIDA, 10);
        adicionar("Feira", Natureza.SAIDA, 11);
        adicionar("Casa", Natureza.SAIDA, 12);
        adicionar("Impostos", Natureza.SAIDA, 13);
        adicionar("Outros gastos", Natureza.SAIDA, 14);

        adicionar("Aporte Renda Fixa", Natureza.INVESTIMENTO, 1);
        adicionar("Aporte Renda Variável", Natureza.INVESTIMENTO, 2);
        adicionar("Aporte Reserva Emergencia", Natureza.INVESTIMENTO, 3);
        adicionar("Aporte Previdência", Natureza.INVESTIMENTO, 4);
    }

    private void criarAdministradorInicial() {
        Correntista admin = correntistaRepository.findByLogin("admin").orElseGet(Correntista::new);
        admin.setNome("Administrador");
        admin.setLogin("admin");
        admin.setSenha("admin123");
        admin.setAdmin(true);
        correntistaRepository.save(admin);
    }

    private void adicionar(String nome, Natureza natureza, int ordem) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setNatureza(natureza);
        categoria.setOrdem(ordem);
        categoria.setAtiva(true);
        categoriaRepository.save(categoria);
    }
}
