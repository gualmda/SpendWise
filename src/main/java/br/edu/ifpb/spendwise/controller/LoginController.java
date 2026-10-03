package br.edu.ifpb.spendwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.spendwise.model.Correntista;
import br.edu.ifpb.spendwise.service.CorrentistaService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final CorrentistaService correntistaService;

    public LoginController(CorrentistaService correntistaService) {
        this.correntistaService = correntistaService;
    }

    @GetMapping("/")
    public String inicio(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("usuarioAdmin"))
            ? "redirect:/correntistas"
            : "redirect:/contas";
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ModelAndView autenticar(@RequestParam String login,
                                   @RequestParam String senha,
                                   HttpSession session) {
        Correntista usuario = correntistaService.autenticar(login, senha).orElse(null);
        if (usuario == null) {
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("erro", "Login ou senha inválidos.");
            mv.addObject("loginInformado", login);
            return mv;
        }

        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNome", usuario.getNome());
        session.setAttribute("usuarioAdmin", usuario.isAdmin());

        return new ModelAndView(usuario.isAdmin() ? "redirect:/correntistas" : "redirect:/contas");
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
