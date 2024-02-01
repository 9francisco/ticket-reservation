package com.example.ticketreservation.service;

import com.example.ticketreservation.model.Booking;
import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.repository.BookingRepository;
import com.example.ticketreservation.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Service for managing booking operations.
 */
@Service
public class BookingService {

    private static final AtomicLong COUNTER = new AtomicLong(1);
    private static final String PREFIX = "TK";

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final ShowService showService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowRepository showRepository, ShowService showService) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.showService = showService;
    }

    /**
     * Checks available seats for a given show.
     *
     * @param showNumber The show number to check seats for.
     * @return A list of available seats.
     */
    public List<String> checkAvailableSeats(String showNumber) {
        Show show = showRepository.findByShowNumber(showNumber);
        if (show == null) {
            throw new IllegalArgumentException("Show not found.");
        }

        // Get booked seats for this show
        Collection<Booking> bookings = bookingRepository.findByShowNumber(showNumber);
        Set<String> bookedSeats = bookings.stream()
                .flatMap(booking -> booking.getSeats().stream())
                .collect(Collectors.toSet());

        // Filter out booked seats from all seats
        return show.getSeats().keySet().stream()
                .filter(seat -> !bookedSeats.contains(seat))
                .sorted((seat1, seat2) -> {
                    String[] parts1 = seat1.split("(?<=\\D)(?=\\d)");
                    String[] parts2 = seat2.split("(?<=\\D)(?=\\d)");
                    int rowComparison = parts1[0].compareTo(parts2[0]);
                    return rowComparison == 0 ? Integer.compare(Integer.parseInt(parts1[1]), Integer.parseInt(parts2[1])) : rowComparison;
                })
                .collect(Collectors.toList());

    }

    /**
     * Books seats on a show for a buyer.
     *
     * @param showNumber The unique identifier of the show.
     * @param phoneNumber The buyer's phone number.
     * @param selectedSeats The seats to book.
     * @return A unique ticket number for the booking.
     */
    public String bookSeats(String showNumber, String phoneNumber, List<String> selectedSeats) {
        Show show = showRepository.findByShowNumber(showNumber);
        if (show == null) {
            throw new IllegalArgumentException("Show not found.");
        }
        if (bookingRepository.isPhoneNumberUsed(showNumber, phoneNumber)) {
            throw new IllegalArgumentException("This phone number has existing booking in this show.");
        }

        // Ensure all selected seats are available
        List<String> availableSeats = checkAvailableSeats(showNumber);
        if (!availableSeats.containsAll(selectedSeats)) {
            throw new IllegalArgumentException("One or more selected seats are not available.");
        }

        // Create and save the booking
        String ticketNumber = generateTicketNumber();
        Booking booking = new Booking(ticketNumber, phoneNumber, showNumber, selectedSeats);
        bookingRepository.save(booking);

        showService.updateShowSeatAvailability(show, selectedSeats, false); // For booking

        return ticketNumber;
    }

    /**
     * Cancels a booking if within the cancellation window.
     *
     * @param ticketNumber The ticket number of the booking to cancel.
     * @param phoneNumber The phone number used for the booking.
     */
    public void cancelBooking(String ticketNumber, String phoneNumber) {
        Booking booking = bookingRepository.findByTicketNumber(ticketNumber);
        if (booking == null || !booking.getPhoneNumber().equals(phoneNumber)) {
            throw new IllegalArgumentException("Booking not found or phone number does not match.");
        }

        Show show = showRepository.findByShowNumber(booking.getShowNumber());
        if (show == null) {
            throw new IllegalArgumentException("Show for this booking not found.");
        }

        // Check cancellation window
        Instant bookingCreationTime = booking.getBookingCreationTime();
        Instant cancellationDeadline = bookingCreationTime.plusSeconds(show.getCancelWindowInMinutes() * 60);

        // Check if the current time is before the cancellation deadline.
        if (Instant.now().isBefore(cancellationDeadline)) {
            // Proceed with cancellation
            bookingRepository.delete(ticketNumber);
            System.out.println("Booking cancelled successfully.");
        } else {
            throw new IllegalArgumentException("Cancellation window has passed.");
        }

        bookingRepository.delete(ticketNumber);

        showService.updateShowSeatAvailability(show, booking.getSeats(), true); // For cancellation
    }

    /**
     * Generates a unique ticket number using a static atomic counter.
     *
     * Example output format: "TK000A1", "TK00CC2", where "TK" is the predefined prefix.
     *
     * @return A string representing the uniquely generated ticket number in the format "PREFIX + padded base-36 counter value".
     */
    public static String generateTicketNumber() {
        long counterValue = COUNTER.getAndIncrement();
        // Convert the counter value to a base-36 string and pad it to ensure 6 characters length
        String base36 = Long.toString(counterValue, 36).toUpperCase();
        String padded = String.format("%6s", base36).replace(' ', '0');
        return PREFIX + padded;
    }
}

