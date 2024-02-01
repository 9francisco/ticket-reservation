package com.example.ticketreservation.repository;

import com.example.ticketreservation.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In-memory repository for managing Booking objects.
 */
@Repository
public class BookingRepository {

    private final Map<String, Booking> bookings = new HashMap<>();

    /**
     * Saves a booking in the repository.
     *
     * @param booking The booking to save.
     */
    public void save(Booking booking) {
        bookings.put(booking.getTicketNumber(), booking);
    }

    /**
     * Retrieves a booking by its ticket number.
     *
     * @param ticketNumber The ticket number to search for.
     * @return The booking if found, or null otherwise.
     */
    public Booking findByTicketNumber(String ticketNumber) {
        return bookings.get(ticketNumber);
    }

    /**
     * Deletes a booking from the repository.
     *
     * @param ticketNumber The ticket number of the booking to delete.
     */
    public void delete(String ticketNumber) {
        bookings.remove(ticketNumber);
    }

    /**
     * Retrieves all bookings for a given show number.
     *
     * @param showNumber The show number to search bookings for.
     * @return A collection of bookings for the specified show.
     */
    public Collection<Booking> findByShowNumber(String showNumber) {
        return bookings.values().stream()
                .filter(booking -> booking.getShowNumber().equals(showNumber))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a phone number has already been used for a booking on a specific show.
     *
     * @param showNumber The show number to check.
     * @param phoneNumber The phone number to search for.
     * @return true if the phone number already has a booking on the show, false otherwise.
     */
    public boolean isPhoneNumberUsed(String showNumber, String phoneNumber) {
        return bookings.values().stream()
                .anyMatch(booking -> booking.getShowNumber().equals(showNumber) && booking.getPhoneNumber().equals(phoneNumber));
    }
}
