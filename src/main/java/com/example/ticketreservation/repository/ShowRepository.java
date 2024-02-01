package com.example.ticketreservation.repository;

import com.example.ticketreservation.model.Show;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory repository for managing Show objects.
 */
@Repository
public class ShowRepository {

    private final Map<String, Show> shows = new HashMap<>();

    /**
     * Saves a show in the repository.
     *
     * @param show The show to save.
     */
    public void save(Show show) {
        shows.put(show.getShowNumber(), show);
    }

    /**
     * Retrieves a show by its show number.
     *
     * @param showNumber The show number to search for.
     * @return The show if found, or null otherwise.
     */
    public Show findByShowNumber(String showNumber) {
        return shows.get(showNumber);
    }

}
