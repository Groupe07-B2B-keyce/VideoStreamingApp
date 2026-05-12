package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.PlaybackApiDelegate;
import prime.video.model.PlaybackToken;
import prime.video.model.StartPlaybackRequest;

import java.util.UUID;

@Service
public class PlaybackApiDelegateImpl implements PlaybackApiDelegate {

    @Override
    public ResponseEntity<PlaybackToken> startPlayback(UUID titleId, StartPlaybackRequest startPlaybackRequest) {
        PlaybackToken token = new PlaybackToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(java.time.OffsetDateTime.now().plusHours(4));
        token.setStreamUrl(java.net.URI.create("https://stream.primevideo.com/api/v1/manifest/" + titleId));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
