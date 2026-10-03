package br.edu.ifpb.spendwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.spendwise.model.Conta;
import br.edu.ifpb.spendwise.model.TipoConta;
import br.edu.ifpb.spendwise.service.ContaService;
import br.edu.ifpb.spendwise.service.CorrentistaService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;
    private final CorrentistaService correntistaService;

    public ContaController(ContaService contaService, CorrentistaService correntistaService) {
        this.contaService = contaService;
        this.correntistaService = correntistaService;
    }

    @GetMapping
    public ModelAndView listar(HttpSession session) {
        Long usuarioId = usuarioId(session);
        boolean admin = admin(session);
        ModelAndView mv = new ModelAndView("contas/list");
        mv.addObject("contas", contaService.listar(usuarioId, admin));
        mv.addObject("usuarioAdmin", admin);
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formulario(HttpSession session) {
        return formulario(new Conta(), null, session);
    }

    @PostMapping("/save")
    public ModelAndView salvar(@ModelAttribute Conta conta,
                               @RequestParam(required = false) Long correntistaId,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        try {
            contaService.criar(conta, correntistaId, usuarioId(session), admin(session));
        } catch (IllegalArgumentException e) {
            ModelAndView mv = formulario(conta, correntistaId, session);
            mv.addObject("erro", e.getMessage());
            return mv;
        }

        redirectAttributes.addFlashAttribute("mensagem", "Conta cadastrada com sucesso.");
        return new ModelAndView("redirect:/contas");
    }

    private ModelAndView formulario(Conta conta, Long correntistaId, HttpSession session) {
        boolean admin = admin(session);
        ModelAndView mv = new ModelAndView("contas/form");
        mv.addObject("conta", conta);
        mv.addObject("tipos", TipoConta.values());
        mv.addObject("correntistaId", correntistaId);
        mv.addObject("admin", admin);
        mv.addObject("usuarioAdmin", admin);
        if (admin) {
            mv.addObject("correntistas", correntistaService.listar());
        } else {
            mv.addObject("usuario", correntistaService.buscar(usuarioId(session)));
        }
        return mv;
    }

    private Long usuarioId(HttpSession session) {
        return (Long) session.getAttribute("usuarioId");
    }

    private boolean admin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("usuarioAdmin"));
    }
}
