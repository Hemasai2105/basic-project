// Railway Reservation System 
// Build a reservation system where: 
// • Users can book tickets  
// • Cancel tickets  
// • View seat availability  
// Constraints: 
// • Limited seats per train  
// • Generate ticket ID  
import java.util.*;
public class RailwayReservation {
    static class Train {
        int trainNo;
        String name;
        String from;
        String to;
        int totalSeats;
        boolean[] seats;
        Map<String, Integer> bookings = new HashMap<>(); // ticketId -> seatNo
        int ticketCounter = 1000;
        Train(int no, String name, String from, String to, int seats) {
            this.trainNo = no;
            this.name = name;
            this.from = from;
            this.to = to;
            this.totalSeats = seats;
            this.seats = new boolean[seats + 1]; // 1-indexed
        }
        int availableSeats() {
            int count = 0;
            for (int i = 1; i <= totalSeats; i++) if (!seats[i]) count++;
            return count;
        }
        String bookTicket(String passengerName) {
            for (int i = 1; i <= totalSeats; i++) {
                if (!seats[i]) {
                    seats[i] = true;
                    String ticketId = "TKT" + (++ticketCounter);
                    bookings.put(ticketId, i);
                    System.out.println("\n=== Ticket Booked ===");
                    System.out.println("Ticket ID  : " + ticketId);
                    System.out.println("Passenger  : " + passengerName);
                    System.out.println("Train      : " + trainNo + " - " + name);
                    System.out.println("Route      : " + from + " → " + to);
                    System.out.println("Seat No    : " + i);
                    return ticketId;
                }
            }
            return null;
        }
        boolean cancelTicket(String ticketId) {
            if (!bookings.containsKey(ticketId)) return false;
            int seat = bookings.remove(ticketId);
            seats[seat] = false;
            return true;
        }
        void showSeats() {
            System.out.println("Seat layout for Train " + trainNo + " - " + name + ":");
            for (int i = 1; i <= totalSeats; i++) {
                System.out.printf("[%2d:%s] ", i, seats[i] ? "X" : "O");
                if (i % 8 == 0) System.out.println();
            }
            System.out.println();
            System.out.println("O=Available, X=Booked | Available: " + availableSeats() + "/" + totalSeats);
        }
    }
    static List<Train> trains = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        loadTrains();
        while (true) {
            System.out.println("\n===== Railway Reservation System =====");
            System.out.println("1. View All Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Check Seat Availability");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            switch (getInt()) {
                case 1 -> viewTrains();
                case 2 -> bookTicket();
                case 3 -> cancelTicket();
                case 4 -> checkSeats();
                case 5 -> { System.out.println("Have a safe journey!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    static void viewTrains() {
        System.out.printf("%-8s %-20s %-12s %-12s %-10s%n", "Train No", "Name", "From", "To", "Available");
        System.out.println("-".repeat(65));
        for (Train t : trains)
            System.out.printf("%-8d %-20s %-12s %-12s %d/%d%n",
                t.trainNo, t.name, t.from, t.to, t.availableSeats(), t.totalSeats);
    }
    static void bookTicket() {
        viewTrains();
        System.out.print("Enter Train Number: ");
        int no = getInt();
        Train t = findTrain(no);
        if (t == null) { System.out.println("Train not found."); return; }
        if (t.availableSeats() == 0) { System.out.println("No seats available!"); return; }
        System.out.print("Passenger Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        String id = t.bookTicket(name);
        if (id == null) System.out.println("Booking failed.");
    }
    static void cancelTicket() {
        System.out.print("Enter Train Number: ");
        int no = getInt();
        Train t = findTrain(no);
        if (t == null) { System.out.println("Train not found."); return; }
        System.out.print("Enter Ticket ID: ");
        sc.nextLine();
        String ticketId = sc.nextLine().trim().toUpperCase();
        if (t.cancelTicket(ticketId)) System.out.println("Ticket " + ticketId + " cancelled successfully.");
        else System.out.println("Ticket not found.");
    }
    static void checkSeats() {
        System.out.print("Enter Train Number: ");
        int no = getInt();
        Train t = findTrain(no);
        if (t == null) { System.out.println("Train not found."); return; }
        t.showSeats();
    }
    static void loadTrains() {
        trains.add(new Train(12345, "Chennai Express", "Chennai", "Mumbai", 40));
        trains.add(new Train(22222, "Rajdhani", "Delhi", "Kolkata", 32));
        trains.add(new Train(10101, "Shatabdi", "Bangalore", "Hyderabad", 48));
    }
    static Train findTrain(int no) {
        for (Train t : trains) if (t.trainNo == no) return t;
        return null;
    }
    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
