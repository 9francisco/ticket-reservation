# Ticket Reservation System

A simple Ticket Reservation System implemented in Java Spring Boot using Gradle as the build tool. This system allows buyers to check show's availability, book seats, and cancel bookings. It also provides admin members the ability to configure shows and view buyer bookings.

## Table of Contents

- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Code Structure](#code-structure)

## Requirements

- Java 17 or higher
- Spring Boot 3.2.x
- Gradle for building the project

## Getting Started

1. Clone the repository to your local machine:

```bash
git clone https://github.com/9francisco/ticket-reservation.git
```

2. Navigate to the project directory:

```bash
cd ticket-reservation
```

## Running the Application

To run the Spring Boot application using the executable JAR file, follow these steps:

1. Build the application by running the following Gradle command:

   ```bash
   ./gradlew build
   ```

   After a successful build, you can find the JAR file in the `build/libs` directory.

2. Open a terminal and navigate to the `build/libs` directory:

   ```bash
   cd build/libs
   ```

3. Run the application using the `java` command with the JAR file. Replace `ticket-reservation-0.0.1-SNAPSHOT.jar` with the actual name of your JAR file:

   ```bash
   java -jar ticket-reservation-0.0.1-SNAPSHOT.jar
   ```

   This will start both the Spring Boot application and the Command Line Interface (CLI).

> Note: Make sure you have Java 17 or higher installed on your system before running the application.

## Usage

### Buyer Commands

Buyers can perform the following commands using CLI:

- `availability <showNumber>`: Check available seats for a specific show.
- `book <showNumber> <phoneNumber> <commaSeparatedSelectedSeats>`: Book seats on a show. Example: `book SH001 +123456789 B2,C3`
- `cancel <ticketNumber> <phoneNumber>`: Cancel a booking using the ticket number and phone number.

In addition to the CLI, buyers can perform the following commands using HTTP requests. Examples:
- **Check available seats**:
  - `GET /api/availability/SH001`
- **Book seats**:
  - `POST /api/book/SH001?phoneNumber=+123456&selectedSeats=B1,B2`
- **Cancel booking**:
  - `DELETE /api/cancel?ticketNumber=TK000001&phoneNumber=+123456`
    
### Admin Commands

Admin members can perform the following commands using CLI:

- `setup <showNumber> <numberOfRows> <seatsPerRow> <cancelWindowInMinutes>`: Configure a new show.
- `view <showNumber>`: Display show details, including booked seats.

In addition to the CLI, admin can perform the following commands using HTTP requests. Examples:
- **Configure a new show**:
  - `POST /api/setup`

    ```json
    {
      "showNumber": "SH001",
      "numberOfRows": 10,
      "seatsPerRow": 6,
      "cancelWindowInMinutes": 30
    }
    ```

- **View show details, including booked seats**:
  - `GET /api/view/SH001`

## Code Structure

The project is structured as follows:

- `src/main/java/com/example/ticketreservation`: Contains Java source code.
  - `controller` Contains controller classes for handling HTTP requests.
  - `model`: Contains model classes representing Show and Booking entities.
  - `repository`: Contains repository classes for managing data.
  - `service`: Contains service classes for implementing business logic.
  - `exception`: Contains the GlobalExceptionHandler class for handling exceptions (error responses).
- `src/main/resources`: Contains application configuration files and templates.