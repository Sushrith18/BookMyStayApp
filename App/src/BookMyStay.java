import java.util.*;

/**
 * Book My Stay App - Use Case 7
 * Demonstrates add-on services mapped to reservations
 *
 * @author Sushrith
 * @version 7.1
 */

// Service class (Add-On)
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (Service s : services) {
            System.out.println("- " + s.getServiceName() + " (₹" + s.getCost() + ")");
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);
        double total = 0;

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v7.1\n");

        // Assume reservation IDs from Use Case 6
        String reservation1 = "SI_1234";
        String reservation2 = "SU_5678";

        // Initialize manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 500);
        Service wifi = new Service("WiFi", 200);
        Service spa = new Service("Spa", 1500);

        // Add services to reservations
        manager.addService(reservation1, breakfast);
        manager.addService(reservation1, wifi);

        manager.addService(reservation2, spa);

        // Display services
        manager.displayServices(reservation1);
        System.out.println("Total Add-On Cost: ₹" + manager.calculateTotalCost(reservation1));

        manager.displayServices(reservation2);
        System.out.println("Total Add-On Cost: ₹" + manager.calculateTotalCost(reservation2));
    }
}