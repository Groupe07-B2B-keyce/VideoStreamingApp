package prime.video.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "titles")
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    // ── Informations de base ────────────────────────────────────
    @Column(nullable = false)
    private String originalTitle;

    private String localizedTitle;

    @Column(nullable = false)
    private String type; // MOVIE, SERIES, DOCUMENTARY, MINI_SERIES, STAND_UP

    @Column(nullable = false)
    private String status; // DRAFT, PENDING_REVIEW, PUBLISHED, ARCHIVED, COMING_SOON

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private String tagline;

    @Column(nullable = false)
    private String rating; // G, PG, PG13, R, NC17, TV_MA, TV_14, TV_PG, TV_G

    @Column(nullable = false)
    private Integer releaseYear;

    // ── Médias (URLs) ───────────────────────────────────────────
    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;

    /** URL du fichier vidéo source (MP4, stockage local ou distant) */
    @Column(columnDefinition = "TEXT")
    private String videoFileUrl;

    // ── Données externes ────────────────────────────────────────
    private String imdbId;
    private Double imdbRating;

    // ── Détails techniques (TechnicalDetails du YAML) ───────────
    private String resolutionMax; // SD, HD_720P, FHD_1080P, UHD_4K
    private Integer durationMinutes;
    private Boolean hasHDR;
    private Boolean hasDolbyVision;
    private Boolean hasDolbyAtmos;

    /** Langues audio séparées par des virgules : "fr,en,sw" */
    private String audioLanguages;

    /** Sous-titres séparés par des virgules : "fr,en,ar" */
    private String subtitleLanguages;

    // ── Accès & Pricing (AccessInfo du YAML) ────────────────────
    private String accessType; // INCLUDED, RENT, BUY, CHANNEL_SUBSCRIPTION
    private Double rentalPrice;
    private Double purchasePrice;
    private String currency;

    // ── Casting simplifié ───────────────────────────────────────
    private String director;

    @Column(columnDefinition = "TEXT")
    private String castMembers; // "Acteur1:Rôle1, Acteur2:Rôle2"

    // ── Statistiques ────────────────────────────────────────────
    @Column(nullable = false)
    private Integer totalViews = 0;

    @Column(nullable = false)
    private Integer uniqueViewers = 0;

    // ── Séries ──────────────────────────────────────────────────
    private Integer totalSeasonsCount;

    // ── Relations ───────────────────────────────────────────────
    @ManyToMany
    @JoinTable(
        name = "title_genres",
        joinColumns = @JoinColumn(name = "title_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    public Title() {}

    // ── Builder ─────────────────────────────────────────────────
    public static TitleBuilder builder() {
        return new TitleBuilder();
    }

    // ── Getters & Setters ───────────────────────────────────────
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }
    public String getLocalizedTitle() { return localizedTitle; }
    public void setLocalizedTitle(String localizedTitle) { this.localizedTitle = localizedTitle; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getBackdropUrl() { return backdropUrl; }
    public void setBackdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    public String getVideoFileUrl() { return videoFileUrl; }
    public void setVideoFileUrl(String videoFileUrl) { this.videoFileUrl = videoFileUrl; }
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }
    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) { this.imdbRating = imdbRating; }
    public String getResolutionMax() { return resolutionMax; }
    public void setResolutionMax(String resolutionMax) { this.resolutionMax = resolutionMax; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public Boolean getHasHDR() { return hasHDR; }
    public void setHasHDR(Boolean hasHDR) { this.hasHDR = hasHDR; }
    public Boolean getHasDolbyVision() { return hasDolbyVision; }
    public void setHasDolbyVision(Boolean hasDolbyVision) { this.hasDolbyVision = hasDolbyVision; }
    public Boolean getHasDolbyAtmos() { return hasDolbyAtmos; }
    public void setHasDolbyAtmos(Boolean hasDolbyAtmos) { this.hasDolbyAtmos = hasDolbyAtmos; }
    public String getAudioLanguages() { return audioLanguages; }
    public void setAudioLanguages(String audioLanguages) { this.audioLanguages = audioLanguages; }
    public String getSubtitleLanguages() { return subtitleLanguages; }
    public void setSubtitleLanguages(String subtitleLanguages) { this.subtitleLanguages = subtitleLanguages; }
    public String getAccessType() { return accessType; }
    public void setAccessType(String accessType) { this.accessType = accessType; }
    public Double getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(Double rentalPrice) { this.rentalPrice = rentalPrice; }
    public Double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public String getCastMembers() { return castMembers; }
    public void setCastMembers(String castMembers) { this.castMembers = castMembers; }
    public Integer getTotalViews() { return totalViews; }
    public void setTotalViews(Integer totalViews) { this.totalViews = totalViews; }
    public Integer getUniqueViewers() { return uniqueViewers; }
    public void setUniqueViewers(Integer uniqueViewers) { this.uniqueViewers = uniqueViewers; }
    public Integer getTotalSeasonsCount() { return totalSeasonsCount; }
    public void setTotalSeasonsCount(Integer totalSeasonsCount) { this.totalSeasonsCount = totalSeasonsCount; }
    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    // ── Builder class ───────────────────────────────────────────
    public static class TitleBuilder {
        private UUID id;
        private String originalTitle, localizedTitle, type, status, synopsis, tagline, rating;
        private Integer releaseYear;
        private String posterUrl, backdropUrl, trailerUrl, videoFileUrl;
        private String imdbId;
        private Double imdbRating;
        private String resolutionMax;
        private Integer durationMinutes;
        private Boolean hasHDR, hasDolbyVision, hasDolbyAtmos;
        private String audioLanguages, subtitleLanguages;
        private String accessType, currency;
        private Double rentalPrice, purchasePrice;
        private String director, castMembers;
        private Integer totalViews = 0, uniqueViewers = 0, totalSeasonsCount;
        private List<Genre> genres;

        public TitleBuilder id(UUID id) { this.id = id; return this; }
        public TitleBuilder originalTitle(String v) { this.originalTitle = v; return this; }
        public TitleBuilder localizedTitle(String v) { this.localizedTitle = v; return this; }
        public TitleBuilder type(String v) { this.type = v; return this; }
        public TitleBuilder status(String v) { this.status = v; return this; }
        public TitleBuilder synopsis(String v) { this.synopsis = v; return this; }
        public TitleBuilder tagline(String v) { this.tagline = v; return this; }
        public TitleBuilder rating(String v) { this.rating = v; return this; }
        public TitleBuilder releaseYear(Integer v) { this.releaseYear = v; return this; }
        public TitleBuilder posterUrl(String v) { this.posterUrl = v; return this; }
        public TitleBuilder backdropUrl(String v) { this.backdropUrl = v; return this; }
        public TitleBuilder trailerUrl(String v) { this.trailerUrl = v; return this; }
        public TitleBuilder videoFileUrl(String v) { this.videoFileUrl = v; return this; }
        public TitleBuilder imdbId(String v) { this.imdbId = v; return this; }
        public TitleBuilder imdbRating(Double v) { this.imdbRating = v; return this; }
        public TitleBuilder resolutionMax(String v) { this.resolutionMax = v; return this; }
        public TitleBuilder durationMinutes(Integer v) { this.durationMinutes = v; return this; }
        public TitleBuilder hasHDR(Boolean v) { this.hasHDR = v; return this; }
        public TitleBuilder hasDolbyVision(Boolean v) { this.hasDolbyVision = v; return this; }
        public TitleBuilder hasDolbyAtmos(Boolean v) { this.hasDolbyAtmos = v; return this; }
        public TitleBuilder audioLanguages(String v) { this.audioLanguages = v; return this; }
        public TitleBuilder subtitleLanguages(String v) { this.subtitleLanguages = v; return this; }
        public TitleBuilder accessType(String v) { this.accessType = v; return this; }
        public TitleBuilder rentalPrice(Double v) { this.rentalPrice = v; return this; }
        public TitleBuilder purchasePrice(Double v) { this.purchasePrice = v; return this; }
        public TitleBuilder currency(String v) { this.currency = v; return this; }
        public TitleBuilder director(String v) { this.director = v; return this; }
        public TitleBuilder castMembers(String v) { this.castMembers = v; return this; }
        public TitleBuilder totalViews(Integer v) { this.totalViews = v; return this; }
        public TitleBuilder uniqueViewers(Integer v) { this.uniqueViewers = v; return this; }
        public TitleBuilder totalSeasonsCount(Integer v) { this.totalSeasonsCount = v; return this; }
        public TitleBuilder genres(List<Genre> v) { this.genres = v; return this; }

        public Title build() {
            Title t = new Title();
            t.id = id; t.originalTitle = originalTitle; t.localizedTitle = localizedTitle;
            t.type = type; t.status = status; t.synopsis = synopsis; t.tagline = tagline;
            t.rating = rating; t.releaseYear = releaseYear;
            t.posterUrl = posterUrl; t.backdropUrl = backdropUrl; t.trailerUrl = trailerUrl;
            t.videoFileUrl = videoFileUrl;
            t.imdbId = imdbId; t.imdbRating = imdbRating;
            t.resolutionMax = resolutionMax; t.durationMinutes = durationMinutes;
            t.hasHDR = hasHDR; t.hasDolbyVision = hasDolbyVision; t.hasDolbyAtmos = hasDolbyAtmos;
            t.audioLanguages = audioLanguages; t.subtitleLanguages = subtitleLanguages;
            t.accessType = accessType; t.rentalPrice = rentalPrice; t.purchasePrice = purchasePrice;
            t.currency = currency;
            t.director = director; t.castMembers = castMembers;
            t.totalViews = totalViews; t.uniqueViewers = uniqueViewers;
            t.totalSeasonsCount = totalSeasonsCount;
            t.genres = genres;
            return t;
        }
    }
}
