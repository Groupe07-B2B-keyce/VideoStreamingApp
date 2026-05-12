package prime.video.service;

import prime.video.domain.Title;
import prime.video.repository.TitleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public List<prime.video.model.TitleSummary> getAllTitleSummaries() {
        return titleRepository.findAll().stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    public prime.video.model.Title getTitleModelById(java.util.UUID id) {
        return titleRepository.findById(id)
                .map(this::mapToModel)
                .orElse(null);
    }

    private prime.video.model.TitleSummary mapToSummary(Title domain) {
        prime.video.model.TitleSummary summary = new prime.video.model.TitleSummary();
        summary.setId(domain.getId());
        summary.setOriginalTitle(domain.getOriginalTitle());
        summary.setReleaseYear(domain.getReleaseYear());
        summary.setImdbRating(domain.getImdbRating());
        
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
}
