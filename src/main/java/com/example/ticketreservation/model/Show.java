package com.example.ticketreservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a show with a specific configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    private String showNumber;
    private int numberOfRows;
    private int seatsPerRow;
    private int cancelWindowInMinutes;
    // A map to hold seat availability status. True if available, false if booked.
    private Map<String, Boolean> seats;

    /**
     * Initializes a show with seat availability.
     *
     * @param showNumber The unique identifier of the show.
     * @param numberOfRows The number of rows in the show.
     * @param seatsPerRow The number of seats per row in the show.
     * @param cancelWindowInMinutes The window in minutes for ticket cancellation.
     */
    public Show(String showNumber, int numberOfRows, int seatsPerRow, int cancelWindowInMinutes) {
        this.showNumber = showNumber;
        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;
        this.cancelWindowInMinutes = cancelWindowInMinutes;
        this.seats = new HashMap<>();
        initializeSeats();
    }

    /**
     * Initializes the seat map for a new show.
     */
    private void initializeSeats() {
        char row = 'A';
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 1; j <= seatsPerRow; j++) {
                // Example seat format: A1, A2, B1...
                String seat = row + String.valueOf(j);
                this.seats.put(seat, true); // All seats are initially available
            }
            row++;
        }
    }

    /**
     * Updates the availability of a list of seats.
     *
     * @param seats The seats to update.
     * @param available The new availability status.
     */
    public void updateSeatAvailability(List<String> seats, boolean available) {
        for (String seat : seats) {
            if (this.seats.containsKey(seat)) {
                this.seats.put(seat, available);
            }
        }
    }
}
