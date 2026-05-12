package prime.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.video.domain.Profile;
import prime.video.domain.User;
import java.util.List;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    List<Profile> findByUser(User user);
}
