package prime.video.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prime.video.domain.CreatorProfile;
import prime.video.domain.CreatorSubscription;
import prime.video.domain.User;
import prime.video.repository.CreatorProfileRepository;
import prime.video.repository.CreatorSubscriptionRepository;
import prime.video.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CreatorService {

    private final CreatorProfileRepository creatorProfileRepository;
    private final CreatorSubscriptionRepository creatorSubscriptionRepository;
    private final UserRepository userRepository;

    public CreatorService(CreatorProfileRepository creatorProfileRepository,
                          CreatorSubscriptionRepository creatorSubscriptionRepository,
                          UserRepository userRepository) {
        this.creatorProfileRepository = creatorProfileRepository;
        this.creatorSubscriptionRepository = creatorSubscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreatorProfile upgradeToCreator(UUID userId, String displayName, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (creatorProfileRepository.existsByUser(user)) {
            throw new RuntimeException("L'utilisateur est déjà un créateur");
        }

        // Mettre à jour le rôle de l'utilisateur
        user.setRole("ROLE_CREATOR");
        userRepository.save(user);

        // Créer le profil créateur
        CreatorProfile profile = new CreatorProfile();
        profile.setUser(user);
        profile.setDisplayName(displayName);
        profile.setBio(bio);
        profile.setSubscriberCount(0);
        profile.setVerified(false);

        return creatorProfileRepository.save(profile);
    }

    @Transactional
    public void subscribe(UUID subscriberId, UUID creatorProfileId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Abonné non trouvé"));
        CreatorProfile creator = creatorProfileRepository.findById(creatorProfileId)
                .orElseThrow(() -> new RuntimeException("Créateur non trouvé"));

        if (creatorSubscriptionRepository.existsBySubscriberAndCreator(subscriber, creator)) {
            return; // Déjà abonné
        }

        creatorSubscriptionRepository.save(new CreatorSubscription(subscriber, creator));
        
        // Incrémenter le compteur
        creator.setSubscriberCount(creator.getSubscriberCount() + 1);
        creatorProfileRepository.save(creator);
    }

    public List<CreatorProfile> getTopCreators() {
        return creatorProfileRepository.findAllByOrderBySubscriberCountDesc();
    }

    public CreatorProfile getProfileByUserId(UUID userId) {
        return userRepository.findById(userId)
                .flatMap(creatorProfileRepository::findByUser)
                .orElse(null);
    }
}
