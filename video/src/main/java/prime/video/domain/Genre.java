package prime.video.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private String slug;

    private String description;

    public Genre() {}

    public Genre(UUID id, String name, String slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public static GenreBuilder builder() {
        return new GenreBuilder();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static class GenreBuilder {
        private UUID id;
        private String name;
        private String slug;
        private String description;

        public GenreBuilder id(UUID id) { this.id = id; return this; }
        public GenreBuilder name(String name) { this.name = name; return this; }
        public GenreBuilder slug(String slug) { this.slug = slug; return this; }
        public GenreBuilder description(String description) { this.description = description; return this; }
        public Genre build() { return new Genre(id, name, slug, description); }
    }
}
