package org.example.reviewservice.repository;

import org.example.reviewservice.TestcontainersConfiguration;
import org.example.reviewservice.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use the MySQL container, not an embedded DB
@Import(TestcontainersConfiguration.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    private Review save(long bookingId, long roomId) {
        Review r = new Review();
        r.setBookingId(bookingId);
        r.setRoomId(roomId);
        r.setCustomerId(10L);
        r.setRating(5);
        r.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(r);
    }

    @Test
    void findByRoomId_returnsOnlyReviewsForThatRoom() {
        save(1L, 100L);
        save(2L, 100L);
        save(3L, 200L);

        assertThat(reviewRepository.findByRoomId(100L)).hasSize(2);
        assertThat(reviewRepository.findByRoomId(300L)).isEmpty();
    }

    @Test
    void existsByBookingId_reflectsSavedReviews() {
        save(1L, 100L);

        assertThat(reviewRepository.existsByBookingId(1L)).isTrue();
        assertThat(reviewRepository.existsByBookingId(2L)).isFalse();
    }
}