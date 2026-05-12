package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.RecommendationsApiDelegate;
import prime.video.model.GetProfilesProfileidRecommendationsReasonParameter;
import prime.video.model.RecommendationPage;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class RecommendationsApiDelegateImpl implements RecommendationsApiDelegate {

    @Override
    public ResponseEntity<RecommendationPage> getRecommendations(UUID profileId, Integer limit, GetProfilesProfileidRecommendationsReasonParameter reason) {
        RecommendationPage response = new RecommendationPage();
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
