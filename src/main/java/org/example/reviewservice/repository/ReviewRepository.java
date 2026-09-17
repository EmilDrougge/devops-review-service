package org.example.reviewservice.repository;

import org.example.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoomId(Long roomId);
    boolean existsByBookingId(Long bookingId);
}
