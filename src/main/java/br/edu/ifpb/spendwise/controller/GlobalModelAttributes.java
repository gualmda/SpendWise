package br.edu.ifpb.spendwise.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void adicionarContextoDoUsuario(HttpSession session, Model model) {
        Object usuarioNome = session.getAttribute("usuarioNome");
        Object usuarioAdmin = session.getAttribute("usuarioAdmin");

        if (usuarioNome != null) {
            model.addAttribute("usuarioNome", usuarioNome);
        }
        if (usuarioAdmin != null) {
            model.addAttribute("usuarioAdmin", usuarioAdmin);
        }
    }
}
