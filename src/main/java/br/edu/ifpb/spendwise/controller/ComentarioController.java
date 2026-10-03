package br.edu.ifpb.spendwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.spendwise.model.Comentario;
import br.edu.ifpb.spendwise.model.Transacao;
import br.edu.ifpb.spendwise.service.ComentarioService;
import br.edu.ifpb.spendwise.service.TransacaoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final TransacaoService transacaoService;

    public ComentarioController(ComentarioService comentarioService,
                                TransacaoService transacaoService) {
        this.comentarioService = comentarioService;
        this.transacaoService = transacaoService;
    }

    @GetMapping("/form/{transacaoId}")
    public ModelAndView formulario(@PathVariable Long transacaoId, HttpSession session) {
        Transacao transacao = transacaoService.buscarAcessivel(transacaoId, usuarioId(session), admin(session));
        if (transacao.getComentario() != null) {
            return new ModelAndView("redirect:/comentarios/edit/" + transacao.getComentario().getId());
        }
        return formulario(new Comentario(), transacao, false, admin(session));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editar(@PathVariable Long id, HttpSession session) {
        Comentario comentario = comentarioService.buscarAcessivel(id, usuarioId(session), admin(session));
        return formulario(comentario, comentario.getTransacao(), true, admin(session));
    }

    @PostMapping("/save")
    public ModelAndView salvar(@ModelAttribute Comentario comentario,
                               @RequestParam Long transacaoId,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        comentarioService.criar(transacaoId, comentario, usuarioId(session), admin(session));
        redirectAttributes.addFlashAttribute("mensagem", "Comentário adicionado com sucesso.");
        return new ModelAndView("redirect:/contas");
    }

    @PostMapping("/update")
    public ModelAndView atualizar(@ModelAttribute Comentario comentario,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        comentarioService.atualizar(comentario, usuarioId(session), admin(session));
        redirectAttributes.addFlashAttribute("mensagem", "Comentário atualizado com sucesso.");
        return new ModelAndView("redirect:/contas");
    }

    @PostMapping("/delete/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {
        comentarioService.excluir(id, usuarioId(session), admin(session));
        redirectAttributes.addFlashAttribute("mensagem", "Comentário excluído com sucesso.");
        return "redirect:/contas";
    }

    private ModelAndView formulario(Comentario comentario, Transacao transacao, boolean edicao, boolean usuarioAdmin) {
        ModelAndView mv = new ModelAndView("comentarios/form");
        mv.addObject("comentario", comentario);
        mv.addObject("transacao", transacao);
        mv.addObject("transacaoId", transacao.getId());
        mv.addObject("edicao", edicao);
        mv.addObject("usuarioAdmin", usuarioAdmin);
        return mv;
    }

    private Long usuarioId(HttpSession session) {
        return (Long) session.getAttribute("usuarioId");
    }

    private boolean admin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("usuarioAdmin"));
    }
}
