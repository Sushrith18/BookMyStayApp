import java.util.*;

/**
 * Book My Stay App - Use Case 8
 * Demonstrates booking history storage and reporting
 *
 * @author Sushrith
 * @version 8.1
 */

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display history
    public void displayHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// Reporting Service
class BookingReportService {

    // Generate simple report
    public void generateReport(List<Reservation> reservations) {

        System.out.println("\nBooking Report Summary:");

        Map<String, Integer> countByRoom = new HashMap<>();

        for (Reservation r : reservations) {
            countByRoom.put(r.getRoomType(),
                    countByRoom.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : countByRoom.entrySet()) {
            System.out.println(entry.getKey() + " bookings: " + entry.getValue());
        }
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v8.1\n");

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.addReservation(new Reservation("SI_1234", "Alice", "Single Room"));
        history.addReservation(new Reservation("SU_5678", "Bob", "Suite Room"));
        history.addReservation(new Reservation("SI_4321", "Charlie", "Single Room"));

        // Display history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getAllReservations());
    }
}