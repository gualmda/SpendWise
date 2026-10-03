package br.edu.ifpb.spendwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.spendwise.model.Correntista;
import br.edu.ifpb.spendwise.service.CorrentistaService;

@Controller
@RequestMapping("/correntistas")
public class CorrentistaController {

    private final CorrentistaService correntistaService;

    public CorrentistaController(CorrentistaService correntistaService) {
        this.correntistaService = correntistaService;
    }

    @GetMapping
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("correntistas/list");
        mv.addObject("correntistas", correntistaService.listar());
        mv.addObject("usuarioAdmin", true);
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView formulario() {
        ModelAndView mv = new ModelAndView("correntistas/form");
        mv.addObject("correntista", new Correntista());
        mv.addObject("usuarioAdmin", true);
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView salvar(@ModelAttribute Correntista correntista,
                               RedirectAttributes redirectAttributes) {
        try {
            correntistaService.salvar(correntista);
        } catch (IllegalArgumentException e) {
            ModelAndView mv = new ModelAndView("correntistas/form");
            mv.addObject("correntista", correntista);
            mv.addObject("erro", e.getMessage());
            mv.addObject("usuarioAdmin", true);
            return mv;
        }

        redirectAttributes.addFlashAttribute("mensagem", "Correntista cadastrado com sucesso.");
        return new ModelAndView("redirect:/correntistas");
    }
}
