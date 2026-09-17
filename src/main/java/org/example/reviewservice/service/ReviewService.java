package org.example.reviewservice.service;

import org.example.reviewservice.client.BookingServiceClient;
import org.example.reviewservice.model.Review;
import org.example.reviewservice.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingServiceClient bookingServiceClient;

    public ReviewService(ReviewRepository reviewRepository, BookingServiceClient bookingServiceClient) {
        this.reviewRepository = reviewRepository;
        this.bookingServiceClient = bookingServiceClient;
    }

    @SuppressWarnings("unchecked")
    public Review createReview(Review incoming) {
        Map<String, Object> booking = bookingServiceClient.getBooking(incoming.getBookingId());

        Long bookingCustomerId = Long.valueOf(booking.get("customerID").toString());

        Map<String, Object> room = (Map<String, Object>) booking.get("room");
        Long roomId = Long.valueOf(room.get("id").toString());

        LocalDate endDate = LocalDate.parse(booking.get("endDate").toString());

        if (!bookingCustomerId.equals(incoming.getCustomerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This booking does not belong to the user.");
        }

        if (endDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only review a booking after checkout.");
        }

        if (reviewRepository.existsByBookingId(incoming.getBookingId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A review already exists for this booking.");
        }

        incoming.setRoomId(roomId);
        incoming.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(incoming);
    }

    public List<Review> getReviewsForRoom(Long roomId) {
        return reviewRepository.findByRoomId(roomId);
    }
}