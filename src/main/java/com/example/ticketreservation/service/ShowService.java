package com.example.ticketreservation.service;

import com.example.ticketreservation.model.Booking;
import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.repository.BookingRepository;
import com.example.ticketreservation.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing show operations.
 */
@Service
public class ShowService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;

    @Autowired
    public ShowService(ShowRepository showRepository, BookingRepository bookingRepository) {
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Configures a new show.
     *
     * @param showNumber The unique identifier for the show.
     * @param numberOfRows The number of rows in the show.
     * @param seatsPerRow The number of seats per row.
     * @param cancelWindowInMinutes The window in minutes in which a booking can be cancelled.
     */
    public void configureShow(String showNumber, int numberOfRows, int seatsPerRow, int cancelWindowInMinutes) {
        if (numberOfRows > 26 || seatsPerRow > 10) {
            throw new IllegalArgumentException("Number of rows cannot exceed 26 and seats per row cannot exceed 10.");
        }
        Show show = new Show(showNumber, numberOfRows, seatsPerRow, cancelWindowInMinutes);
        showRepository.save(show);
    }

    /**
     * Updates the availability of seats for a show.
     *
     * @param show The show to update.
     * @param seats The seats to update.
     * @param available The new availability status for the seats.
     */
    public void updateShowSeatAvailability(Show show, List<String> seats, boolean available) {
        show.updateSeatAvailability(seats, available);
        showRepository.save(show);
    }

    /**
     * Displays detailed information for a specific show, including show number,
     * and for each booking on that show: the ticket number, buyer phone number,
     * and the seats allocated to the buyer.
     *
     * @param showNumber The show number for which detailed information is requested.
     *
     */
    public List<String> displayShowDetails(String showNumber) {
        List<String> details = new ArrayList<>();

        Show show = showRepository.findByShowNumber(showNumber);
        if (show == null) {
            throw new IllegalArgumentException("Show number " + showNumber + " not found.");
        }

        details.add("Show Number: " + show.getShowNumber());

        List<Booking> bookings = new ArrayList<>(bookingRepository.findByShowNumber(showNumber));

        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("No bookings found for show " + showNumber);
        }

        bookings.forEach(booking -> details.add("Ticket Number: " + booking.getTicketNumber() +
                ", Phone Number: " + booking.getPhoneNumber() +
                ", Seats: " + String.join(", ", booking.getSeats())));

        return details;
    }

    /**
     * Retrieves show details by show number.
     *
     * @param showNumber The show number of the show to retrieve.
     * @return The show details.
     */
    public Show getShowDetails(String showNumber) {
        return showRepository.findByShowNumber(showNumber);
    }

}

