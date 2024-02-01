package com.example.ticketreservation.controller;

import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.service.BookingService;
import com.example.ticketreservation.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for managing show bookings.
 */
@RestController
@RequestMapping("/api")
public class ShowBookingController {

    private final ShowService showService;
    private final BookingService bookingService;

    /**
     * Constructs a ShowBookingController with necessary services.
     *
     * @param showService   The service for show operations
     * @param bookingService  The service for booking operations
     */
    @Autowired
    public ShowBookingController(ShowService showService, BookingService bookingService) {
        this.showService = showService;
        this.bookingService = bookingService;
    }

    /**
     * Checks and returns available seats for a specified show.
     *
     * @param showNumber The show number to check
     * @return A list of available seat numbers
     */
    @GetMapping("/availability/{showNumber}")
    public ResponseEntity<List<String>> checkAvailableSeats(@PathVariable String showNumber) {
        List<String> availableSeats = bookingService.checkAvailableSeats(showNumber);

        return ResponseEntity.ok(availableSeats);
    }

    /**
     * Books seats on a show for a buyer.
     *
     * @param showNumber   The show number to book
     * @param phoneNumber  The buyer's phone number
     * @param selectedSeats  A list of selected seats to book
     * @return A booking confirmation message
     */
    @PostMapping("/book/{showNumber}")
    public ResponseEntity<String> bookSeats(
            @PathVariable String showNumber,
            @RequestParam String phoneNumber,
            @RequestParam List<String> selectedSeats) {
        String ticketNumber = bookingService.bookSeats(showNumber, phoneNumber, selectedSeats);

        return new ResponseEntity<>("Booking successful. Ticket Number: " + ticketNumber, HttpStatus.CREATED);
    }

    /**
     * Cancels a booking.
     *
     * @param ticketNumber  The ticket number of the booking to cancel
     * @param phoneNumber The phone number used for the booking
     * @return A cancellation confirmation message
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<String> cancelBooking(
            @RequestParam String ticketNumber,
            @RequestParam String phoneNumber) {
        bookingService.cancelBooking(ticketNumber, phoneNumber);

        return ResponseEntity.ok("Booking cancelled successfully.");
    }

    /**
     * Configures a new show.
     *
     * @param show The show configuration request
     * @return A success message for show configuration
     */
    @PostMapping("/setup")
    public ResponseEntity<String> configureShow(
            @RequestBody Show show) {
        showService.configureShow(show.getShowNumber(), show.getNumberOfRows(),
                show.getSeatsPerRow(), show.getCancelWindowInMinutes());

        return new ResponseEntity<>("Show configured successfully.", HttpStatus.CREATED);
    }

    /**
     * Retrieves details for a specific show.
     *
     * @param showNumber The show number to retrieve details for
     * @return Show details, including booked seats
     */
    @GetMapping("/view/{showNumber}")
    public ResponseEntity<List<String>> getShowDetails(@PathVariable String showNumber) {
        List<String> details = showService.displayShowDetails(showNumber);

        return ResponseEntity.ok(details);
    }
}
