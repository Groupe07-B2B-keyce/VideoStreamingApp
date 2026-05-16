package prime.video.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prime.video.domain.Title;
import prime.video.repository.TitleRepository;

import java.util.UUID;

/**
 * Service de comptage des vues — incrémente le compteur à chaque lancement de lecture.
 */
@Service
public class ViewCountService {

    private final TitleRepository titleRepository;

    public ViewCountService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * Incrémente le compteur de vues totales pour un titre donné.
     * Appelé automatiquement lors du chargement de la page /lecture/{id}.
     */
    @Transactional
    public void incrementViewCount(UUID titleId) {
        titleRepository.findById(titleId).ifPresent(title -> {
            title.setTotalViews(title.getTotalViews() + 1);
            titleRepository.save(title);
        });
    }

    public Integer getViewCount(UUID titleId) {
        return titleRepository.findById(titleId)
                .map(Title::getTotalViews)
                .orElse(0);
    }
}
