package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.GroupWatchApiDelegate;
import prime.video.model.CreateGroupWatchRequest;
import prime.video.model.GroupWatchControlRequest;
import prime.video.model.GroupWatchSession;
import prime.video.model.PostGroupwatchSessionidJoinRequest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class GroupWatchApiDelegateImpl implements GroupWatchApiDelegate {

    @Override
    public ResponseEntity<GroupWatchSession> createGroupWatchSession(CreateGroupWatchRequest createGroupWatchRequest) {
        GroupWatchSession session = new GroupWatchSession();
        session.setId(UUID.randomUUID());
        session.setTitleId(createGroupWatchRequest.getTitleId());
        session.setStatus(GroupWatchSession.StatusEnum.WAITING);
        session.setInviteCode("PRIME" + UUID.randomUUID().toString().substring(0, 3).toUpperCase());
        session.setParticipants(new ArrayList<>());
        session.setCreatedAt(OffsetDateTime.now());
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<GroupWatchSession> getGroupWatchSession(UUID sessionId) {
        GroupWatchSession session = new GroupWatchSession();
        session.setId(sessionId);
        session.setTitleId(UUID.randomUUID());
        session.setStatus(GroupWatchSession.StatusEnum.PLAYING);
        session.setParticipants(new ArrayList<>());
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupWatchSession> joinGroupWatchSession(UUID sessionId, PostGroupwatchSessionidJoinRequest postGroupwatchSessionidJoinRequest) {
        GroupWatchSession session = new GroupWatchSession();
        session.setId(sessionId);
        session.setStatus(GroupWatchSession.StatusEnum.PLAYING);
        session.setParticipants(new ArrayList<>());
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupWatchSession> controlGroupWatch(UUID sessionId, GroupWatchControlRequest groupWatchControlRequest) {
        GroupWatchSession session = new GroupWatchSession();
        session.setId(sessionId);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> leaveGroupWatchSession(UUID sessionId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
