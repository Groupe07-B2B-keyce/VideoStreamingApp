package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.ProfilesApiDelegate;
import prime.video.model.CreateProfileRequest;
import prime.video.model.Profile;
import prime.video.model.ProfileSummary;
import prime.video.model.UpdateProfileRequest;
import prime.video.service.ProfileService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfilesApiDelegateImpl implements ProfilesApiDelegate {

    private final ProfileService profileService;

    public ProfilesApiDelegateImpl(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public ResponseEntity<List<ProfileSummary>> listProfiles() {
        List<ProfileSummary> summaries = profileService.getCurrentUserProfiles().stream()
                .map(p -> {
                    ProfileSummary s = new ProfileSummary();
                    s.setId(p.getId());
                    s.setDisplayName(p.getDisplayName());
                    s.setIsKidsProfile(p.getIsKidsProfile());
                    s.setAvatarUrl(p.getAvatarUrl());
                    if (p.getMaturityLevel() != null) {
                        s.setMaturityLevel(p.getMaturityLevel().getValue());
                    }
                    return s;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Profile> createProfile(CreateProfileRequest createProfileRequest) {
        Profile profile = new Profile();
        profile.setDisplayName(createProfileRequest.getDisplayName());
        profile.setIsKidsProfile(createProfileRequest.getIsKidsProfile());
        
        Profile created = profileService.createProfile(profile);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Profile> getProfileById(UUID profileId) {
        return profileService.getCurrentUserProfiles().stream()
                .filter(p -> p.getId().equals(profileId))
                .findFirst()
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> deleteProfile(UUID profileId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Profile> updateProfile(UUID profileId, UpdateProfileRequest updateProfileRequest) {
        return new ResponseEntity<>(new Profile(), HttpStatus.OK);
    }
}
