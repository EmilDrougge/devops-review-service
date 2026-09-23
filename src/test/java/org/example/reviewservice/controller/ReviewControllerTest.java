package org.example.reviewservice.controller;

import org.example.reviewservice.model.Review;
import org.example.reviewservice.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    private static final String REQUEST_BODY = """
            {"bookingId": 1, "customerId": 10, "rating": 5, "comment": "Great stay"}
            """;

    @Test
    void createReview_returns201WithSavedReview() throws Exception {
        Review saved = new Review();
        saved.setId(1L);
        saved.setBookingId(1L);
        saved.setCustomerId(10L);
        saved.setRoomId(100L);
        saved.setRating(5);
        saved.setComment("Great stay");
        when(reviewService.createReview(any(Review.class))).thenReturn(saved);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomId").value(100))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void createReview_returns403_whenServiceRejects() throws Exception {
        when(reviewService.createReview(any(Review.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "This booking does not belong to the user."));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    void getReviewsForRoom_returnsList() throws Exception {
        Review r = new Review();
        r.setId(1L);
        r.setRoomId(100L);
        r.setRating(4);
        when(reviewService.getReviewsForRoom(100L)).thenReturn(List.of(r));

        mockMvc.perform(get("/api/reviews/room/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].rating").value(4));
    }
}