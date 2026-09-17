package org.example.reviewservice.controller;

import org.example.reviewservice.model.Review;
import org.example.reviewservice.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review saved = reviewService.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Review>> getReviewsForRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(reviewService.getReviewsForRoom(roomId));
    }

}
