package com.example.ticketreservation;

import com.example.ticketreservation.model.Show;
import com.example.ticketreservation.service.BookingService;
import com.example.ticketreservation.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@Profile("!test")
public class TicketReservationApplication implements CommandLineRunner {

    private final ShowService showService;
    private final BookingService bookingService;

    @Autowired
    public TicketReservationApplication(ShowService showService, BookingService bookingService) {
        this.showService = showService;
        this.bookingService = bookingService;
    }

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(TicketReservationApplication.class, args);
    }

    @Override
    public void run(String... args) {
        mainMenu();
    }

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome to the Ticket Reservation System");
            System.out.println("=== Main Menu ===");
            System.out.println("1 Admin");
            System.out.println("2 Buyer");
            System.out.println("3 Exit");
            System.out.print("Enter menu selection: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    adminMenu();
                    break;
                case "2":
                    buyerMenu();
                    break;
                case "3":
                    System.out.println("Exiting program.");
                    running = false;
                    break;
                default:
                    System.err.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }
    }

    private void adminMenu() {
        System.out.println("\n--- Admin Mode ---");
        System.out.println("Available commands:");
        System.out.println("setup <showNumber> <numberOfRows> <seatsPerRow> <cancelWindowInMinutes>");
        System.out.println("view <showNumber>");
        System.out.println("'back' to return to main menu.");
        System.out.println("Enter command: ");

        while (true) {
            String command = scanner.nextLine();
            if ("back".equalsIgnoreCase(command)) {
                return; // Return to the main menu
            }
            processAdminCommand(command);
            System.out.println("Enter command: ");
        }
    }

    private void buyerMenu() {
        System.out.println("\n--- Buyer Mode ---");
        System.out.println("Available commands:");
        System.out.println("availability <showNumber>");
        System.out.println("book <showNumber> <phoneNumber> <commaSeparatedSelectedSeats>");
        System.out.println("cancel <ticketNumber> <phoneNumber>");
        System.out.println("'back' to return to main menu.");
        System.out.println("Enter command: ");

        while (true) {
            String command = scanner.nextLine();
            if ("back".equalsIgnoreCase(command)) {
                return; // Return to the main menu
            }
            processBuyerCommand(command);
            System.out.println("Enter command: ");
        }
    }

    private void processAdminCommand(String command) {
        String[] parts = command.split("\\s+");
        try {
            switch (parts[0].toLowerCase()) {
                case "setup":
                    if (parts.length != 5) {
                        System.err.println("Invalid 'setup' command format. Expected format: setup <showNumber> <numberOfRows> <seatsPerRow> <cancelWindowInMinutes>");
                        return;
                    }
                    showService.configureShow(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
                    System.out.println("Show configured successfully.");
                    break;
                case "view":
                    if (parts.length != 2) {
                        System.err.println("Invalid 'view' command format. Expected format: view <showNumber>");
                        return;
                    }
                    List<String> details = showService.displayShowDetails(parts[1]);
                    for (String detail : details) {
                        System.out.println(detail);
                    }
                    break;
                default:
                    System.err.println("Invalid command for admin. Available commands are 'setup' and 'view'.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error processing command: " + e.getMessage());
        }
    }

    private void processBuyerCommand(String command) {
        String[] parts = command.split("\\s+");
        try {
            switch (parts[0].toLowerCase()) {
                case "availability":
                    if (parts.length != 2) {
                        System.err.println("Invalid 'availability' command format. Expected format: availability <showNumber>");
                        return;
                    }
                    List<String> availableSeats = bookingService.checkAvailableSeats(parts[1]);
                    System.out.println("Available seats: " + String.join(", ", availableSeats));
                    break;
                case "book":
                    if (parts.length != 4) {
                        System.err.println("Invalid 'book' command format. Expected format: book <showNumber> <phoneNumber> <commaSeparatedSelectedSeats>");
                        return;
                    }
                    List<String> selectedSeats = Arrays.asList(parts[3].split(","));
                    String ticketNumber = bookingService.bookSeats(parts[1], parts[2], selectedSeats);
                    Show show = showService.getShowDetails(parts[1]);
                    int cancellationWindowMinutes = show != null ? show.getCancelWindowInMinutes() : 0;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault());

                    // Print booking details
                    System.out.println("Booking successful. Ticket Number: " + ticketNumber);
                    System.out.println("Booking Creation Time: " + formatter.format(Instant.now()));
                    System.out.println("Reminder: Cancellation is allowed " + cancellationWindowMinutes + " minutes only after booking.");
                    break;
                case "cancel":
                    if (parts.length != 3) {
                        System.err.println("Invalid 'cancel' command format. Expected format: cancel <ticketNumber> <phoneNumber>");
                        return;
                    }
                    bookingService.cancelBooking(parts[1].toUpperCase(), parts[2]);
                    break;
                default:
                    System.err.println("Invalid command for buyer. Available commands are 'availability', 'book', and 'cancel'.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error processing command: " + e.getMessage());
        }
    }
}
