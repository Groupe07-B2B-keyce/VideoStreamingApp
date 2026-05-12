package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.ReviewsApiDelegate;
import prime.video.model.CreateReviewRequest;
import prime.video.model.Review;
import prime.video.model.ReviewPage;
import prime.video.model.TitlesTitleidReviewsSortParameter;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class ReviewsApiDelegateImpl implements ReviewsApiDelegate {

    @Override
    public ResponseEntity<Review> createReview(UUID titleId, CreateReviewRequest createReviewRequest, TitlesTitleidReviewsSortParameter sort) {
        Review review = new Review();
        review.setId(UUID.randomUUID());
        review.setTitleId(titleId);
        review.setRating(createReviewRequest.getRating());
        review.setComment(createReviewRequest.getComment());
        review.setCreatedAt(java.time.OffsetDateTime.now());
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteReview(UUID titleId, UUID reviewId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ReviewPage> listReviews(UUID titleId, TitlesTitleidReviewsSortParameter sort, Integer page, Integer limit) {
        ReviewPage response = new ReviewPage();
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Review> updateReview(UUID titleId, UUID reviewId, CreateReviewRequest createReviewRequest) {
        return new ResponseEntity<>(new Review(), HttpStatus.OK);
    }
}
