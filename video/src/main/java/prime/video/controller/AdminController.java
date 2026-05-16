package prime.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prime.video.api.AdminApiDelegate;
import prime.video.domain.Genre;
import prime.video.domain.Title;
import prime.video.model.GlobalPlatformStats;
import prime.video.service.AdminTitleService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminApiDelegate adminApiDelegate;
    private final AdminTitleService adminTitleService;

    public AdminController(AdminApiDelegate adminApiDelegate, AdminTitleService adminTitleService) {
        this.adminApiDelegate = adminApiDelegate;
        this.adminTitleService = adminTitleService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        GlobalPlatformStats stats = adminApiDelegate.getGlobalStats().getBody();
        List<Title> recentTitles = adminTitleService.getAllTitles();
        model.addAttribute("stats", stats);
        model.addAttribute("recentTitles", recentTitles);
        model.addAttribute("activeMenu", "admin-dash");
        return "admin/dashboard";
    }

    @GetMapping("/catalogue")
    public String manageCatalogue(Model model) {
        List<Title> titles = adminTitleService.getAllTitles();
        List<Genre> genres = adminTitleService.getAllGenres();
        model.addAttribute("titles", titles);
        model.addAttribute("genres", genres);
        model.addAttribute("activeMenu", "admin-cat");
        return "admin/catalogue";
    }

    /**
     * Ajout d'un titre — tous les champs conformes au SubmitTitleRequest du YAML.
     */
    @PostMapping("/catalogue/ajouter")
    public String addTitle(
            // Infos de base
            @RequestParam String originalTitle,
            @RequestParam(required = false) String localizedTitle,
            @RequestParam String type,
            @RequestParam String rating,
            @RequestParam Integer releaseYear,
            @RequestParam(required = false) String synopsis,
            @RequestParam(required = false) String tagline,
            // Médias
            @RequestParam(required = false) String posterUrl,
            @RequestParam(required = false) String backdropUrl,
            @RequestParam(required = false) String trailerUrl,
            @RequestParam(required = false) String videoFileUrl,
            // Données externes
            @RequestParam(required = false) String imdbId,
            @RequestParam(required = false) Double imdbRating,
            // Détails techniques
            @RequestParam(required = false) String resolutionMax,
            @RequestParam(required = false) Integer durationMinutes,
            @RequestParam(required = false) Boolean hasHDR,
            @RequestParam(required = false) Boolean hasDolbyVision,
            @RequestParam(required = false) Boolean hasDolbyAtmos,
            @RequestParam(required = false) String audioLanguages,
            @RequestParam(required = false) String subtitleLanguages,
            // Accès & Pricing
            @RequestParam(required = false) String accessType,
            @RequestParam(required = false) Double rentalPrice,
            @RequestParam(required = false) Double purchasePrice,
            @RequestParam(required = false) String currency,
            // Casting
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String castMembers,
            // Séries
            @RequestParam(required = false) Integer totalSeasonsCount,
            // Genres
            @RequestParam(required = false) List<UUID> genreIds,
            RedirectAttributes ra) {
        try {
            adminTitleService.createTitle(
                    originalTitle, localizedTitle, type, rating, releaseYear, synopsis, tagline,
                    posterUrl, backdropUrl, trailerUrl, videoFileUrl,
                    imdbId, imdbRating,
                    resolutionMax, durationMinutes, hasHDR, hasDolbyVision, hasDolbyAtmos,
                    audioLanguages, subtitleLanguages,
                    accessType, rentalPrice, purchasePrice, currency,
                    director, castMembers,
                    totalSeasonsCount, genreIds);
            ra.addFlashAttribute("successMsg", "Film « " + originalTitle + " » ajouté avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Erreur : " + e.getMessage());
        }
        return "redirect:/admin/catalogue";
    }

    @PostMapping("/catalogue/supprimer/{id}")
    public String deleteTitle(@PathVariable UUID id, RedirectAttributes ra) {
        try {
            adminTitleService.deleteTitle(id);
            ra.addFlashAttribute("successMsg", "Titre supprimé.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Erreur suppression : " + e.getMessage());
        }
        return "redirect:/admin/catalogue";
    }

    @GetMapping("/utilisateurs")
    public String manageUsers(Model model) {
        model.addAttribute("activeMenu", "admin-users");
        return "admin/users";
    }
}
