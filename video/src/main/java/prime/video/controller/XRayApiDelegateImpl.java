package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.XRayApiDelegate;
import prime.video.model.PostAdminTitlesTitleidXrayRequest;
import prime.video.model.TimelineEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class XRayApiDelegateImpl implements XRayApiDelegate {

    @Override
    public ResponseEntity<List<TimelineEvent>> getXRayAtTimestamp(UUID titleId, Integer timestampSeconds, UUID episodeId) {
        List<TimelineEvent> events = new ArrayList<>();
        TimelineEvent event = new TimelineEvent();
        event.setId(UUID.randomUUID());
        event.setText("Anecdote X-Ray sur cette scène.");
        events.add(event);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TimelineEvent> createTimelineEvent(UUID titleId, PostAdminTitlesTitleidXrayRequest postAdminTitlesTitleidXrayRequest) {
        TimelineEvent event = new TimelineEvent();
        event.setId(UUID.randomUUID());
        event.setTitleId(titleId);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }
}
