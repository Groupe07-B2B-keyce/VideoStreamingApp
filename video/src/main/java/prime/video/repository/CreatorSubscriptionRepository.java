package prime.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.video.domain.CreatorProfile;
import prime.video.domain.CreatorSubscription;
import prime.video.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreatorSubscriptionRepository extends JpaRepository<CreatorSubscription, UUID> {
    Optional<CreatorSubscription> findBySubscriberAndCreator(User subscriber, CreatorProfile creator);
    boolean existsBySubscriberAndCreator(User subscriber, CreatorProfile creator);
    List<CreatorSubscription> findBySubscriber(User subscriber);
    int countByCreator(CreatorProfile creator);
}
