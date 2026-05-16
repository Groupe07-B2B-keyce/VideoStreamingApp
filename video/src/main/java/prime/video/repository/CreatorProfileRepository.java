package prime.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.video.domain.CreatorProfile;
import prime.video.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreatorProfileRepository extends JpaRepository<CreatorProfile, UUID> {
    Optional<CreatorProfile> findByUser(User user);
    boolean existsByUser(User user);
    List<CreatorProfile> findAllByOrderBySubscriberCountDesc();
}
