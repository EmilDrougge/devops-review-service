package org.example.reviewservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@Component
public class BookingServiceClient {

    private final RestClient restClient;

    public BookingServiceClient(@Value("${booking-service.url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public Map<String, Object> getBooking(Long bookingId) {
        try {
            return restClient.get()
                    .uri("/api/bookings/getbooking/{id}", bookingId)
                    .retrieve()
                    .body(Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found: " + bookingId);
        }
    }
}
