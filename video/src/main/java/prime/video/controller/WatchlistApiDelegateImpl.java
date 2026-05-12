package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.WatchlistApiDelegate;
import prime.video.model.AddToWatchlistRequest;
import prime.video.model.WatchlistItem;
import prime.video.model.WatchlistPage;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class WatchlistApiDelegateImpl implements WatchlistApiDelegate {

    @Override
    public ResponseEntity<WatchlistPage> getWatchlist(UUID profileId, Integer page, Integer limit) {
        WatchlistPage response = new WatchlistPage();
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WatchlistItem> addToWatchlist(UUID profileId, AddToWatchlistRequest addToWatchlistRequest) {
        WatchlistItem item = new WatchlistItem();
        item.setId(UUID.randomUUID());
        item.setTitleId(addToWatchlistRequest.getTitleId());
        item.setAddedAt(java.time.OffsetDateTime.now());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> removeFromWatchlist(UUID profileId, String watchlistItemId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
