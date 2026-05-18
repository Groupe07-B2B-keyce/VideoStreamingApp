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

import java.util.List;

@Configuration
public class DataInitializer {

    private static final String SAMPLE_VIDEO_FLOWER = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4";
    private static final String SAMPLE_VIDEO_BIG_BUCK = "https://www.w3schools.com/html/mov_bbb.mp4";

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      TitleRepository titleRepository,
                                      GenreRepository genreRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@primevideo.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("admin@primevideo.com")
                        .password(passwordEncoder.encode("Admin123!"))
                        .country("FR")
                        .role("ROLE_SUPER_ADMIN")
                        .build());
            }

            Genre nollywood = ensureGenre(genreRepository, "Nollywood", "Films du Nigeria");
            Genre cameroun = ensureGenre(genreRepository, "Cinema Camerounais", "Productions locales 237");
            Genre mystique = ensureGenre(genreRepository, "Sorcellerie et Mystique", "Contes et mysteres africains");
            Genre musique = ensureGenre(genreRepository, "Afrobeat Clips", "Le meilleur de la musique africaine");
            Genre comedie = ensureGenre(genreRepository, "Comedie Africaine", "Humour et rires du continent");
            Genre drame = ensureGenre(genreRepository, "Drame Africain", "Histoires fortes du continent");
            Genre documentaire = ensureGenre(genreRepository, "Documentaire Africain", "Cultures, villes et portraits africains");
            Genre action = ensureGenre(genreRepository, "Action Africaine", "Films d'action du continent");

            seedTitle(titleRepository,
                    "Le Grimoire maudit", "Le Grimoire maudit", "MOVIE", "PG13", 2024,
                    "Un ancien grimoire decouvert dans un village du Cameroun seme la terreur. Un jeune etudiant doit affronter les forces occultes pour sauver sa communaute.",
                    "Certains secrets ne doivent jamais etre reveles.", 98, 7.8,
                    poster("grimoire"), backdrop("grimoire"), SAMPLE_VIDEO_FLOWER,
                    "Jean-Pierre Bekolo", "Alain Bomo Bomo, Eriq Ebouaney", List.of(mystique, cameroun));

            seedTitle(titleRepository,
                    "La Cite des Ombres", "La Cite des Ombres", "MOVIE", "TV_14", 2023,
                    "Dans une metropole africaine, une journaliste suit une piste dangereuse qui relie politique, musique et disparitions nocturnes.",
                    "La nuit garde les preuves.", 112, 7.4,
                    poster("cite-ombres"), backdrop("cite-ombres"), SAMPLE_VIDEO_BIG_BUCK,
                    "Aminata Diallo", "Fatou Ndiaye, Patrick N'Guessan", List.of(dramaSafe(drame), action));

            seedTitle(titleRepository,
                    "Rires de Douala", "Rires de Douala", "MOVIE", "PG", 2022,
                    "Un chauffeur de taxi inventif transforme chaque course en aventure pour sauver le commerce familial.",
                    "Chaque embouteillage cache une histoire.", 86, 6.9,
                    poster("rires-douala"), backdrop("rires-douala"), SAMPLE_VIDEO_FLOWER,
                    "Clarisse Mballa", "Junior Mbappe, Carine Fotso", List.of(comedie, cameroun));

            seedTitle(titleRepository,
                    "Les Tambours de Dakar", "Les Tambours de Dakar", "DOCUMENTARY", "G", 2021,
                    "Un voyage musical a travers les quartiers de Dakar, entre transmission, danse et scenes contemporaines.",
                    "Le rythme raconte la ville.", 74, 8.1,
                    poster("tambours-dakar"), backdrop("tambours-dakar"), SAMPLE_VIDEO_BIG_BUCK,
                    "Mamadou Sarr", "Collectif Dakar Percussions", List.of(documentaire, musique));

            seedTitle(titleRepository,
                    "Lagos Night Run", "Lagos Night Run", "MOVIE", "PG13", 2024,
                    "Un livreur doit traverser Lagos en une nuit pour remettre un colis qui peut innocenter son frere.",
                    "Une ville. Une nuit. Une chance.", 101, 7.2,
                    poster("lagos-night-run"), backdrop("lagos-night-run"), SAMPLE_VIDEO_FLOWER,
                    "Tunde Adebayo", "Chidi Okonkwo, Bisi Balogun", List.of(nollywood, action));

            seedTitle(titleRepository,
                    "Heritage 237", "Heritage 237", "DOCUMENTARY", "TV_G", 2020,
                    "Des jeunes createurs camerounais racontent comment ils reinventent la mode, la cuisine et la musique de leurs familles.",
                    "Le futur parle avec les anciens.", 68, 7.6,
                    poster("heritage-237"), backdrop("heritage-237"), SAMPLE_VIDEO_BIG_BUCK,
                    "Nora Kamga", "Createurs de Yaounde et Douala", List.of(documentaire, cameroun));

            seedTitle(titleRepository,
                    "La Route d'Abidjan", "La Route d'Abidjan", "MOVIE", "PG", 2022,
                    "Deux soeurs partent a Abidjan pour retrouver leur pere et decouvrent une famille plus vaste que leurs souvenirs.",
                    "Le voyage commence toujours par une question.", 95, 7.0,
                    poster("route-abidjan"), backdrop("route-abidjan"), SAMPLE_VIDEO_FLOWER,
                    "Mireille Kouame", "Aicha Kone, Serge Bamba", List.of(dramaSafe(drame), comedie));

            seedTitle(titleRepository,
                    "Kinshasa Beat", "Kinshasa Beat", "MOVIE", "TV_PG", 2023,
                    "Un producteur debutant mise tout sur un groupe de quartier pour gagner un concours musical national.",
                    "Quand la ville chante, personne ne reste assis.", 90, 7.3,
                    poster("kinshasa-beat"), backdrop("kinshasa-beat"), SAMPLE_VIDEO_BIG_BUCK,
                    "Didier Mbuyi", "Grace Mutombo, Cedric Lemba", List.of(musique, drame));
        };
    }

    private Genre ensureGenre(GenreRepository genreRepository, String name, String description) {
        return genreRepository.findByName(name).orElseGet(() -> genreRepository.save(
                Genre.builder()
                        .name(name)
                        .slug(slug(name))
                        .description(description)
                        .build()));
    }

    private Genre dramaSafe(Genre genre) {
        return genre;
    }

    private void seedTitle(TitleRepository titleRepository,
                           String originalTitle,
                           String localizedTitle,
                           String type,
                           String rating,
                           int releaseYear,
                           String synopsis,
                           String tagline,
                           int durationMinutes,
                           double imdbRating,
                           String posterUrl,
                           String backdropUrl,
                           String videoFileUrl,
                           String director,
                           String castMembers,
                           List<Genre> genres) {
        Title title = titleRepository.findByOriginalTitle(originalTitle).orElseGet(Title::new);
        title.setOriginalTitle(originalTitle);
        title.setLocalizedTitle(localizedTitle);
        title.setType(type);
        title.setStatus("PUBLISHED");
        title.setRating(rating);
        title.setReleaseYear(releaseYear);
        title.setSynopsis(synopsis);
        title.setTagline(tagline);
        title.setDurationMinutes(durationMinutes);
        title.setImdbRating(imdbRating);
        title.setPosterUrl(posterUrl);
        title.setBackdropUrl(backdropUrl);
        title.setVideoFileUrl(videoFileUrl);
        title.setTrailerUrl(videoFileUrl);
        title.setResolutionMax("FHD_1080P");
        title.setHasHDR(false);
        title.setHasDolbyVision(false);
        title.setHasDolbyAtmos(false);
        title.setAudioLanguages("fr,en");
        title.setSubtitleLanguages("fr,en");
        title.setAccessType("INCLUDED");
        title.setCurrency("EUR");
        title.setDirector(director);
        title.setCastMembers(castMembers);
        title.setGenres(genres);
        if (title.getTotalViews() == null) {
            title.setTotalViews(0);
        }
        if (title.getUniqueViewers() == null) {
            title.setUniqueViewers(0);
        }
        titleRepository.save(title);
    }

    private static String poster(String seed) {
        return "https://picsum.photos/seed/" + seed + "/480/720";
    }

    private static String backdrop(String seed) {
        return "https://picsum.photos/seed/" + seed + "-backdrop/1280/720";
    }

    private static String slug(String value) {
        return value.toLowerCase()
                .replace("'", "")
                .replace("&", "et")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}