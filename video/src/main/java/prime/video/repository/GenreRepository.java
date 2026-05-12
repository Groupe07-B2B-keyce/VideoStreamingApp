package prime.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.video.domain.Genre;

import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
    Optional<Genre> findByName(String name);
}
