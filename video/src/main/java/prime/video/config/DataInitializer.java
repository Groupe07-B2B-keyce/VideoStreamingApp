package prime.video.config;

import prime.video.domain.Genre;
import prime.video.domain.Title;
import prime.video.domain.User;
import prime.video.repository.GenreRepository;
import prime.video.repository.TitleRepository;
import prime.video.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, 
                                     TitleRepository titleRepository, 
                                     GenreRepository genreRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            // Initial User
            if (userRepository.findByEmail("admin@primevideo.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("admin@primevideo.com")
                        .password(passwordEncoder.encode("Admin123!"))
                        .country("FR")
                        .role("ROLE_SUPER_ADMIN")
                        .build());
            }

            // Catégories Africaines
            Genre nollywood = genreRepository.findByName("Nollywood").orElseGet(() -> 
                genreRepository.save(Genre.builder().name("Nollywood").description("Films du Nigeria").build()));
            Genre cameroun = genreRepository.findByName("Cinéma Camerounais").orElseGet(() -> 
                genreRepository.save(Genre.builder().name("Cinéma Camerounais").description("Productions locales 237").build()));
            Genre sorcellerie = genreRepository.findByName("Sorcellerie & Mystique").orElseGet(() -> 
                genreRepository.save(Genre.builder().name("Sorcellerie & Mystique").description("Contes et mystères africains").build()));
            Genre afrobeat = genreRepository.findByName("Afrobeat Clips").orElseGet(() -> 
                genreRepository.save(Genre.builder().name("Afrobeat Clips").description("Le meilleur de la musique africaine").build()));
            // Catégories supplémentaires
            genreRepository.findByName("Comédie Africaine").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Comédie Africaine").description("Humour et rires du continent").build()));
            genreRepository.findByName("Drame Africain").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Drame Africain").description("Histoires poignantes d'Afrique").build()));
            genreRepository.findByName("Contes & Légendes").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Contes & Légendes").description("Traditions orales revisitées").build()));
            genreRepository.findByName("Cinéma Congolais").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Cinéma Congolais").description("Productions RDC & Congo").build()));
            genreRepository.findByName("Cinéma Ivoirien").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Cinéma Ivoirien").description("Films de Côte d'Ivoire").build()));
            genreRepository.findByName("Cinéma Sénégalais").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Cinéma Sénégalais").description("Productions du Sénégal").build()));
            genreRepository.findByName("Cinéma Sud-Africain").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Cinéma Sud-Africain").description("Productions d'Afrique du Sud").build()));
            genreRepository.findByName("Action Africaine").orElseGet(() ->
                genreRepository.save(Genre.builder().name("Action Africaine").description("Films d'action du continent").build()));

            // Titre de démonstration avec tous les champs
            if (titleRepository.findAll().isEmpty()) {
                titleRepository.save(Title.builder()
                        .originalTitle("Le Grimoire maudit")
                        .localizedTitle("Le Grimoire maudit")
                        .type("MOVIE")
                        .status("PUBLISHED")
                        .rating("PG13")
                        .releaseYear(2024)
                        .synopsis("Un ancien grimoire découvert dans un village du Cameroun sème la terreur. Un jeune étudiant doit affronter les forces occultes pour sauver sa communauté.")
                        .tagline("Certains secrets ne doivent jamais être révélés.")
                        .durationMinutes(98)
                        .resolutionMax("FHD_1080P")
                        .hasHDR(false)
                        .hasDolbyAtmos(false)
                        .audioLanguages("fr,en")
                        .subtitleLanguages("fr,en,sw")
                        .accessType("INCLUDED")
                        .currency("XAF")
                        .director("Jean-Pierre Bekolo")
                        .castMembers("Alain Bomo Bomo:Le Sage, Eriq Ebouaney:L'Étudiant")
                        .genres(Arrays.asList(sorcellerie, cameroun))
                        .build());
            }
        };

    }
}
