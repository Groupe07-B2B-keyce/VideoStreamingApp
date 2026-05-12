package prime.video.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import prime.video.domain.Profile;
import prime.video.domain.User;
import prime.video.repository.ProfileRepository;
import prime.video.repository.UserRepository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public List<prime.video.model.Profile> getCurrentUserProfiles() {
        User user = getCurrentUser();
        return profileRepository.findByUser(user).stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    public prime.video.model.Profile createProfile(prime.video.model.Profile model) {
        User user = getCurrentUser();
        Profile domain = new Profile();
        domain.setId(UUID.randomUUID());
        domain.setCreatedAt(OffsetDateTime.now());
        domain.setUpdatedAt(OffsetDateTime.now());
        domain.setDisplayName(model.getDisplayName());
        domain.setIsKidsProfile(model.getIsKidsProfile());
        domain.setUser(user);
        
        Profile saved = profileRepository.save(domain);
        return mapToModel(saved);
    }

    public prime.video.model.Profile mapToModel(Profile domain) {
        prime.video.model.Profile model = new prime.video.model.Profile();
        model.setId(domain.getId());
        model.setCreatedAt(domain.getCreatedAt());
        model.setUpdatedAt(domain.getUpdatedAt());
        model.setDisplayName(domain.getDisplayName());
        model.setIsKidsProfile(domain.getIsKidsProfile());
        model.setLanguage(domain.getLanguage());
        if (domain.getAvatarUrl() != null) {
            model.setAvatarUrl(URI.create(domain.getAvatarUrl()));
        }
        if (domain.getMaturityLevel() != null) {
            model.setMaturityLevel(prime.video.model.Profile.MaturityLevelEnum.fromValue(domain.getMaturityLevel()));
        }
        model.setParentalPinEnabled(domain.getParentalPinEnabled());
        model.setAutoplayNextEpisode(domain.getAutoplayNextEpisode());
        model.setAutoplayPreviews(domain.getAutoplayPreviews());
        model.setDefaultAudioLanguage(domain.getDefaultAudioLanguage());
        model.setDefaultSubtitleLanguage(domain.getDefaultSubtitleLanguage());
        return model;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
