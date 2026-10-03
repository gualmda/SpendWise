package br.edu.ifpb.spendwise.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.spendwise.model.Categoria;
import br.edu.ifpb.spendwise.model.Comentario;
import br.edu.ifpb.spendwise.model.Movimento;
import br.edu.ifpb.spendwise.model.Transacao;
import br.edu.ifpb.spendwise.repository.CategoriaRepository;
import br.edu.ifpb.spendwise.service.ComentarioService;
import br.edu.ifpb.spendwise.service.ContaService;
import br.edu.ifpb.spendwise.service.TransacaoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final ContaService contaService;
    private final CategoriaRepository categoriaRepository;
    private final ComentarioService comentarioService;

    public TransacaoController(TransacaoService transacaoService,
                               ContaService contaService,
                               CategoriaRepository categoriaRepository,
                               ComentarioService comentarioService) {
        this.transacaoService = transacaoService;
        this.contaService = contaService;
        this.categoriaRepository = categoriaRepository;
        this.comentarioService = comentarioService;
    }

    @GetMapping("/form/{contaId}")
    public ModelAndView formulario(@PathVariable Long contaId, HttpSession session) {
        contaService.buscarAcessivel(contaId, usuarioId(session), admin(session));
        return formulario(new Transacao(), contaId, null, false, session);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editar(@PathVariable Long id, HttpSession session) {
        Transacao transacao = transacaoService.buscarAcessivel(id, usuarioId(session), admin(session));
        return formulario(transacao, transacao.getConta().getId(), transacao.getCategoria(), true, session);
    }

    @PostMapping("/save")
    public ModelAndView salvar(@ModelAttribute Transacao transacao,
                               @RequestParam Long contaId,
                               @RequestParam Long categoriaId,
                               @RequestParam(required = false) String comentarioTexto,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        Long usuarioId = usuarioId(session);
        boolean admin = admin(session);
        Transacao salva = transacaoService.criar(transacao, contaId, categoriaId, usuarioId, admin);

        if (comentarioTexto != null && !comentarioTexto.isBlank()) {
            Comentario comentario = new Comentario();
            comentario.setTexto(comentarioTexto);
            comentarioService.criar(salva.getId(), comentario, usuarioId, admin);
        }

        redirectAttributes.addFlashAttribute("mensagem", "Transação cadastrada com sucesso.");
        return new ModelAndView("redirect:/contas");
    }

    @PostMapping("/update")
    public ModelAndView atualizar(@ModelAttribute Transacao transacao,
                                  @RequestParam Long contaId,
                                  @RequestParam Long categoriaId,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        transacaoService.atualizar(transacao, contaId, categoriaId, usuarioId(session), admin(session));
        redirectAttributes.addFlashAttribute("mensagem", "Transação atualizada com sucesso.");
        return new ModelAndView("redirect:/contas");
    }

    private ModelAndView formulario(Transacao transacao,
                                    Long contaId,
                                    Categoria categoriaAtual,
                                    boolean edicao,
                                    HttpSession session) {
        ModelAndView mv = new ModelAndView("transacoes/form");
        mv.addObject("transacao", transacao);
        mv.addObject("contaId", contaId);
        mv.addObject("movimentos", Movimento.values());
        mv.addObject("edicao", edicao);
        mv.addObject("usuarioAdmin", admin(session));
        mv.addObject("contas", contaService.listar(usuarioId(session), admin(session)));

        List<Categoria> categorias = new ArrayList<>(categoriaRepository.findByAtivaTrueOrderByNaturezaAscOrdemAsc());
        if (categoriaAtual != null && !categoriaAtual.isAtiva()
                && categorias.stream().noneMatch(c -> c.getId().equals(categoriaAtual.getId()))) {
            categorias.add(categoriaAtual);
        }
        mv.addObject("categorias", categorias);
        mv.addObject("categoriaId", categoriaAtual == null ? null : categoriaAtual.getId());
        return mv;
    }

    private Long usuarioId(HttpSession session) {
        return (Long) session.getAttribute("usuarioId");
    }

    private boolean admin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("usuarioAdmin"));
    }
}
