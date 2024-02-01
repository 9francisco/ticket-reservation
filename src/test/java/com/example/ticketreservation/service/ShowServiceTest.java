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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Tests for ShowService.
 */
public class ShowServiceTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ShowService showService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConfigureShow() {
        String showNumber = "SH0001";
        doNothing().when(showRepository).save(any(Show.class));
        showService.configureShow(showNumber, 10, 5, 60);

        verify(showRepository, times(1)).save(any(Show.class));
    }

    @Test
    void testDisplayShowDetails() {
        String showNumber = "SH0001";
        Show show = new Show(showNumber, 10, 5, 60, new HashMap<>());
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking("TK000001", "+1234567890", showNumber, Arrays.asList("A1", "A2"));
        bookings.add(booking);

        when(showRepository.findByShowNumber(showNumber)).thenReturn(show);
        when(bookingRepository.findByShowNumber(showNumber)).thenReturn(bookings);

        showService.displayShowDetails(showNumber);

        verify(showRepository, times(1)).findByShowNumber(showNumber);
    }
}
