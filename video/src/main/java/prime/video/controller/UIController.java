package prime.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import prime.video.service.TitleService;
import prime.video.service.ViewCountService;

import java.util.UUID;

/**
 * UI Controller — Renvoie des vues HTML Thymeleaf.
 * Séparé strictement du @RestController (JSON) conformément aux normes REST.
 * Routes UI : /, /films, /series, /genres, /titres/{id}, /recherche,
 *              /ma-liste, /historique, /profils, /connexion, /inscription, etc.
 */
@Controller
public class UIController {

    private final TitleService titleService;
    private final ViewCountService viewCountService;

    public UIController(TitleService titleService, ViewCountService viewCountService) {
        this.titleService = titleService;
        this.viewCountService = viewCountService;
    }

    /* ── Page d'accueil ────────────────────────────────────── */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("titles", titleService.getAllTitleSummaries());
        model.addAttribute("activeMenu", "home");
        model.addAttribute("pageTitle", "Accueil");
        return "index";
    }

    /* ── Catalogue Films ────────────────────────────────────── */
    @GetMapping("/films")
    public String films(
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        model.addAttribute("titles", titleService.getAllTitleSummaries());
        model.addAttribute("activeMenu", "movies");
        model.addAttribute("pageTitle", "Films");
        model.addAttribute("selectedGenre", genre);
        return "catalogue";
    }

    /* ── Catalogue Séries ───────────────────────────────────── */
    @GetMapping("/series")
    public String series(
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        model.addAttribute("titles", titleService.getAllTitleSummaries());
        model.addAttribute("activeMenu", "series");
        model.addAttribute("pageTitle", "Séries");
        model.addAttribute("selectedGenre", genre);
        return "catalogue";
    }

    /* ── Page Genres ────────────────────────────────────────── */
    @GetMapping("/genres")
    public String genres(Model model) {
        model.addAttribute("activeMenu", "genres");
        model.addAttribute("pageTitle", "Genres");
        return "genres";
    }

    /* ── Détail d'un titre ──────────────────────────────────── */
    @GetMapping("/titres/{id}")
    public String titleDetail(@PathVariable UUID id, Model model) {
        var title = titleService.getTitleModelById(id);
        if (title == null) {
            return "redirect:/";
        }
        model.addAttribute("title", title);
        model.addAttribute("pageTitle", title.getOriginalTitle());
        return "title-detail";
    }

    /* ── Recherche ──────────────────────────────────────────── */
    @GetMapping("/recherche")
    public String search(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("query", q);
        model.addAttribute("titles", titleService.getAllTitleSummaries());
        model.addAttribute("activeMenu", "search");
        model.addAttribute("pageTitle", "Recherche : " + (q != null ? q : ""));
        return "search";
    }

    /* ── Ma liste (watchlist) ───────────────────────────────── */
    @GetMapping("/ma-liste")
    public String watchlist(Model model) {
        model.addAttribute("activeMenu", "watchlist");
        model.addAttribute("pageTitle", "Ma liste");
        return "watchlist";
    }

    /* ── Historique ─────────────────────────────────────────── */
    @GetMapping("/historique")
    public String history(Model model) {
        model.addAttribute("activeMenu", "history");
        model.addAttribute("pageTitle", "Historique");
        return "history";
    }

    /* ── Profils ────────────────────────────────────────────── */
    @GetMapping("/profils")
    public String profiles(Model model) {
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("pageTitle", "Mes profils");
        return "profiles";
    }

    /* ── Mon compte ─────────────────────────────────────────── */
    @GetMapping("/mon-compte")
    public String myAccount(Model model) {
        model.addAttribute("pageTitle", "Mon compte");
        return "account";
    }

    /* ── Paramètres ─────────────────────────────────────────── */
    @GetMapping("/parametres")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "Paramètres");
        return "settings";
    }

    /* ── Tendances ──────────────────────────────────────────── */
    @GetMapping("/tendances")
    public String trending(Model model) {
        model.addAttribute("titles", titleService.getAllTitleSummaries());
        model.addAttribute("activeMenu", "trending");
        model.addAttribute("pageTitle", "Tendances");
        return "catalogue";
    }

    /* ── Authentification (pages UI) ────────────────────────── */
    @GetMapping("/connexion")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/inscription")
    public String registerPage() {
        return "register";
    }

    /* ── Lecture vidéo ──────────────────────────────────────── */
    @GetMapping("/lecture/{id}")
    public String playback(@PathVariable UUID id, Model model) {
        var title = titleService.getTitleModelById(id);
        if (title == null) {
            return "redirect:/";
        }
        // Compter la vue à chaque lancement de lecture
        viewCountService.incrementViewCount(id);
        model.addAttribute("title", title);
        model.addAttribute("pageTitle", "Lecture : " + title.getOriginalTitle());
        return "player";
    }
}
