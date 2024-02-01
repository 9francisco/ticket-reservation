package com.example.ticketreservation.controller;

import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.service.BookingService;
import com.example.ticketreservation.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for ShowBookingController.
 */
public class ShowBookingControllerTest {

    @Mock
    private ShowService showService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private ShowBookingController showBookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookSeats() {
        // Given
        String showNumber = "SH0001";
        String phoneNumber = "+1234567890";
        List<String> selectedSeats = Arrays.asList("A1", "B1");
        String ticketNumber = "TK000001";
        when(bookingService.bookSeats(showNumber, phoneNumber, selectedSeats)).thenReturn(ticketNumber);

        // When
        ResponseEntity<String> response = showBookingController.bookSeats(showNumber, phoneNumber, selectedSeats);

        // Then
        assertEquals("Booking successful. Ticket Number: " + ticketNumber, response.getBody());
        verify(bookingService).bookSeats(showNumber, phoneNumber, selectedSeats);
    }

    @Test
    void testCancelBooking() {
        // Given
        String ticketNumber = "TK000001";
        String phoneNumber = "+1234567890";
        doNothing().when(bookingService).cancelBooking(ticketNumber, phoneNumber);

        // When
        ResponseEntity<String> response = showBookingController.cancelBooking(ticketNumber, phoneNumber);

        // Then
        assertEquals("Booking cancelled successfully.", response.getBody());
        verify(bookingService).cancelBooking(ticketNumber, phoneNumber);
    }

    @Test
    void testGetShowDetails() {
        // Given
        String showNumber = "SH0001";
        List<String> showDetails = Collections.singletonList("[Show Number: SH0001, Ticket Number: TK000001, Phone Number: 123, Seats: A1]");
        when(showService.displayShowDetails(showNumber)).thenReturn(showDetails);

        // When
        ResponseEntity<List<String>> response = showBookingController.getShowDetails(showNumber);

        // Then
        assertEquals(showDetails, response.getBody());
        verify(showService).displayShowDetails(showNumber);
    }

    @Test
    void testConfigureShow() {
        // Given
        Show show = new Show();
        show.setShowNumber("SH0001");
        show.setNumberOfRows(20);
        show.setSeatsPerRow(6);
        show.setCancelWindowInMinutes(120);

        doNothing().when(showService).configureShow(anyString(), anyInt(), anyInt(), anyInt());

        // When
        ResponseEntity<String> response = showBookingController.configureShow(show);

        // Then
        assertEquals("Show configured successfully.", response.getBody());
        verify(showService).configureShow(show.getShowNumber(), show.getNumberOfRows(),
                show.getSeatsPerRow(), show.getCancelWindowInMinutes());
    }

    @Test
    void testCheckAvailableSeats() {
        // Given
        String showNumber = "SH0001";
        List<String> availableSeats = Arrays.asList("A1", "B1", "C1");
        when(bookingService.checkAvailableSeats(showNumber)).thenReturn(availableSeats);

        // When
        ResponseEntity<List<String>> response = showBookingController.checkAvailableSeats(showNumber);

        // Then
        assertEquals(availableSeats, response.getBody());
        verify(bookingService).checkAvailableSeats(showNumber);
    }
    
}