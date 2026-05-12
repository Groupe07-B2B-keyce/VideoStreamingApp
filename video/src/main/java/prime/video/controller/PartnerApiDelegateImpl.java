package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.PartnerApiDelegate;
import prime.video.model.GetPartnerTitles200Response;
import prime.video.model.IngestionJob;
import prime.video.model.SubmitTitleRequest;
import prime.video.model.TitleAnalytics;

import java.util.UUID;

@Service
public class PartnerApiDelegateImpl implements PartnerApiDelegate {

    @Override
    public ResponseEntity<IngestionJob> getIngestionJob(UUID jobId) {
        IngestionJob job = new IngestionJob();
        job.setId(jobId);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetPartnerTitles200Response> listPartnerTitles(Integer page, Integer limit, prime.video.model.GetPartnerTitlesStatusParameter status) {
        GetPartnerTitles200Response response = new GetPartnerTitles200Response();
        response.setData(new java.util.ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IngestionJob> submitTitle(SubmitTitleRequest submitTitleRequest) {
        IngestionJob job = new IngestionJob();
        job.setId(UUID.randomUUID());
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TitleAnalytics> getPartnerTitleAnalytics(UUID titleId, java.time.LocalDate from, java.time.LocalDate to) {
        TitleAnalytics analytics = new TitleAnalytics();
        analytics.setTitleId(titleId);
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }
}
