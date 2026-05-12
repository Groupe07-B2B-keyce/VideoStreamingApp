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

            // Initial Genres
            Genre action = genreRepository.findByName("Action").orElseGet(() -> 
                genreRepository.save(Genre.builder().name("Action").description("Action movies").build()));
            Genre drama = genreRepository.findByName("Drama").orElseGet(() -> 
                genreRepository.save(Genre.builder().name("Drama").description("Drama movies").build()));

            // Initial Title
            if (titleRepository.findAll().isEmpty()) {
                titleRepository.save(Title.builder()
                        .originalTitle("Oppenheimer")
                        .localizedTitle("Oppenheimer")
                        .type("MOVIE")
                        .status("PUBLISHED")
                        .rating("PG13")
                        .releaseYear(2023)
                        .synopsis("L'histoire de J. Robert Oppenheimer et du développement de la bombe atomique.")
                        .genres(Arrays.asList(action, drama))
                        .build());
            }
        };
    }
}
