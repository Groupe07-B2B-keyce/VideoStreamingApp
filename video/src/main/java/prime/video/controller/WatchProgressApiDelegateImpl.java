package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.WatchProgressApiDelegate;
import prime.video.model.*;

import java.util.UUID;

@Service
public class WatchProgressApiDelegateImpl implements WatchProgressApiDelegate {

    @Override
    public ResponseEntity<Void> deleteWatchProgress(UUID profileId, UUID titleId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<WatchSession> getProgressForTitle(UUID profileId, UUID titleId) {
        WatchSession session = new WatchSession();
        session.setTitleId(titleId);
        session.setProgressSeconds(0);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetProfilesProfileidWatchProgress200Response> getWatchHistory(UUID profileId, Integer page, Integer limit, GetProfilesProfileidWatchProgressStatusParameter status) {
        GetProfilesProfileidWatchProgress200Response response = new GetProfilesProfileidWatchProgress200Response();
        response.setData(new java.util.ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WatchSession> updateWatchProgress(UUID profileId, UUID titleId, UpdateWatchProgressRequest updateWatchProgressRequest) {
        WatchSession session = new WatchSession();
        session.setProgressSeconds(updateWatchProgressRequest.getProgressSeconds());
        return new ResponseEntity<>(session, HttpStatus.OK);
    }
}
