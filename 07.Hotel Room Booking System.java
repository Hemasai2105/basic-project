
// "Hotel Room Booking System 
// Develop a booking system where: 
// • Rooms can be booked and canceled  
// • Show available rooms  
// • Store customer details  
// Constraints: 
// • Avoid double booking  
// • Room categories (AC, Non-AC) "
import java.util.*;
public class HotelBooking {
    enum RoomType { AC, NON_AC }
    static class Room {
        int number;
        RoomType type;
        double pricePerNight;
        boolean booked;
        String guestName;
        String guestPhone;
        int nights;
        Room(int number, RoomType type, double price) {
            this.number = number;
            this.type = type;
            this.pricePerNight = price;
            this.booked = false;
        }
        void display() {
            System.out.printf("Room %-4d | %-6s | ₹%-8.0f/night | %s%n",
                number, type, pricePerNight,
                booked ? "BOOKED (Guest: " + guestName + ", " + nights + " nights)" : "AVAILABLE");
        }
    }

    static List<Room> rooms = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        initRooms();
        while (true) {
            System.out.println("\n===== Hotel Room Booking System =====");
            System.out.println("1. View All Rooms");
            System.out.println("2. View Available Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Guest Details");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            switch (getInt()) {
                case 1 -> displayRooms(rooms);
                case 2 -> displayAvailable();
                case 3 -> bookRoom();
                case 4 -> cancelBooking();
                case 5 -> guestDetails();
                case 6 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void initRooms() {
        // AC rooms: 101-105, Non-AC: 201-205
        for (int i = 1; i <= 5; i++) rooms.add(new Room(100 + i, RoomType.AC, 2500));
        for (int i = 1; i <= 5; i++) rooms.add(new Room(200 + i, RoomType.NON_AC, 1200));
    }

    static void displayRooms(List<Room> list) {
        System.out.println("--- Room Status ---");
        for (Room r : list) r.display();
    }

    static void displayAvailable() {
        List<Room> avail = new ArrayList<>();
        for (Room r : rooms) if (!r.booked) avail.add(r);
        if (avail.isEmpty()) System.out.println("No rooms available.");
        else displayRooms(avail);
    }

    static void bookRoom() {
        System.out.println("Room Type: 1. AC (₹2500/night)   2. Non-AC (₹1200/night)");
        System.out.print("Choose: ");
        int type = getInt();
        RoomType rt = type == 1 ? RoomType.AC : type == 2 ? RoomType.NON_AC : null;
        if (rt == null) { System.out.println("Invalid type."); return; }

        Room selected = null;
        for (Room r : rooms) if (r.type == rt && !r.booked) { selected = r; break; }
        if (selected == null) { System.out.println("No " + rt + " rooms available."); return; }

        System.out.println("Room " + selected.number + " is available.");
        System.out.print("Guest Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        System.out.print("Phone Number: ");
        String phone = sc.nextLine().trim();
        System.out.print("Number of nights: ");
        int nights = getPositiveInt();

        selected.booked = true;
        selected.guestName = name;
        selected.guestPhone = phone;
        selected.nights = nights;

        double total = selected.pricePerNight * nights;
        System.out.println("\n=== Booking Confirmed ===");
        System.out.println("Room      : " + selected.number + " (" + selected.type + ")");
        System.out.println("Guest     : " + name);
        System.out.println("Phone     : " + phone);
        System.out.println("Nights    : " + nights);
        System.out.printf("Total Bill: ₹%.2f%n", total);
    }

    static void cancelBooking() {
        System.out.print("Enter Room Number to cancel: ");
        int num = getInt();
        Room r = findRoom(num);
        if (r == null) { System.out.println("Room not found."); return; }
        if (!r.booked) { System.out.println("Room is not booked."); return; }
        System.out.println("Cancelling booking for: " + r.guestName);
        r.booked = false;
        r.guestName = "";
        r.guestPhone = "";
        r.nights = 0;
        System.out.println("Booking cancelled successfully.");
    }

    static void guestDetails() {
        System.out.print("Enter Room Number: ");
        int num = getInt();
        Room r = findRoom(num);
        if (r == null) { System.out.println("Room not found."); return; }
        if (!r.booked) { System.out.println("Room is not booked."); return; }
        System.out.println("Room   : " + r.number + " (" + r.type + ")");
        System.out.println("Guest  : " + r.guestName);
        System.out.println("Phone  : " + r.guestPhone);
        System.out.println("Nights : " + r.nights);
        System.out.printf("Bill   : ₹%.2f%n", r.pricePerNight * r.nights);
    }

    static Room findRoom(int num) {
        for (Room r : rooms) if (r.number == num) return r;
        return null;
    }

    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }

    static int getPositiveInt() {
        while (true) {
            int v = getInt();
            if (v > 0) return v;
            System.out.print("Must be positive: ");
        }
    }
}
