package prime.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.video.domain.Collection;
import java.util.UUID;

public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    java.util.Optional<Collection> findBySlug(String slug);
}
