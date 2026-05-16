package prime.video.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Profil Créateur : un utilisateur CREATOR peut poster ses propres contenus.
 */
@Entity
@Table(name = "creator_profiles")
public class CreatorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatarUrl;
    private String bannerUrl;
    private String country;
    private String socialLink;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(nullable = false)
    private Integer subscriberCount = 0;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    public CreatorProfile() {}

    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getSocialLink() { return socialLink; }
    public void setSocialLink(String socialLink) { this.socialLink = socialLink; }
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    public Integer getSubscriberCount() { return subscriberCount; }
    public void setSubscriberCount(Integer subscriberCount) { this.subscriberCount = subscriberCount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
