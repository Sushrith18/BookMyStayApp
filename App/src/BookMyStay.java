import java.util.*;

/**
 * Book My Stay App - Use Case 11
 * Demonstrates thread-safe booking using synchronization
 *
 * @author Sushrith
 * @version 11.1
 */

// Reservation
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

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
        System.out.println(Thread.currentThread().getName() +
                " added request for " + r.getGuestName());
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Thread-safe Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            Reservation r;

            // Critical section: fetch request
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            // Critical section: allocate room
            synchronized (inventory) {
                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println(getName() + " CONFIRMED booking for "
                            + r.getGuestName() + " (" + r.getRoomType() + ")");
                } else {
                    System.out.println(getName() + " FAILED booking for "
                            + r.getGuestName() + " (" + r.getRoomType() + ")");
                }
            }
        }
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v11.1\n");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Double Room"));

        // Create multiple threads
        Thread t1 = new BookingProcessor("Thread-1", queue, inventory);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventory);

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory
        inventory.displayInventory();
    }
}