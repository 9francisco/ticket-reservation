package com.example.ticketreservation.service;

import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.repository.BookingRepository;
import com.example.ticketreservation.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
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
        String showNumber = "SH000123";
        doNothing().when(showRepository).save(any(Show.class));
        showService.configureShow(showNumber, 10, 5, 60);

        verify(showRepository, times(1)).save(any(Show.class));
    }

    @Test
    void testDisplayShowDetails() {
        String showNumber = "SH000123";
        Show show = new Show(showNumber, 10, 5, 60, new HashMap<>());

        when(showRepository.findByShowNumber(showNumber)).thenReturn(show);

        showService.displayShowDetails(showNumber);

        verify(showRepository, times(1)).findByShowNumber(showNumber);
    }
}
