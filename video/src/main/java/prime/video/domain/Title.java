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

    @Column(nullable = false)
    private String originalTitle;

    private String localizedTitle;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private String tagline;

    @Column(nullable = false)
    private String rating;

    @Column(nullable = false)
    private Integer releaseYear;

    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;

    private String imdbId;
    private Double imdbRating;

    @ManyToMany
    @JoinTable(
        name = "title_genres",
        joinColumns = @JoinColumn(name = "title_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    public Title() {}

    public Title(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt, String originalTitle, String localizedTitle, String type, String status, String synopsis, String tagline, String rating, Integer releaseYear, String posterUrl, String backdropUrl, String trailerUrl, String imdbId, Double imdbRating, List<Genre> genres) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.originalTitle = originalTitle;
        this.localizedTitle = localizedTitle;
        this.type = type;
        this.status = status;
        this.synopsis = synopsis;
        this.tagline = tagline;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.posterUrl = posterUrl;
        this.backdropUrl = backdropUrl;
        this.trailerUrl = trailerUrl;
        this.imdbId = imdbId;
        this.imdbRating = imdbRating;
        this.genres = genres;
    }

    public static TitleBuilder builder() {
        return new TitleBuilder();
    }

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
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }
    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) { this.imdbRating = imdbRating; }
    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public static class TitleBuilder {
        private UUID id;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private String originalTitle;
        private String localizedTitle;
        private String type;
        private String status;
        private String synopsis;
        private String tagline;
        private String rating;
        private Integer releaseYear;
        private String posterUrl;
        private String backdropUrl;
        private String trailerUrl;
        private String imdbId;
        private Double imdbRating;
        private List<Genre> genres;

        public TitleBuilder id(UUID id) { this.id = id; return this; }
        public TitleBuilder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TitleBuilder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public TitleBuilder originalTitle(String originalTitle) { this.originalTitle = originalTitle; return this; }
        public TitleBuilder localizedTitle(String localizedTitle) { this.localizedTitle = localizedTitle; return this; }
        public TitleBuilder type(String type) { this.type = type; return this; }
        public TitleBuilder status(String status) { this.status = status; return this; }
        public TitleBuilder synopsis(String synopsis) { this.synopsis = synopsis; return this; }
        public TitleBuilder tagline(String tagline) { this.tagline = tagline; return this; }
        public TitleBuilder rating(String rating) { this.rating = rating; return this; }
        public TitleBuilder releaseYear(Integer releaseYear) { this.releaseYear = releaseYear; return this; }
        public TitleBuilder posterUrl(String posterUrl) { this.posterUrl = posterUrl; return this; }
        public TitleBuilder backdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; return this; }
        public TitleBuilder trailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; return this; }
        public TitleBuilder imdbId(String imdbId) { this.imdbId = imdbId; return this; }
        public TitleBuilder imdbRating(Double imdbRating) { this.imdbRating = imdbRating; return this; }
        public TitleBuilder genres(List<Genre> genres) { this.genres = genres; return this; }
        public Title build() { return new Title(id, createdAt, updatedAt, originalTitle, localizedTitle, type, status, synopsis, tagline, rating, releaseYear, posterUrl, backdropUrl, trailerUrl, imdbId, imdbRating, genres); }
    }
}
