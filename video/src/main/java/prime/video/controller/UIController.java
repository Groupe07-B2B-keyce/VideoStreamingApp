package prime.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prime.video.service.AuthService;
import prime.video.service.ProfileService;
import prime.video.service.TitleService;
import prime.video.service.ViewCountService;

import java.security.Principal;
import java.util.UUID;

@Controller
public class UIController {

    private final TitleService titleService;
    private final ViewCountService viewCountService;
    private final AuthService authService;
    private final ProfileService profileService;

    public UIController(TitleService titleService,
                        ViewCountService viewCountService,
                        AuthService authService,
                        ProfileService profileService) {
        this.titleService = titleService;
        this.viewCountService = viewCountService;
        this.authService = authService;
        this.profileService = profileService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String genre,
                        @RequestParam(required = false) String type,
                        Model model,
                        Principal principal) {
        model.addAttribute("titles", titleService.getFilteredTitleSummaries(type, genre, null));
        model.addAttribute("featured", titleService.getFeaturedTitle());
        model.addAttribute("activeMenu", "home");
        model.addAttribute("pageTitle", "Accueil");
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedType", type);
        model.addAttribute("currentPath", "/");
        model.addAttribute("isLoggedIn", principal != null);
        return "index";
    }

    @GetMapping("/films")
    public String films(@RequestParam(required = false) String genre,
                        @RequestParam(defaultValue = "1") int page,
                        Model model) {
        model.addAttribute("titles", titleService.getFilteredTitleSummaries("MOVIE", genre, null));
        model.addAttribute("activeMenu", "movies");
        model.addAttribute("pageTitle", "Films");
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedType", "MOVIE");
        model.addAttribute("currentPath", "/films");
        return "catalogue";
    }

    @GetMapping("/series")
    public String series(@RequestParam(required = false) String genre,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
        model.addAttribute("titles", titleService.getFilteredTitleSummaries("SERIES", genre, null));
        model.addAttribute("activeMenu", "series");
        model.addAttribute("pageTitle", "Series");
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedType", "SERIES");
        model.addAttribute("currentPath", "/series");
        return "catalogue";
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        model.addAttribute("activeMenu", "genres");
        model.addAttribute("pageTitle", "Genres");
        return "genres";
    }

    @GetMapping("/titres/{id}")
    public String titleDetail(@PathVariable UUID id, Model model) {
        var title = titleService.getTitleDomainById(id);
        if (title == null) {
            return "redirect:/";
        }
        model.addAttribute("title", title);
        model.addAttribute("pageTitle", title.getOriginalTitle());
        return "title-detail";
    }

    @GetMapping({"/recherche", "/search"})
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String genre,
                         Model model) {
        model.addAttribute("query", q);
        model.addAttribute("titles", titleService.getFilteredTitleSummaries(null, genre, q));
        model.addAttribute("activeMenu", "search");
        model.addAttribute("pageTitle", "Recherche : " + (q != null ? q : ""));
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("currentPath", "/recherche");
        return "search";
    }

    @GetMapping("/ma-liste")
    public String watchlist(Model model) {
        model.addAttribute("activeMenu", "watchlist");
        model.addAttribute("pageTitle", "Ma liste");
        return "watchlist";
    }

    @GetMapping("/historique")
    public String history(Model model) {
        model.addAttribute("activeMenu", "history");
        model.addAttribute("pageTitle", "Historique");
        return "history";
    }

    @GetMapping("/profils")
    public String profiles(Model model) {
        model.addAttribute("profiles", profileService.getCurrentUserDomainProfiles());
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("pageTitle", "Mes profils");
        return "profiles";
    }

    @PostMapping("/profils")
    public String createProfile(@RequestParam String displayName,
                                @RequestParam(defaultValue = "false") boolean kidsProfile,
                                RedirectAttributes redirectAttributes) {
        var profile = profileService.createProfile(displayName, kidsProfile);
        redirectAttributes.addFlashAttribute("successMsg", "Profil cree avec succes.");
        return "redirect:/profils/" + profile.getId();
    }

    @GetMapping("/profils/{id}")
    public String profileDetail(@PathVariable UUID id, Model model) {
        var profile = profileService.getCurrentUserProfile(id);
        model.addAttribute("profile", profile);
        model.addAttribute("profiles", profileService.getCurrentUserDomainProfiles());
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("pageTitle", profile.getDisplayName());
        return "profile-detail";
    }

    @PostMapping("/profils/{id}")
    public String updateProfile(@PathVariable UUID id,
                                @RequestParam String displayName,
                                @RequestParam(required = false) String language,
                                @RequestParam(required = false) String maturityLevel,
                                @RequestParam(defaultValue = "false") boolean kidsProfile,
                                @RequestParam(defaultValue = "false") boolean autoplayNextEpisode,
                                @RequestParam(defaultValue = "false") boolean autoplayPreviews,
                                RedirectAttributes redirectAttributes) {
        profileService.updateCurrentUserProfile(id, displayName, language, maturityLevel, kidsProfile, autoplayNextEpisode, autoplayPreviews);
        redirectAttributes.addFlashAttribute("successMsg", "Profil mis a jour.");
        return "redirect:/profils/" + id;
    }

    @GetMapping("/mon-compte")
    public String myAccount(Model model) {
        model.addAttribute("pageTitle", "Mon compte");
        return "account";
    }

    @GetMapping("/parametres")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "Parametres");
        return "settings";
    }

    @GetMapping("/tendances")
    public String trending(@RequestParam(required = false) String genre, Model model) {
        model.addAttribute("titles", titleService.getFilteredTitleSummaries(null, genre, null));
        model.addAttribute("activeMenu", "trending");
        model.addAttribute("pageTitle", "Tendances");
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("currentPath", "/tendances");
        return "catalogue";
    }

    @GetMapping("/connexion")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/inscription")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/inscription")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String country,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (email == null || email.isBlank()) {
            model.addAttribute("error", "L'adresse email est obligatoire.");
            return "register";
        }
        if (password == null || password.length() < 8) {
            model.addAttribute("error", "Le mot de passe doit contenir au moins 8 caracteres.");
            return "register";
        }

        try {
            String normalizedEmail = email.trim().toLowerCase();
            String selectedCountry = (country == null || country.isBlank()) ? "FR" : country;
            authService.register(normalizedEmail, password, selectedCountry);
            redirectAttributes.addAttribute("registered", "true");
            return "redirect:/connexion";
        } catch (RuntimeException ex) {
            model.addAttribute("error", "Cet email est deja utilise.");
            return "register";
        }
    }

    @GetMapping("/lecture/{id}")
    public String playback(@PathVariable UUID id, Model model) {
        var title = titleService.getTitleDomainById(id);
        if (title == null) {
            return "redirect:/";
        }
        viewCountService.incrementViewCount(id);
        model.addAttribute("title", title);
        model.addAttribute("pageTitle", "Lecture : " + title.getOriginalTitle());
        return "player";
    }
}