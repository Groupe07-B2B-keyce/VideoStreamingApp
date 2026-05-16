package prime.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour les pages d'erreur UI — affichage friendly.
 */
@Controller
public class ErrorUIController {

    /**
     * Page d'accès refusé friendly (403/401).
     * Remplace le 403 brut par une page avec des options claires.
     */
    @GetMapping("/acces-refuse")
    public String accessDenied(
            @RequestParam(required = false) String redirect,
            Model model) {
        model.addAttribute("redirectTo", redirect != null ? redirect : "/");
        model.addAttribute("pageTitle", "Accès refusé");
        return "error/access-denied";
    }
}
