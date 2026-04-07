import java.util.*;

/**
 * Book My Stay App - Use Case 9
 * Demonstrates validation and error handling using custom exceptions
 *
 * @author Sushrith
 * @version 9.1
 */

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reduceAvailability(String roomType) throws InvalidBookingException {
        int available = inventory.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }

        inventory.put(roomType, available - 1);
    }
}

// Validator class
class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected");
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available");
        }
    }
}

// Booking Service
class BookingService {

    public void processBooking(Reservation reservation, RoomInventory inventory) {

        try {
            // Validate first (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Reduce inventory (safe operation)
            inventory.reduceAvailability(reservation.getRoomType());

            // Confirm booking
            System.out.println("Booking Confirmed for " + reservation.getGuestName()
                    + " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v9.1\n");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Test cases

        // Valid booking
        service.processBooking(new Reservation("Alice", "Single Room"), inventory);

        // Invalid room type
        service.processBooking(new Reservation("Bob", "Luxury Room"), inventory);

        // No availability
        service.processBooking(new Reservation("Charlie", "Suite Room"), inventory);

        // Empty name
        service.processBooking(new Reservation("", "Double Room"), inventory);
    }
}