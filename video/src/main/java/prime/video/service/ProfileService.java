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
        return getCurrentUserDomainProfiles().stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    public List<Profile> getCurrentUserDomainProfiles() {
        User user = getCurrentUser();
        List<Profile> profiles = profileRepository.findByUser(user);
        if (profiles.isEmpty()) {
            profiles = List.of(createDefaultProfile(user));
        }
        return profiles;
    }

    public Profile getCurrentUserProfile(UUID profileId) {
        return getCurrentUserDomainProfiles().stream()
                .filter(profile -> profile.getId().equals(profileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
    }

    public Profile createProfile(String displayName, boolean kidsProfile) {
        User user = getCurrentUser();
        Profile profile = new Profile();
        applyDefaults(profile, user);
        profile.setDisplayName(displayName == null || displayName.isBlank() ? "Nouveau profil" : displayName.trim());
        profile.setIsKidsProfile(kidsProfile);
        profile.setMaturityLevel(kidsProfile ? "KIDS" : "ADULT");
        return profileRepository.save(profile);
    }

    public prime.video.model.Profile createProfile(prime.video.model.Profile model) {
        Profile saved = createProfile(model.getDisplayName(), Boolean.TRUE.equals(model.getIsKidsProfile()));
        return mapToModel(saved);
    }

    public Profile updateCurrentUserProfile(UUID profileId,
                                            String displayName,
                                            String language,
                                            String maturityLevel,
                                            boolean kidsProfile,
                                            boolean autoplayNextEpisode,
                                            boolean autoplayPreviews) {
        Profile profile = getCurrentUserProfile(profileId);
        profile.setDisplayName(displayName == null || displayName.isBlank() ? profile.getDisplayName() : displayName.trim());
        profile.setLanguage(language == null || language.isBlank() ? "fr" : language);
        profile.setMaturityLevel(maturityLevel == null || maturityLevel.isBlank() ? (kidsProfile ? "KIDS" : "ADULT") : maturityLevel);
        profile.setIsKidsProfile(kidsProfile);
        profile.setAutoplayNextEpisode(autoplayNextEpisode);
        profile.setAutoplayPreviews(autoplayPreviews);
        profile.setUpdatedAt(OffsetDateTime.now());
        return profileRepository.save(profile);
    }

    public prime.video.model.Profile mapToModel(Profile domain) {
        prime.video.model.Profile model = new prime.video.model.Profile();
        model.setId(domain.getId());
        model.setCreatedAt(domain.getCreatedAt());
        model.setUpdatedAt(domain.getUpdatedAt());
        model.setDisplayName(domain.getDisplayName());
        model.setIsKidsProfile(domain.getIsKidsProfile());
        model.setLanguage(domain.getLanguage());
        if (domain.getAvatarUrl() != null && !domain.getAvatarUrl().isBlank()) {
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

    private Profile createDefaultProfile(User user) {
        Profile profile = new Profile();
        applyDefaults(profile, user);
        String email = user.getEmail() == null ? "Profil" : user.getEmail();
        String name = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        profile.setDisplayName(name.isBlank() ? "Mon profil" : name);
        return profileRepository.save(profile);
    }

    private void applyDefaults(Profile profile, User user) {
        profile.setId(UUID.randomUUID());
        profile.setCreatedAt(OffsetDateTime.now());
        profile.setUpdatedAt(OffsetDateTime.now());
        profile.setIsKidsProfile(false);
        profile.setLanguage("fr");
        profile.setMaturityLevel("ADULT");
        profile.setParentalPinEnabled(false);
        profile.setAutoplayNextEpisode(true);
        profile.setAutoplayPreviews(true);
        profile.setDefaultAudioLanguage("fr");
        profile.setDefaultSubtitleLanguage("fr");
        profile.setUser(user);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));
    }
}