package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.CollectionsApiDelegate;
import prime.video.model.GetCollections200Response;
import prime.video.model.GetCollectionsCollectionidTitles200Response;
import prime.video.model.GetCollectionsTypeParameter;

import java.util.UUID;

@Service
public class CollectionsApiDelegateImpl implements CollectionsApiDelegate {

    @Override
    public ResponseEntity<GetCollections200Response> listCollections(Integer page, Integer limit, GetCollectionsTypeParameter type) {
        GetCollections200Response response = new GetCollections200Response();
        response.setData(new java.util.ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetCollectionsCollectionidTitles200Response> listTitlesByCollection(UUID collectionId, Integer page, Integer limit) {
        GetCollectionsCollectionidTitles200Response response = new GetCollectionsCollectionidTitles200Response();
        response.setData(new java.util.ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
