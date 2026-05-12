package prime.video.controller;

import prime.video.api.CatalogApiDelegate;
import prime.video.model.GetTitles200Response;
import prime.video.model.GetTitlesAccesstypeParameter;
import prime.video.model.GetTitlesRatingParameter;
import prime.video.model.GetTitlesSortParameter;
import prime.video.model.GetTitlesTypeParameter;
import prime.video.model.PaginatedResponseMeta;
import prime.video.model.Title;
import prime.video.service.TitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CatalogApiDelegateImpl implements CatalogApiDelegate {

    private final TitleService titleService;

    public CatalogApiDelegateImpl(TitleService titleService) {
        this.titleService = titleService;
    }

    @Override
    public ResponseEntity<GetTitles200Response> listTitles(Integer page,
                                                        Integer limit,
                                                        GetTitlesTypeParameter type,
                                                        UUID genreId,
                                                        Integer releaseYear,
                                                        GetTitlesRatingParameter rating,
                                                        GetTitlesAccesstypeParameter accessType,
                                                        GetTitlesSortParameter sort,
                                                        String q) {
        List<prime.video.model.TitleSummary> titles = titleService.getAllTitleSummaries();
        
        GetTitles200Response response = new GetTitles200Response();
        response.setData(titles);
        
        PaginatedResponseMeta meta = new PaginatedResponseMeta();
        meta.setCurrentPage(page != null ? page : 1);
        meta.setItemsPerPage(limit != null ? limit : 20);
        meta.setTotalItems(titles.size());
        meta.setTotalPages((int) Math.ceil((double) titles.size() / meta.getItemsPerPage()));
        
        response.setMeta(meta);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Title> getTitleById(UUID titleId) {
        Title title = titleService.getTitleModelById(titleId);
        if (title != null) {
            return new ResponseEntity<>(title, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<prime.video.model.Season>> listSeasonsByTitle(UUID titleId) {
        List<prime.video.model.Season> seasons = new ArrayList<>();
        prime.video.model.Season s1 = new prime.video.model.Season();
        s1.setId(UUID.randomUUID());
        s1.setSeasonNumber(1);
        s1.setTitle("Saison 1");
        s1.setStatus(prime.video.model.Season.StatusEnum.PUBLISHED);
        s1.setCreatedAt(java.time.OffsetDateTime.now());
        s1.setUpdatedAt(java.time.OffsetDateTime.now());
        seasons.add(s1);
        return new ResponseEntity<>(seasons, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<prime.video.model.Episode>> listEpisodesBySeason(UUID titleId, Integer seasonNumber) {
        List<prime.video.model.Episode> episodes = new ArrayList<>();
        prime.video.model.Episode e1 = new prime.video.model.Episode();
        e1.setId(UUID.randomUUID());
        e1.setEpisodeNumber(1);
        e1.setTitle("Épisode 1");
        e1.setStatus(prime.video.model.Episode.StatusEnum.PUBLISHED);
        e1.setCreatedAt(java.time.OffsetDateTime.now());
        e1.setUpdatedAt(java.time.OffsetDateTime.now());
        
        prime.video.model.TechnicalDetails tech = new prime.video.model.TechnicalDetails();
        tech.setDurationMinutes(45);
        tech.setResolutionMax(prime.video.model.TechnicalDetails.ResolutionMaxEnum.UHD_4K);
        tech.setHasHDR(true);
        tech.setHasDolbyAtmos(true);
        e1.setTechnicalDetails(tech);
        
        episodes.add(e1);
        return new ResponseEntity<>(episodes, HttpStatus.OK);
    }
}
