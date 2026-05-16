package prime.video.service;

import prime.video.domain.Genre;
import prime.video.domain.Title;
import prime.video.repository.GenreRepository;
import prime.video.repository.TitleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminTitleService {

    private final TitleRepository titleRepository;
    private final GenreRepository genreRepository;

    public AdminTitleService(TitleRepository titleRepository, GenreRepository genreRepository) {
        this.titleRepository = titleRepository;
        this.genreRepository = genreRepository;
    }

    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    /**
     * Création complète d'un titre conforme au SubmitTitleRequest du YAML.
     */
    @Transactional
    public Title createTitle(
            // Infos de base
            String originalTitle, String localizedTitle, String type,
            String rating, Integer releaseYear, String synopsis, String tagline,
            // Médias
            String posterUrl, String backdropUrl, String trailerUrl, String videoFileUrl,
            // Données externes
            String imdbId, Double imdbRating,
            // Détails techniques
            String resolutionMax, Integer durationMinutes,
            Boolean hasHDR, Boolean hasDolbyVision, Boolean hasDolbyAtmos,
            String audioLanguages, String subtitleLanguages,
            // Accès & Pricing
            String accessType, Double rentalPrice, Double purchasePrice, String currency,
            // Casting
            String director, String castMembers,
            // Séries
            Integer totalSeasonsCount,
            // Genres
            List<UUID> genreIds) {

        List<Genre> genres = genreIds != null
            ? genreIds.stream()
                .map(id -> genreRepository.findById(id).orElse(null))
                .filter(g -> g != null)
                .collect(Collectors.toList())
            : List.of();

        Title title = Title.builder()
            .originalTitle(originalTitle)
            .localizedTitle(localizedTitle != null && !localizedTitle.isBlank() ? localizedTitle : originalTitle)
            .type(type)
            .status("PUBLISHED")
            .rating(rating)
            .releaseYear(releaseYear)
            .synopsis(synopsis)
            .tagline(tagline)
            // Médias
            .posterUrl(posterUrl)
            .backdropUrl(backdropUrl)
            .trailerUrl(trailerUrl)
            .videoFileUrl(videoFileUrl)
            // Données externes
            .imdbId(imdbId)
            .imdbRating(imdbRating)
            // Détails techniques
            .resolutionMax(resolutionMax != null ? resolutionMax : "FHD_1080P")
            .durationMinutes(durationMinutes)
            .hasHDR(hasHDR != null ? hasHDR : false)
            .hasDolbyVision(hasDolbyVision != null ? hasDolbyVision : false)
            .hasDolbyAtmos(hasDolbyAtmos != null ? hasDolbyAtmos : false)
            .audioLanguages(audioLanguages)
            .subtitleLanguages(subtitleLanguages)
            // Accès & Pricing
            .accessType(accessType != null ? accessType : "INCLUDED")
            .rentalPrice(rentalPrice)
            .purchasePrice(purchasePrice)
            .currency(currency != null ? currency : "XAF")
            // Casting
            .director(director)
            .castMembers(castMembers)
            // Séries
            .totalSeasonsCount(totalSeasonsCount)
            // Genres
            .genres(genres)
            .build();

        return titleRepository.save(title);
    }

    @Transactional
    public void deleteTitle(UUID id) {
        titleRepository.deleteById(id);
    }
}
