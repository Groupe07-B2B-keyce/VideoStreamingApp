package prime.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import prime.video.service.TitleService;

@Controller
public class UIController {

    private final TitleService titleService;

    public UIController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("titles", titleService.getAllTitleSummaries());
        model.addAttribute("activeMenu", "home");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
