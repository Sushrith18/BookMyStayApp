import java.util.LinkedList;
import java.util.Queue;

/**
 * Book My Stay App - Use Case 5
 * Demonstrates booking request handling using FIFO Queue
 *
 * @author Sushrith
 * @version 5.1
 */

// Reservation class (represents a booking request)
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// Booking Queue (FIFO)
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request Added:");
        reservation.display();
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\nCurrent Booking Queue:");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v5.1\n");

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue (FIFO order preserved)
        bookingQueue.displayQueue();
    }
}