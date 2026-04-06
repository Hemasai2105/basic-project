/******************************************************************************

Problem Statement

Title: Hotel Reservation System in Java

Question:

Design and implement a Hotel Reservation System using Java. The system should allow users to manage hotel operations such as guest registration, room booking, check-in, check-out, and billing.

The program must include the following features:

Register a new guest with details like name, phone, email, and address.
Display all available rooms with details such as room number, type, price, and capacity.
Search for available rooms based on check-in date, check-out date, and room type.
Book a room by providing guest ID, room number, dates, and number of guests.
Perform check-in and check-out operations.
Generate invoice including room charges, GST (18%), advance payment, and balance amount.
Cancel bookings with partial refund.
View guest details along with booking history.
Display all bookings.
View detailed information of a specific room.
Show hotel statistics such as total rooms, bookings, and revenue.
Search guest by name or phone number.

The system should be menu-driven and continue running until the user chooses to exit.

*******************************************************************************/
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

// ─────────────────────────────────────────────
//  ENUMS
// ─────────────────────────────────────────────
enum RoomType {
    STANDARD, DELUXE, SUITE
}

enum RoomStatus {
    AVAILABLE, BOOKED, MAINTENANCE
}

enum BookingStatus {
    CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
}

// ─────────────────────────────────────────────
//  PERSON (Abstract Base Class)
// ─────────────────────────────────────────────
abstract class Person {
    protected String name;
    protected String phone;
    protected String email;

    public Person(String name, String phone, String email) {
        this.name  = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName()  { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    public abstract void displayInfo();
}

// ─────────────────────────────────────────────
//  GUEST
// ─────────────────────────────────────────────
class Guest extends Person {
    private static int counter = 1000;
    private String guestId;
    private String address;
    private List<String> bookingIds;

    public Guest(String name, String phone, String email, String address) {
        super(name, phone, email);
        this.guestId    = "G" + (++counter);
        this.address    = address;
        this.bookingIds = new ArrayList<>();
    }

    public String getGuestId()          { return guestId; }
    public String getAddress()          { return address; }
    public List<String> getBookingIds() { return bookingIds; }
    public void addBookingId(String id) { bookingIds.add(id); }

    @Override
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.printf ("│  Guest ID   : %-27s│%n", guestId);
        System.out.printf ("│  Name       : %-27s│%n", name);
        System.out.printf ("│  Phone      : %-27s│%n", phone);
        System.out.printf ("│  Email      : %-27s│%n", email);
        System.out.printf ("│  Address    : %-27s│%n", address);
        System.out.printf ("│  Bookings   : %-27s│%n", bookingIds.size());
        System.out.println("└─────────────────────────────────────────┘");
    }
}

// ─────────────────────────────────────────────
//  ROOM
// ─────────────────────────────────────────────
class Room {
    private int roomNumber;
    private RoomType type;
    private RoomStatus status;
    private double pricePerNight;
    private int capacity;
    private List<String> amenities;

    public Room(int roomNumber, RoomType type, double pricePerNight, int capacity) {
        this.roomNumber    = roomNumber;
        this.type          = type;
        this.status        = RoomStatus.AVAILABLE;
        this.pricePerNight = pricePerNight;
        this.capacity      = capacity;
        this.amenities     = new ArrayList<>();
        loadAmenities();
    }

    private void loadAmenities() {
        amenities.add("Wi-Fi");
        amenities.add("AC");
        if (type == RoomType.DELUXE || type == RoomType.SUITE) {
            amenities.add("Mini Bar");
            amenities.add("Flat-Screen TV");
        }
        if (type == RoomType.SUITE) {
            amenities.add("Jacuzzi");
            amenities.add("Living Room");
            amenities.add("Private Balcony");
        }
    }

    public int getRoomNumber()      { return roomNumber; }
    public RoomType getType()       { return type; }
    public RoomStatus getStatus()   { return status; }
    public double getPricePerNight(){ return pricePerNight; }
    public int getCapacity()        { return capacity; }

    public void setStatus(RoomStatus status) { this.status = status; }

    public void displayRoom() {
        String statusIcon = status == RoomStatus.AVAILABLE ? "✓" :
                            status == RoomStatus.BOOKED    ? "✗" : "⚠";
        System.out.printf("│  Room %-4d │ %-9s │ %-12s │ Rs.%-8.0f │ %d persons │ [%s]  │%n",
                roomNumber, type, status, pricePerNight, capacity, statusIcon);
    }

    public void displayFullDetails() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.printf ("│  Room Number: %-27d│%n", roomNumber);
        System.out.printf ("│  Type       : %-27s│%n", type);
        System.out.printf ("│  Status     : %-27s│%n", status);
        System.out.printf ("│  Price/Night: Rs. %-23.2f│%n", pricePerNight);
        System.out.printf ("│  Capacity   : %-27d│%n", capacity);
        System.out.printf ("│  Amenities  : %-27s│%n", String.join(", ", amenities));
        System.out.println("└─────────────────────────────────────────┘");
    }
}

// ─────────────────────────────────────────────
//  BOOKING
// ─────────────────────────────────────────────
class Booking {
    private static int counter = 5000;
    private String bookingId;
    private String guestId;
    private int roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;
    private double totalAmount;
    private double advancePaid;
    private int guests;
    private String specialRequests;
    private LocalDateTime bookingTime;

    public Booking(String guestId, int roomNumber, LocalDate checkIn,
                   LocalDate checkOut, double pricePerNight, int guests, String specialRequests) {
        this.bookingId       = "BK" + (++counter);
        this.guestId         = guestId;
        this.roomNumber      = roomNumber;
        this.checkIn         = checkIn;
        this.checkOut        = checkOut;
        this.status          = BookingStatus.CONFIRMED;
        this.guests          = guests;
        this.specialRequests = specialRequests;
        this.bookingTime     = LocalDateTime.now();
        long nights          = ChronoUnit.DAYS.between(checkIn, checkOut);
        this.totalAmount     = nights * pricePerNight;
        this.advancePaid     = totalAmount * 0.20; // 20% advance
    }

    public String getBookingId()    { return bookingId; }
    public String getGuestId()      { return guestId; }
    public int getRoomNumber()      { return roomNumber; }
    public LocalDate getCheckIn()   { return checkIn; }
    public LocalDate getCheckOut()  { return checkOut; }
    public BookingStatus getStatus(){ return status; }
    public double getTotalAmount()  { return totalAmount; }
    public double getAdvancePaid()  { return advancePaid; }

    public void setStatus(BookingStatus status) { this.status = status; }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public void displayBooking() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         BOOKING CONFIRMATION             ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf ("║  Booking ID    : %-24s║%n", bookingId);
        System.out.printf ("║  Guest ID      : %-24s║%n", guestId);
        System.out.printf ("║  Room Number   : %-24d║%n", roomNumber);
        System.out.printf ("║  Check-In      : %-24s║%n", checkIn);
        System.out.printf ("║  Check-Out     : %-24s║%n", checkOut);
        System.out.printf ("║  Nights        : %-24d║%n", getNights());
        System.out.printf ("║  Guests        : %-24d║%n", guests);
        System.out.printf ("║  Status        : %-24s║%n", status);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf ("║  Total Amount  : Rs. %-20.2f║%n", totalAmount);
        System.out.printf ("║  Advance Paid  : Rs. %-20.2f║%n", advancePaid);
        System.out.printf ("║  Balance Due   : Rs. %-20.2f║%n", (totalAmount - advancePaid));
        if (!specialRequests.isEmpty())
            System.out.printf("║  Special Req   : %-24s║%n", specialRequests);
        System.out.println("╚══════════════════════════════════════════╝");
    }

    public void displaySummary() {
        System.out.printf("│  %-8s │ %-8s │ Room %-4d │ %-11s │ %-11s │ Rs.%-8.0f │ %-12s │%n",
                bookingId, guestId, roomNumber, checkIn, checkOut, totalAmount, status);
    }
}

// ─────────────────────────────────────────────
//  INVOICE
// ─────────────────────────────────────────────
class Invoice {
    public static void generate(Booking booking, Guest guest, Room room) {
        double tax      = booking.getTotalAmount() * 0.18;
        double total    = booking.getTotalAmount() + tax;
        double balance  = total - booking.getAdvancePaid();

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            GRAND PALACE HOTEL                   ║");
        System.out.println("║         TAX INVOICE / BILL OF CHARGES           ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf ("║  Booking ID   : %-33s║%n", booking.getBookingId());
        System.out.printf ("║  Guest        : %-33s║%n", guest.getName());
        System.out.printf ("║  Phone        : %-33s║%n", guest.getPhone());
        System.out.printf ("║  Room         : %d (%s)%-25s║%n",
                room.getRoomNumber(), room.getType(), "");
        System.out.printf ("║  Check-In     : %-33s║%n", booking.getCheckIn());
        System.out.printf ("║  Check-Out    : %-33s║%n", booking.getCheckOut());
        System.out.printf ("║  Nights       : %-33d║%n", booking.getNights());
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf ("║  Room Charges : Rs. %-29.2f║%n", booking.getTotalAmount());
        System.out.printf ("║  GST (18%%)    : Rs. %-29.2f║%n", tax);
        System.out.println("║──────────────────────────────────────────────────║");
        System.out.printf ("║  GRAND TOTAL  : Rs. %-29.2f║%n", total);
        System.out.printf ("║  Advance Paid : Rs. %-29.2f║%n", booking.getAdvancePaid());
        System.out.printf ("║  BALANCE DUE  : Rs. %-29.2f║%n", balance);
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("  Thank you for staying at Grand Palace Hotel!");
    }
}

// ─────────────────────────────────────────────
//  HOTEL  (Core Manager)
// ─────────────────────────────────────────────
class Hotel {
    private String name;
    private Map<Integer, Room>    rooms;
    private Map<String, Guest>    guests;
    private Map<String, Booking>  bookings;
    private DateTimeFormatter     fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Hotel(String name) {
        this.name     = name;
        this.rooms    = new LinkedHashMap<>();
        this.guests   = new LinkedHashMap<>();
        this.bookings = new LinkedHashMap<>();
        initRooms();
    }

    // ── seed rooms ──────────────────────────────
    private void initRooms() {
        // Standard  101-105
        for (int i = 101; i <= 105; i++)
            rooms.put(i, new Room(i, RoomType.STANDARD, 2500, 2));
        // Deluxe    201-203
        for (int i = 201; i <= 203; i++)
            rooms.put(i, new Room(i, RoomType.DELUXE, 5000, 3));
        // Suite     301-302
        rooms.put(301, new Room(301, RoomType.SUITE, 10000, 4));
        rooms.put(302, new Room(302, RoomType.SUITE, 12000, 6));
    }

    // ── helpers ──────────────────────────────────
    private void line(char c, int n) { System.out.println(String.valueOf(c).repeat(n)); }
    private void header(String title) {
        line('═', 48);
        System.out.printf("   %s%n", title);
        line('═', 48);
    }

    // ── 1. REGISTER GUEST ────────────────────────
    public void registerGuest(Scanner sc) {
        header("REGISTER NEW GUEST");
        System.out.print("  Name    : "); String name    = sc.nextLine();
        System.out.print("  Phone   : "); String phone   = sc.nextLine();
        System.out.print("  Email   : "); String email   = sc.nextLine();
        System.out.print("  Address : "); String address = sc.nextLine();

        Guest g = new Guest(name, phone, email, address);
        guests.put(g.getGuestId(), g);
        System.out.println("\n  ✔ Guest registered successfully!");
        System.out.println("  Your Guest ID : " + g.getGuestId());
    }

    // ── 2. VIEW ALL ROOMS ────────────────────────
    public void viewAllRooms() {
        header("ALL ROOMS");
        System.out.println("┌────────────┬───────────┬──────────────┬────────────┬───────────┬───────┐");
        System.out.println("│   Room     │   Type    │    Status    │   Price    │ Capacity  │ Avail │");
        System.out.println("├────────────┼───────────┼──────────────┼────────────┼───────────┼───────┤");
        for (Room r : rooms.values()) r.displayRoom();
        System.out.println("└────────────┴───────────┴──────────────┴────────────┴───────────┴───────┘");
    }

    // ── 3. SEARCH AVAILABLE ROOMS ────────────────
    public void searchAvailableRooms(Scanner sc) {
        header("SEARCH AVAILABLE ROOMS");
        System.out.print("  Check-In  (dd-MM-yyyy) : "); String ci = sc.nextLine();
        System.out.print("  Check-Out (dd-MM-yyyy) : "); String co = sc.nextLine();
        System.out.print("  Room Type (STANDARD/DELUXE/SUITE or ALL) : ");
        String typeFilter = sc.nextLine().toUpperCase();

        LocalDate checkIn, checkOut;
        try {
            checkIn  = LocalDate.parse(ci, fmt);
            checkOut = LocalDate.parse(co, fmt);
        } catch (Exception e) {
            System.out.println("  ✘ Invalid date format."); return;
        }

        System.out.println("\n  Available Rooms:");
        System.out.println("┌────────────┬───────────┬────────────┬───────────┐");
        System.out.println("│   Room     │   Type    │   Price    │ Capacity  │");
        System.out.println("├────────────┼───────────┼────────────┼───────────┤");

        boolean found = false;
        for (Room r : rooms.values()) {
            if (r.getStatus() != RoomStatus.AVAILABLE) continue;
            if (!typeFilter.equals("ALL") && !r.getType().name().equals(typeFilter)) continue;
            if (!isRoomFreeForDates(r.getRoomNumber(), checkIn, checkOut)) continue;
            System.out.printf("│  Room %-4d  │ %-9s │ Rs.%-7.0f │ %d persons │%n",
                    r.getRoomNumber(), r.getType(), r.getPricePerNight(), r.getCapacity());
            found = true;
        }
        if (!found) System.out.println("│         No rooms available for selected dates.        │");
        System.out.println("└────────────┴───────────┴────────────┴───────────┘");
    }

    private boolean isRoomFreeForDates(int roomNo, LocalDate ci, LocalDate co) {
        for (Booking b : bookings.values()) {
            if (b.getRoomNumber() != roomNo) continue;
            if (b.getStatus() == BookingStatus.CANCELLED) continue;
            if (ci.isBefore(b.getCheckOut()) && co.isAfter(b.getCheckIn())) return false;
        }
        return true;
    }

    // ── 4. BOOK ROOM ─────────────────────────────
    public void bookRoom(Scanner sc) {
        header("BOOK A ROOM");
        System.out.print("  Guest ID  : "); String gid = sc.nextLine();
        if (!guests.containsKey(gid)) { System.out.println("  ✘ Guest not found."); return; }

        System.out.print("  Room No   : ");
        int roomNo;
        try { roomNo = Integer.parseInt(sc.nextLine()); }
        catch (NumberFormatException e) { System.out.println("  ✘ Invalid room number."); return; }

        if (!rooms.containsKey(roomNo)) { System.out.println("  ✘ Room not found."); return; }
        Room room = rooms.get(roomNo);

        System.out.print("  Check-In  (dd-MM-yyyy) : "); String ci = sc.nextLine();
        System.out.print("  Check-Out (dd-MM-yyyy) : "); String co = sc.nextLine();

        LocalDate checkIn, checkOut;
        try {
            checkIn  = LocalDate.parse(ci, fmt);
            checkOut = LocalDate.parse(co, fmt);
        } catch (Exception e) { System.out.println("  ✘ Invalid date."); return; }

        if (!checkOut.isAfter(checkIn)) { System.out.println("  ✘ Check-out must be after check-in."); return; }
        if (!isRoomFreeForDates(roomNo, checkIn, checkOut)) { System.out.println("  ✘ Room not available for those dates."); return; }

        System.out.print("  No. of Guests     : ");
        int numGuests;
        try { numGuests = Integer.parseInt(sc.nextLine()); }
        catch (NumberFormatException e) { System.out.println("  ✘ Invalid input."); return; }

        if (numGuests > room.getCapacity()) { System.out.println("  ✘ Exceeds room capacity (" + room.getCapacity() + ")."); return; }

        System.out.print("  Special Requests  : "); String req = sc.nextLine();

        Booking b = new Booking(gid, roomNo, checkIn, checkOut, room.getPricePerNight(), numGuests, req);
        bookings.put(b.getBookingId(), b);
        guests.get(gid).addBookingId(b.getBookingId());
        room.setStatus(RoomStatus.BOOKED);

        b.displayBooking();
        System.out.println("  ✔ Booking confirmed! ID: " + b.getBookingId());
    }

    // ── 5. CHECK-IN ──────────────────────────────
    public void checkIn(Scanner sc) {
        header("CHECK-IN");
        System.out.print("  Booking ID : "); String bid = sc.nextLine();
        if (!bookings.containsKey(bid)) { System.out.println("  ✘ Booking not found."); return; }
        Booking b = bookings.get(bid);
        if (b.getStatus() != BookingStatus.CONFIRMED) {
            System.out.println("  ✘ Cannot check-in. Status: " + b.getStatus()); return;
        }
        b.setStatus(BookingStatus.CHECKED_IN);
        System.out.println("  ✔ Check-In successful for Booking: " + bid);
        System.out.println("  Room " + b.getRoomNumber() + " — Welcome to " + name + "!");
    }

    // ── 6. CHECK-OUT ─────────────────────────────
    public void checkOut(Scanner sc) {
        header("CHECK-OUT");
        System.out.print("  Booking ID : "); String bid = sc.nextLine();
        if (!bookings.containsKey(bid)) { System.out.println("  ✘ Booking not found."); return; }
        Booking b = bookings.get(bid);
        if (b.getStatus() != BookingStatus.CHECKED_IN) {
            System.out.println("  ✘ Cannot check-out. Status: " + b.getStatus()); return;
        }
        b.setStatus(BookingStatus.CHECKED_OUT);
        rooms.get(b.getRoomNumber()).setStatus(RoomStatus.AVAILABLE);

        Guest guest = guests.get(b.getGuestId());
        Room  room  = rooms.get(b.getRoomNumber());
        Invoice.generate(b, guest, room);
    }

    // ── 7. CANCEL BOOKING ────────────────────────
    public void cancelBooking(Scanner sc) {
        header("CANCEL BOOKING");
        System.out.print("  Booking ID : "); String bid = sc.nextLine();
        if (!bookings.containsKey(bid)) { System.out.println("  ✘ Booking not found."); return; }
        Booking b = bookings.get(bid);
        if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.CHECKED_OUT) {
            System.out.println("  ✘ Cannot cancel. Status: " + b.getStatus()); return;
        }
        b.setStatus(BookingStatus.CANCELLED);
        rooms.get(b.getRoomNumber()).setStatus(RoomStatus.AVAILABLE);
        double refund = b.getAdvancePaid() * 0.80;
        System.out.println("  ✔ Booking cancelled.");
        System.out.printf ("  Refund (80%% of advance): Rs. %.2f%n", refund);
    }

    // ── 8. VIEW GUEST INFO ───────────────────────
    public void viewGuestInfo(Scanner sc) {
        header("GUEST INFORMATION");
        System.out.print("  Guest ID : "); String gid = sc.nextLine();
        if (!guests.containsKey(gid)) { System.out.println("  ✘ Guest not found."); return; }
        guests.get(gid).displayInfo();
        System.out.println("  Booking History:");
        for (String bid : guests.get(gid).getBookingIds()) {
            if (bookings.containsKey(bid)) bookings.get(bid).displaySummary();
        }
    }

    // ── 9. ALL BOOKINGS ──────────────────────────
    public void viewAllBookings() {
        header("ALL BOOKINGS");
        if (bookings.isEmpty()) { System.out.println("  No bookings found."); return; }
        System.out.println("┌──────────┬──────────┬──────────┬─────────────┬─────────────┬────────────┬──────────────┐");
        System.out.println("│ Booking  │  Guest   │  Room    │  Check-In   │  Check-Out  │  Amount    │  Status      │");
        System.out.println("├──────────┼──────────┼──────────┼─────────────┼─────────────┼────────────┼──────────────┤");
        for (Booking b : bookings.values()) b.displaySummary();
        System.out.println("└──────────┴──────────┴──────────┴─────────────┴─────────────┴────────────┴──────────────┘");
    }

    // ── 10. ROOM DETAILS ─────────────────────────
    public void viewRoomDetails(Scanner sc) {
        header("ROOM DETAILS");
        System.out.print("  Room Number : ");
        try {
            int rn = Integer.parseInt(sc.nextLine());
            if (!rooms.containsKey(rn)) { System.out.println("  ✘ Room not found."); return; }
            rooms.get(rn).displayFullDetails();
        } catch (NumberFormatException e) { System.out.println("  ✘ Invalid input."); }
    }

    // ── 11. HOTEL STATS ──────────────────────────
    public void showStats() {
        header("HOTEL STATISTICS — " + name);
        long avail   = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count();
        long booked  = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.BOOKED).count();
        long maint   = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.MAINTENANCE).count();
        long confirmed = bookings.values().stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count();
        long checkedIn = bookings.values().stream().filter(b -> b.getStatus() == BookingStatus.CHECKED_IN).count();
        double revenue = bookings.values().stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .mapToDouble(Booking::getTotalAmount).sum();

        System.out.printf("  Total Rooms      : %d%n", rooms.size());
        System.out.printf("  Available        : %d%n", avail);
        System.out.printf("  Booked           : %d%n", booked);
        System.out.printf("  Maintenance      : %d%n", maint);
        System.out.println("  ─────────────────────────────────");
        System.out.printf("  Total Guests     : %d%n", guests.size());
        System.out.printf("  Total Bookings   : %d%n", bookings.size());
        System.out.printf("  Confirmed        : %d%n", confirmed);
        System.out.printf("  Currently In     : %d%n", checkedIn);
        System.out.println("  ─────────────────────────────────");
        System.out.printf("  Total Revenue    : Rs. %.2f%n", revenue);
    }

    // ── 12. SEARCH GUEST ─────────────────────────
    public void searchGuest(Scanner sc) {
        header("SEARCH GUEST");
        System.out.print("  Enter Name / Phone : "); String query = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Guest g : guests.values()) {
            if (g.getName().toLowerCase().contains(query) || g.getPhone().contains(query)) {
                g.displayInfo(); found = true;
            }
        }
        if (!found) System.out.println("  ✘ No guest found matching: " + query);
    }

    public String getName() { return name; }
}

// ─────────────────────────────────────────────
//  MAIN
// ─────────────────────────────────────────────
public class Main {

    static void printBanner(String hotelName) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                                                  ║");
        System.out.println("║         🏨  GRAND PALACE HOTEL  🏨               ║");
        System.out.println("║           Hotel Reservation System               ║");
        System.out.println("║                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("  Welcome to " + hotelName);
        System.out.println("  " + LocalDate.now() + "  |  " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println();
    }

    static void printMenu() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│              MAIN MENU                  │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1.  Register Guest                     │");
        System.out.println("│  2.  View All Rooms                     │");
        System.out.println("│  3.  Search Available Rooms             │");
        System.out.println("│  4.  Book a Room                        │");
        System.out.println("│  5.  Check-In                           │");
        System.out.println("│  6.  Check-Out & Generate Invoice       │");
        System.out.println("│  7.  Cancel Booking                     │");
        System.out.println("│  8.  View Guest Information             │");
        System.out.println("│  9.  View All Bookings                  │");
        System.out.println("│  10. View Room Details                  │");
        System.out.println("│  11. Hotel Statistics                   │");
        System.out.println("│  12. Search Guest                       │");
        System.out.println("│  0.  Exit                               │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("  Enter choice : ");
    }

    public static void main(String[] args) {
        Scanner sc    = new Scanner(System.in);
        Hotel hotel   = new Hotel("Grand Palace Hotel, Chennai");

        printBanner(hotel.getName());

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1"  -> hotel.registerGuest(sc);
                case "2"  -> hotel.viewAllRooms();
                case "3"  -> hotel.searchAvailableRooms(sc);
                case "4"  -> hotel.bookRoom(sc);
                case "5"  -> hotel.checkIn(sc);
                case "6"  -> hotel.checkOut(sc);
                case "7"  -> hotel.cancelBooking(sc);
                case "8"  -> hotel.viewGuestInfo(sc);
                case "9"  -> hotel.viewAllBookings();
                case "10" -> hotel.viewRoomDetails(sc);
                case "11" -> hotel.showStats();
                case "12" -> hotel.searchGuest(sc);
                case "0"  -> {
                    System.out.println("  Thank you for using Grand Palace Hotel System!");
                    System.out.println("  Goodbye! 👋");
                    sc.close();
                    return;
                }
                default -> System.out.println("  ✘ Invalid choice. Please try again.");
            }
        }
    }
}
