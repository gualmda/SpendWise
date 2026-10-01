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
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("contas/list");
        mv.addObject("contas", contaService.listar());
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formulario() {
        return formulario(new Conta(), null);
    }

    @PostMapping("/save")
    public ModelAndView salvar(@ModelAttribute Conta conta,
                               @RequestParam Long correntistaId,
                               RedirectAttributes redirectAttributes) {
        try {
            contaService.criar(conta, correntistaId);
        } catch (IllegalArgumentException e) {
            ModelAndView mv = formulario(conta, correntistaId);
            mv.addObject("erro", e.getMessage());
            return mv;
        }

        redirectAttributes.addFlashAttribute("mensagem", "Conta cadastrada com sucesso.");
        return new ModelAndView("redirect:/contas");
    }

    private ModelAndView formulario(Conta conta, Long correntistaId) {
        ModelAndView mv = new ModelAndView("contas/form");
        mv.addObject("conta", conta);
        mv.addObject("tipos", TipoConta.values());
        mv.addObject("correntistaId", correntistaId);
        mv.addObject("correntistas", correntistaService.listar());
        return mv;
    }
}
