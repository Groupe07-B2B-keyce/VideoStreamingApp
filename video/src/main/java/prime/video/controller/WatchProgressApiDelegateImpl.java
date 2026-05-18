package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.WatchProgressApiDelegate;
import prime.video.model.GetProfilesProfileidWatchProgress200Response;
import prime.video.model.GetProfilesProfileidWatchProgressStatusParameter;
import prime.video.model.UpdateWatchProgressRequest;
import prime.video.model.WatchHistory;
import prime.video.model.WatchSession;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WatchProgressApiDelegateImpl implements WatchProgressApiDelegate {

    private final Map<ProgressKey, WatchSession> sessions = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<Void> deleteWatchProgress(UUID profileId, UUID titleId) {
        WatchSession removed = sessions.remove(new ProgressKey(profileId, titleId));
        return new ResponseEntity<>(removed == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<WatchSession> getProgressForTitle(UUID profileId, UUID titleId) {
        WatchSession session = sessions.get(new ProgressKey(profileId, titleId));
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetProfilesProfileidWatchProgress200Response> getWatchHistory(
            UUID profileId,
            Integer page,
            Integer limit,
            GetProfilesProfileidWatchProgressStatusParameter status) {

        int safePage = Math.max(1, page == null ? 1 : page);
        int safeLimit = Math.max(1, Math.min(100, limit == null ? 20 : limit));
        int skip = (safePage - 1) * safeLimit;

        List<WatchHistory> data = sessions.values().stream()
                .filter(session -> profileId.equals(session.getProfileId()))
                .filter(session -> status == null || session.getStatus() == null || status.getValue().equals(session.getStatus().getValue()))
                .sorted(Comparator.comparing(WatchSession::getLastWatchedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .skip(skip)
                .limit(safeLimit)
                .map(this::toHistory)
                .collect(Collectors.toCollection(ArrayList::new));

        GetProfilesProfileidWatchProgress200Response response = new GetProfilesProfileidWatchProgress200Response();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WatchSession> updateWatchProgress(
            UUID profileId,
            UUID titleId,
            UpdateWatchProgressRequest updateWatchProgressRequest) {

        if (updateWatchProgressRequest.getProgressSeconds() == null || updateWatchProgressRequest.getDurationSeconds() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ProgressKey key = new ProgressKey(profileId, titleId);
        WatchSession session = sessions.computeIfAbsent(key, ignored -> createSession(profileId, titleId));

        int durationSeconds = Math.max(1, updateWatchProgressRequest.getDurationSeconds());
        int progressSeconds = Math.max(0, Math.min(updateWatchProgressRequest.getProgressSeconds(), durationSeconds));
        double completionPercent = Math.round((progressSeconds * 10000.0 / durationSeconds)) / 100.0;
        OffsetDateTime now = OffsetDateTime.now();

        session.setUpdatedAt(now);
        session.setLastWatchedAt(now);
        session.setProgressSeconds(progressSeconds);
        session.setDurationSeconds(durationSeconds);
        session.setCompletionPercent(completionPercent);
        session.setEpisodeId(updateWatchProgressRequest.getEpisodeId());
        session.setStatus(completionPercent >= 90.0 ? WatchSession.StatusEnum.COMPLETED : WatchSession.StatusEnum.IN_PROGRESS);

        if (updateWatchProgressRequest.getDeviceType() != null) {
            session.setDeviceType(WatchSession.DeviceTypeEnum.fromValue(updateWatchProgressRequest.getDeviceType().getValue()));
        }
        if (updateWatchProgressRequest.getQualitySelected() != null) {
            session.setQualitySelected(WatchSession.QualitySelectedEnum.fromValue(updateWatchProgressRequest.getQualitySelected().getValue()));
        }

        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    private WatchHistory toHistory(WatchSession session) {
        WatchHistory history = new WatchHistory();
        history.setSession(session);
        return history;
    }
    private WatchSession createSession(UUID profileId, UUID titleId) {
        OffsetDateTime now = OffsetDateTime.now();
        WatchSession session = new WatchSession();
        session.setId(UUID.randomUUID());
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setProfileId(profileId);
        session.setTitleId(titleId);
        session.setProgressSeconds(0);
        session.setDurationSeconds(1);
        session.setCompletionPercent(0.0);
        session.setStatus(WatchSession.StatusEnum.IN_PROGRESS);
        session.setLastWatchedAt(now);
        return session;
    }

    private record ProgressKey(UUID profileId, UUID titleId) {
        private ProgressKey {
            Objects.requireNonNull(profileId, "profileId");
            Objects.requireNonNull(titleId, "titleId");
        }
    }
}
