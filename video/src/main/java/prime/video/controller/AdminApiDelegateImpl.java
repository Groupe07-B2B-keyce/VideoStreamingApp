package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.AdminApiDelegate;
import prime.video.model.CreateLicenseRequest;
import prime.video.model.GetAdminIngestionQueue200Response;
import prime.video.model.GlobalPlatformStats;
import prime.video.model.IngestionJob;
import prime.video.model.License;
import prime.video.model.UpdateIngestionStatusRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminApiDelegateImpl implements AdminApiDelegate {

    @Override
    public ResponseEntity<License> createLicense(UUID titleId, CreateLicenseRequest createLicenseRequest) {
        License license = new License();
        license.setId(UUID.randomUUID());
        license.setTitleId(titleId);
        return new ResponseEntity<>(license, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<GlobalPlatformStats> getGlobalStats() {
        GlobalPlatformStats stats = new GlobalPlatformStats();
        stats.setTotalUsers(1000000);
        stats.setActiveSubscriptions(800000);
        stats.setTotalTitles(5000);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetAdminIngestionQueue200Response> getIngestionQueue(Integer page, Integer limit) {
        GetAdminIngestionQueue200Response response = new GetAdminIngestionQueue200Response();
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<License>> listTitleLicenses(UUID titleId) {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IngestionJob> updateIngestionStatus(UUID jobId, UpdateIngestionStatusRequest updateIngestionStatusRequest) {
        IngestionJob job = new IngestionJob();
        job.setId(jobId);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }
}
