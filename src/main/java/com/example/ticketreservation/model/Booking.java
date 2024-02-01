package com.example.ticketreservation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Represents a booking made by a buyer.
 */
@Data
@NoArgsConstructor
public class Booking {

    private String ticketNumber;
    private String phoneNumber;
    private String showNumber;
    private List<String> seats;
    private Instant bookingCreationTime;

    /**
     * Creates a new booking with given details.
     *
     * @param ticketNumber Unique identifier for the booking.
     * @param phoneNumber Phone number of the buyer.
     * @param showNumber Identifier of the show being booked.
     * @param seats List of seat identifiers being booked.
     */
    public Booking(String ticketNumber, String phoneNumber, String showNumber, List<String> seats) {
        this.ticketNumber = ticketNumber;
        this.phoneNumber = phoneNumber;
        this.showNumber = showNumber;
        this.seats = seats;
        this.bookingCreationTime = Instant.now(); // Automatically set the booking creation time
    }
}

