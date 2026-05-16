package prime.video.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prime.video.domain.User;
import prime.video.repository.UserRepository;
import prime.video.service.CreatorService;

import java.util.UUID;

@Controller
public class CreatorController {

    private final CreatorService creatorService;
    private final UserRepository userRepository;

    public CreatorController(CreatorService creatorService, UserRepository userRepository) {
        this.creatorService = creatorService;
        this.userRepository = userRepository;
    }

    /* ── Liste des Créateurs (Tendances Africaines) ─────────── */
    @GetMapping("/createurs")
    public String listCreators(Model model) {
        model.addAttribute("creators", creatorService.getTopCreators());
        model.addAttribute("pageTitle", "Créateurs Africains");
        model.addAttribute("activeMenu", "creators");
        return "creators-list";
    }

    /* ── Page pour devenir Créateur ─────────────────────────── */
    @GetMapping("/devenir-createur")
    public String upgradeForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/connexion";
        
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user != null && creatorService.getProfileByUserId(user.getId()) != null) {
            return "redirect:/mon-compte"; // Déjà créateur
        }
        
        model.addAttribute("pageTitle", "Devenir Créateur Pro");
        return "upgrade-creator";
    }

    @PostMapping("/devenir-createur")
    public String processUpgrade(
            @RequestParam String displayName,
            @RequestParam String bio,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes ra) {
        
        try {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            creatorService.upgradeToCreator(user.getId(), displayName, bio);
            ra.addFlashAttribute("successMsg", "Félicitations ! Vous êtes maintenant un créateur Pro.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Erreur : " + e.getMessage());
        }
        
        return "redirect:/mon-compte";
    }

    /* ── S'abonner à un créateur ────────────────────────────── */
    @PostMapping("/s-abonner/{creatorId}")
    public String subscribe(
            @PathVariable UUID creatorId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes ra) {
        
        if (userDetails == null) return "redirect:/connexion";
        
        try {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            creatorService.subscribe(user.getId(), creatorId);
            ra.addFlashAttribute("successMsg", "Vous êtes maintenant abonné !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Erreur : " + e.getMessage());
        }
        
        return "redirect:/createurs";
    }
}
