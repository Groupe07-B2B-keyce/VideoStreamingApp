package prime.video.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    private UUID id;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String displayName;
    private String avatarUrl;
    private Boolean isKidsProfile;
    private String language;
    private String maturityLevel;
    private Boolean parentalPinEnabled;
    private Boolean autoplayNextEpisode;
    private Boolean autoplayPreviews;
    private String defaultAudioLanguage;
    private String defaultSubtitleLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Profile() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Boolean getIsKidsProfile() { return isKidsProfile; }
    public void setIsKidsProfile(Boolean isKidsProfile) { this.isKidsProfile = isKidsProfile; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getMaturityLevel() { return maturityLevel; }
    public void setMaturityLevel(String maturityLevel) { this.maturityLevel = maturityLevel; }

    public Boolean getParentalPinEnabled() { return parentalPinEnabled; }
    public void setParentalPinEnabled(Boolean parentalPinEnabled) { this.parentalPinEnabled = parentalPinEnabled; }

    public Boolean getAutoplayNextEpisode() { return autoplayNextEpisode; }
    public void setAutoplayNextEpisode(Boolean autoplayNextEpisode) { this.autoplayNextEpisode = autoplayNextEpisode; }

    public Boolean getAutoplayPreviews() { return autoplayPreviews; }
    public void setAutoplayPreviews(Boolean autoplayPreviews) { this.autoplayPreviews = autoplayPreviews; }

    public String getDefaultAudioLanguage() { return defaultAudioLanguage; }
    public void setDefaultAudioLanguage(String defaultAudioLanguage) { this.defaultAudioLanguage = defaultAudioLanguage; }

    public String getDefaultSubtitleLanguage() { return defaultSubtitleLanguage; }
    public void setDefaultSubtitleLanguage(String defaultSubtitleLanguage) { this.defaultSubtitleLanguage = defaultSubtitleLanguage; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
