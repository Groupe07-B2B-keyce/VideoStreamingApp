package prime.video.service;

import prime.video.domain.Genre;
import prime.video.domain.Title;
import prime.video.repository.TitleRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public List<prime.video.model.TitleSummary> getAllTitleSummaries() {
        return getFilteredTitleSummaries(null, null, null);
    }

    public List<prime.video.model.TitleSummary> getFilteredTitleSummaries(String type, String genre, String query) {
        Stream<Title> stream = titleRepository.findAll().stream();

        if (type != null && !type.isBlank()) {
            String wantedType = normalize(type);
            stream = stream.filter(title -> normalize(title.getType()).equals(wantedType));
        }
        if (genre != null && !genre.isBlank()) {
            String wantedGenre = normalize(genre);
            stream = stream.filter(title -> title.getGenres() != null && title.getGenres().stream()
                    .anyMatch(g -> normalize(g.getSlug()).equals(wantedGenre)
                            || normalize(g.getName()).equals(wantedGenre)
                            || normalize(g.getName()).contains(wantedGenre)));
        }
        if (query != null && !query.isBlank()) {
            String wantedQuery = normalize(query);
            stream = stream.filter(title -> normalize(title.getOriginalTitle()).contains(wantedQuery)
                    || normalize(title.getLocalizedTitle()).contains(wantedQuery)
                    || normalize(title.getSynopsis()).contains(wantedQuery)
                    || normalize(title.getDirector()).contains(wantedQuery)
                    || normalize(title.getCastMembers()).contains(wantedQuery));
        }

        return stream.sorted(Comparator.comparing(Title::getReleaseYear, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    public Title getFeaturedTitle() {
        return titleRepository.findAll().stream()
                .filter(title -> title.getStatus() == null || "PUBLISHED".equalsIgnoreCase(title.getStatus()))
                .max(Comparator.comparing(Title::getImdbRating, Comparator.nullsLast(Double::compareTo)))
                .orElse(null);
    }

    public prime.video.model.Title getTitleModelById(UUID id) {
        return titleRepository.findById(id)
                .map(this::mapToModel)
                .orElse(null);
    }

    public Title getTitleDomainById(UUID id) {
        return titleRepository.findById(id).orElse(null);
    }

    private prime.video.model.TitleSummary mapToSummary(Title domain) {
        prime.video.model.TitleSummary summary = new prime.video.model.TitleSummary();
        summary.setId(domain.getId());
        summary.setOriginalTitle(domain.getOriginalTitle());
        summary.setReleaseYear(domain.getReleaseYear());
        summary.setImdbRating(domain.getImdbRating());

        if (domain.getPosterUrl() != null && !domain.getPosterUrl().isBlank()) {
            summary.setPosterUrl(URI.create(domain.getPosterUrl()));
        }
        if (domain.getType() != null) {
            summary.setType(prime.video.model.TitleSummary.TypeEnum.fromValue(domain.getType()));
        }
        if (domain.getRating() != null) {
            summary.setRating(domain.getRating());
        }

        return summary;
    }

    private prime.video.model.Title mapToModel(Title domain) {
        prime.video.model.Title model = new prime.video.model.Title();
        model.setId(domain.getId());
        model.setOriginalTitle(domain.getOriginalTitle());
        model.setLocalizedTitle(domain.getLocalizedTitle());
        model.setSynopsis(domain.getSynopsis());
        model.setTagline(domain.getTagline());
        model.setReleaseYear(domain.getReleaseYear());
        model.setImdbId(domain.getImdbId());
        model.setImdbRating(domain.getImdbRating());

        if (domain.getType() != null) {
            model.setType(prime.video.model.Title.TypeEnum.fromValue(domain.getType()));
        }
        if (domain.getStatus() != null) {
            model.setStatus(prime.video.model.Title.StatusEnum.fromValue(domain.getStatus()));
        }
        if (domain.getRating() != null) {
            model.setRating(prime.video.model.Title.RatingEnum.fromValue(domain.getRating()));
        }

        return model;
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
        return normalized.replace("_", "-").replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}