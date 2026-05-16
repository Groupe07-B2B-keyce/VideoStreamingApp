package prime.video.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security — Configuration unifiée Keyce Transmission
 *
 * DEUX comportements selon le chemin :
 *  ① /api/v1/**  → STATELESS + JWT (pour Postman, réponse JSON)
 *  ② /**         → Session + Form-Login (pour le navigateur, Thymeleaf)
 *
 * Séparation via deux SecurityFilterChain avec @Order différent.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    /* ──────────────────────────────────────────────────────────
     *  Chain 1 — API REST : /api/v1/**
     *  Stateless, JWT, réponses JSON (pas de redirect HTML)
     * ────────────────────────────────────────────────────────── */
    @Bean
    @org.springframework.core.annotation.Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/api/v1/catalog/**",
                    "/api/v1/titles/**",
                    "/api/v1/genres/**",
                    "/api/v1/recommendations/**"
                ).permitAll()
                // Tous les autres /api/** nécessitent un token JWT
                .anyRequest().authenticated()
            )
            // Si pas de token → 401 JSON (pas de redirect)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setContentType("application/problem+json");
                    res.setStatus(401);
                    res.getWriter().write(
                        "{\"type\":\"https://keyce.transmission/errors/unauthorized\"," +
                        "\"title\":\"Non authentifié\"," +
                        "\"status\":401," +
                        "\"detail\":\"Un token JWT Bearer valide est requis.\"," +
                        "\"instance\":\"" + req.getRequestURI() + "\"}"
                    );
                })
                .accessDeniedHandler((req, res, e) -> {
                    res.setContentType("application/problem+json");
                    res.setStatus(403);
                    res.getWriter().write(
                        "{\"type\":\"https://keyce.transmission/errors/forbidden\"," +
                        "\"title\":\"Accès interdit\"," +
                        "\"status\":403," +
                        "\"detail\":\"Vous n'avez pas les permissions nécessaires.\"," +
                        "\"instance\":\"" + req.getRequestURI() + "\"}"
                    );
                })
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    /* ──────────────────────────────────────────────────────────
     *  Chain 2 — UI Thymeleaf : /**
     *  Session-based, Form-Login, redirection friendly
     * ────────────────────────────────────────────────────────── */
    @Bean
    @org.springframework.core.annotation.Order(2)
    public SecurityFilterChain uiFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF activé pour l'UI (protection forms)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )
            .authorizeHttpRequests(auth -> auth
                // Pages publiques
                .requestMatchers(
                    "/", "/films", "/series", "/genres", "/tendances",
                    "/connexion", "/inscription", "/deconnexion",
                    "/recherche", "/titres/**",
                    "/css/**", "/js/**", "/images/**", "/favicon.ico",
                    "/error", "/acces-refuse"
                ).permitAll()
                // Pages Admin protégées (UI)
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                // Pages protégées Utilisateur (UI)
                .requestMatchers(
                    "/profils/**", "/mon-compte/**",
                    "/ma-liste/**", "/historique/**",
                    "/parametres/**", "/lecture/**"
                ).authenticated()
                .anyRequest().permitAll()
            )
            // Form Login UI
            .formLogin(form -> form
                .loginPage("/connexion")
                .loginProcessingUrl("/connexion")
                .successHandler((req, res, auth) -> {
                    boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));
                    if (isAdmin) {
                        res.sendRedirect("/admin/dashboard");
                    } else {
                        res.sendRedirect("/");
                    }
                })
                .failureUrl("/connexion?error=true")
                .permitAll()
            )
            // Logout
            .logout(logout -> logout
                .logoutUrl("/deconnexion")
                .logoutSuccessUrl("/connexion?logout=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Accès refusé → page friendly (pas de 403 brut)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) ->
                    res.sendRedirect("/acces-refuse?redirect=" + req.getRequestURI())
                )
                .accessDeniedHandler((req, res, e) ->
                    res.sendRedirect("/acces-refuse")
                )
            );

        return http.build();
    }
}
