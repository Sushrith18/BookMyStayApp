import java.util.*;

/**
 * Book My Stay App - Use Case 10
 * Demonstrates booking cancellation with rollback using Stack (LIFO)
 *
 * @author Sushrith
 * @version 10.1
 */

// Reservation class
class Reservation {
    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public void increaseAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking History (tracks confirmed bookings)
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addBooking(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation getBooking(String reservationId) {
        return bookings.get(reservationId);
    }

    public void removeBooking(String reservationId) {
        bookings.remove(reservationId);
    }
}

// Cancellation Service
class CancellationService {

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) {

        System.out.println("\nProcessing cancellation for ID: " + reservationId);

        // Validate existence
        Reservation reservation = history.getBooking(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        // Push to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increaseAvailability(reservation.getRoomType());

        // Remove booking from history
        history.removeBooking(reservationId);

        // Confirm cancellation
        System.out.println("Cancellation Successful for ID: " + reservationId);
    }

    // Display rollback stack
    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v10.1\n");

        // Initialize services
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        // Simulate confirmed bookings
        history.addBooking(new Reservation("SI_1234", "Single Room"));
        history.addBooking(new Reservation("SU_5678", "Suite Room"));

        // Display initial inventory
        inventory.displayInventory();

        // Cancel valid booking
        cancellationService.cancelBooking("SI_1234", history, inventory);

        // Attempt invalid cancellation
        cancellationService.cancelBooking("XX_0000", history, inventory);

        // Display updated inventory
        inventory.displayInventory();

        // Show rollback stack
        cancellationService.displayRollbackStack();
    }
}