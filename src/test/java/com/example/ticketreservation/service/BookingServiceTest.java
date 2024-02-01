package com.example.ticketreservation.service;

import com.example.ticketreservation.model.Booking;
import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.repository.BookingRepository;
import com.example.ticketreservation.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for BookingService.
 */
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowService showService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookSeatsSuccess() {
        String showNumber = "SH000123";
        Show show = new Show(showNumber, 10, 5, 60);
        when(showRepository.findByShowNumber(showNumber)).thenReturn(show);
        when(bookingRepository.isPhoneNumberUsed(showNumber, "+1234567890")).thenReturn(false);
        List<String> seats = Arrays.asList("A1", "A2");
        doNothing().when(bookingRepository).save(any(Booking.class));

        String ticketNumber = bookingService.bookSeats(showNumber, "+1234567890", seats);

        assertNotNull(ticketNumber);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCancelBookingSuccess() {
        String ticketNumber = "TK0000A1";
        Booking booking = new Booking(ticketNumber, "+1234567890", "SH000123", Arrays.asList("A1", "A2"));
        when(bookingRepository.findByTicketNumber(ticketNumber)).thenReturn(booking);

        Show show = new Show("SH000123", 10, 5, 60, new HashMap<>());
        when(showRepository.findByShowNumber("SH000123")).thenReturn(show);

        doNothing().when(showService).updateShowSeatAvailability(show, booking.getSeats(), true);

        bookingService.cancelBooking(ticketNumber, "+1234567890");

        verify(showService, times(1)).updateShowSeatAvailability(show, booking.getSeats(), true);
        verify(bookingRepository, times(2)).delete(ticketNumber);
    }
}