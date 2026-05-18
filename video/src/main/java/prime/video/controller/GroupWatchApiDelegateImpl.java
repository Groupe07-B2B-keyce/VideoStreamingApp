package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.GroupWatchApiDelegate;
import prime.video.model.CreateGroupWatchRequest;
import prime.video.model.GroupWatchControlRequest;
import prime.video.model.GroupWatchParticipant;
import prime.video.model.GroupWatchSession;
import prime.video.model.PostGroupwatchSessionidJoinRequest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GroupWatchApiDelegateImpl implements GroupWatchApiDelegate {

    private final Map<UUID, GroupWatchSession> sessions = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<GroupWatchSession> createGroupWatchSession(CreateGroupWatchRequest createGroupWatchRequest) {
        UUID hostProfileId = UUID.randomUUID();
        GroupWatchSession session = new GroupWatchSession();
        session.setId(UUID.randomUUID());
        session.setTitleId(createGroupWatchRequest.getTitleId());
        session.setEpisodeId(createGroupWatchRequest.getEpisodeId());
        session.setHostProfileId(hostProfileId);
        session.setStatus(GroupWatchSession.StatusEnum.WAITING);
        session.setInviteCode("PRIME" + UUID.randomUUID().toString().substring(0, 3).toUpperCase());
        session.setCurrentPositionSeconds(0);
        session.setMaxParticipants(Optional.ofNullable(createGroupWatchRequest.getMaxParticipants()).orElse(100));
        session.setParticipants(new ArrayList<>());
        session.getParticipants().add(participant(hostProfileId, "Host", GroupWatchParticipant.RoleEnum.HOST));
        session.setCreatedAt(OffsetDateTime.now());
        sessions.put(session.getId(), session);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<GroupWatchSession> getGroupWatchSession(UUID sessionId) {
        GroupWatchSession session = sessions.get(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupWatchSession> joinGroupWatchSession(UUID sessionId, PostGroupwatchSessionidJoinRequest postGroupwatchSessionidJoinRequest) {
        GroupWatchSession session = sessions.get(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!session.getInviteCode().equalsIgnoreCase(postGroupwatchSessionidJoinRequest.getInviteCode())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (session.getParticipants().size() >= session.getMaxParticipants()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UUID profileId = UUID.randomUUID();
        session.getParticipants().add(participant(profileId, "Viewer " + (session.getParticipants().size() + 1), GroupWatchParticipant.RoleEnum.VIEWER));
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupWatchSession> controlGroupWatch(UUID sessionId, GroupWatchControlRequest groupWatchControlRequest) {
        GroupWatchSession session = sessions.get(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (groupWatchControlRequest.getAction() == GroupWatchControlRequest.ActionEnum.PLAY) {
            session.setStatus(GroupWatchSession.StatusEnum.PLAYING);
        } else if (groupWatchControlRequest.getAction() == GroupWatchControlRequest.ActionEnum.PAUSE) {
            session.setStatus(GroupWatchSession.StatusEnum.PAUSED);
        } else if (groupWatchControlRequest.getAction() == GroupWatchControlRequest.ActionEnum.SEEK) {
            int positionSeconds = Optional.ofNullable(groupWatchControlRequest.getPositionSeconds()).orElse(0);
            session.setCurrentPositionSeconds(Math.max(0, positionSeconds));
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> leaveGroupWatchSession(UUID sessionId) {
        GroupWatchSession session = sessions.get(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!session.getParticipants().isEmpty()) {
            session.getParticipants().remove(session.getParticipants().size() - 1);
        }
        if (session.getParticipants().isEmpty()) {
            session.setStatus(GroupWatchSession.StatusEnum.ENDED);
            sessions.remove(sessionId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private GroupWatchParticipant participant(UUID profileId, String displayName, GroupWatchParticipant.RoleEnum role) {
        GroupWatchParticipant participant = new GroupWatchParticipant();
        participant.setProfileId(profileId);
        participant.setDisplayName(displayName);
        participant.setRole(role);
        participant.setJoinedAt(OffsetDateTime.now());
        return participant;
    }
}
