// 14. Bus Reservation System 
// Design a system to: 
// • Book bus seats  
// • Cancel reservations  
// • Display seat status  
// Features: 
// • Multiple buses  
// • Seat numbering system  
import java.util.*;

public class BusReservation {

    static class Bus {
        int busNo;
        String route;
        String departure;
        int totalSeats;
        String[] seats; // null = available, name = booked
        Map<String, Integer> bookings = new HashMap<>(); // passengerId -> seatNo
        int idCounter = 100;

        Bus(int no, String route, String departure, int seats) {
            this.busNo = no;
            this.route = route;
            this.departure = departure;
            this.totalSeats = seats;
            this.seats = new String[seats + 1];
        }

        int availableCount() {
            int count = 0;
            for (int i = 1; i <= totalSeats; i++) if (seats[i] == null) count++;
            return count;
        }

        String bookSeat(String name, int seatNo) {
            if (seatNo < 1 || seatNo > totalSeats) return null;
            if (seats[seatNo] != null) return null;
            seats[seatNo] = name;
            String pid = "P" + (++idCounter);
            bookings.put(pid, seatNo);
            return pid;
        }

        boolean cancelSeat(String pid) {
            if (!bookings.containsKey(pid)) return false;
            int seat = bookings.remove(pid);
            seats[seat] = null;
            return true;
        }

        void showSeatMap() {
            System.out.println("Bus " + busNo + " | Route: " + route + " | Departure: " + departure);
            System.out.println("Seat Map ( [No:O]=Available, [No:X]=Booked ):");
            for (int i = 1; i <= totalSeats; i++) {
                System.out.printf("[%2d:%s] ", i, seats[i] == null ? "O" : "X");
                if (i % 4 == 0) System.out.println();
            }
            System.out.println("\nAvailable: " + availableCount() + "/" + totalSeats);
        }
    }

    static List<Bus> buses = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadBuses();
        while (true) {
            System.out.println("\n===== Bus Reservation System =====");
            System.out.println("1. View All Buses");
            System.out.println("2. View Seat Map");
            System.out.println("3. Book Seat");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            switch (getInt()) {
                case 1 -> viewBuses();
                case 2 -> viewSeatMap();
                case 3 -> bookSeat();
                case 4 -> cancelBooking();
                case 5 -> { System.out.println("Have a safe trip!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void viewBuses() {
        System.out.printf("%-8s %-25s %-12s %-10s%n", "Bus No", "Route", "Departure", "Available");
        System.out.println("-".repeat(58));
        for (Bus b : buses)
            System.out.printf("%-8d %-25s %-12s %d/%d%n",
                b.busNo, b.route, b.departure, b.availableCount(), b.totalSeats);
    }

    static void viewSeatMap() {
        System.out.print("Enter Bus Number: ");
        Bus b = findBus(getInt());
        if (b == null) { System.out.println("Bus not found."); return; }
        b.showSeatMap();
    }

    static void bookSeat() {
        viewBuses();
        System.out.print("Enter Bus Number: ");
        Bus b = findBus(getInt());
        if (b == null) { System.out.println("Bus not found."); return; }
        if (b.availableCount() == 0) { System.out.println("No seats available!"); return; }
        b.showSeatMap();
        System.out.print("Passenger Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        System.out.print("Choose Seat Number: ");
        int seat = getInt();
        String pid = b.bookSeat(name, seat);
        if (pid == null) {
            System.out.println("Seat " + seat + " is not available or invalid.");
        } else {
            System.out.println("\n=== Booking Confirmed ===");
            System.out.println("Booking ID : " + pid);
            System.out.println("Passenger  : " + name);
            System.out.println("Bus        : " + b.busNo + " | " + b.route);
            System.out.println("Departure  : " + b.departure);
            System.out.println("Seat No    : " + seat);
        }
    }

    static void cancelBooking() {
        System.out.print("Enter Bus Number: ");
        Bus b = findBus(getInt());
        if (b == null) { System.out.println("Bus not found."); return; }
        System.out.print("Enter Booking ID: ");
        sc.nextLine();
        String pid = sc.nextLine().trim().toUpperCase();
        if (b.cancelSeat(pid)) System.out.println("Booking " + pid + " cancelled.");
        else System.out.println("Booking ID not found.");
    }

    static void loadBuses() {
        buses.add(new Bus(101, "Chennai - Coimbatore", "06:00 AM", 20));
        buses.add(new Bus(202, "Mumbai - Pune", "08:30 AM", 24));
        buses.add(new Bus(303, "Delhi - Agra", "09:00 AM", 16));
    }

    static Bus findBus(int no) {
        for (Bus b : buses) if (b.busNo == no) return b;
        return null;
    }

    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
