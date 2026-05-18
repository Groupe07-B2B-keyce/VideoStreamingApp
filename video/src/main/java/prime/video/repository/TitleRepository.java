package prime.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.video.domain.Title;

import java.util.Optional;
import java.util.UUID;

public interface TitleRepository extends JpaRepository<Title, UUID> {
    Optional<Title> findByOriginalTitle(String originalTitle);
}