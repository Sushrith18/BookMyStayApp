import java.io.*;
import java.util.*;

/**
 * Book My Stay App - Use Case 12
 * Demonstrates persistence using serialization and recovery
 *
 * @author Sushrith
 * @version 12.1
 */

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// Wrapper class (to persist full system state)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> bookings;
    RoomInventory inventory;

    public SystemState(List<Reservation> bookings, RoomInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "booking_data.ser";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nData saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("Data loaded successfully!");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No saved data found. Starting fresh...");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return null;
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v12.1\n");

        PersistenceService service = new PersistenceService();

        // Try loading previous state
        SystemState state = service.load();

        List<Reservation> bookings;
        RoomInventory inventory;

        if (state != null) {
            // Restore
            bookings = state.bookings;
            inventory = state.inventory;

            System.out.println("\nRecovered Data:");

        } else {
            // Fresh start
            bookings = new ArrayList<>();
            inventory = new RoomInventory();

            // Add sample data
            bookings.add(new Reservation("SI_1234", "Alice", "Single Room"));
            bookings.add(new Reservation("DO_5678", "Bob", "Double Room"));
        }

        // Display data
        System.out.println("\nBookings:");
        for (Reservation r : bookings) {
            r.display();
        }

        inventory.display();

        // Save state before exit
        SystemState newState = new SystemState(bookings, inventory);
        service.save(newState);
    }
}