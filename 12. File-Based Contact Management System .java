// 12. File-Based Contact Management System 
// Develop a contact system that: 
// • Stores contacts in a file  
// • Add, delete, search contacts  
// • Display all contacts  
// Constraints: 
// • Use file handling  
// • Persistent storage 
import java.util.*;
import java.io.*;

public class ContactManagement {

    static final String FILE_NAME = "contacts.txt";
    static List<String[]> contacts = new ArrayList<>(); // [name, phone, email]
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadContacts();
        System.out.println("Contacts loaded from file.");

        while (true) {
            System.out.println("\n===== Contact Management System =====");
            System.out.println("1. Add Contact");
            System.out.println("2. Display All Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Update Contact");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            switch (getInt()) {
                case 1 -> addContact();
                case 2 -> displayAll();
                case 3 -> searchContact();
                case 4 -> deleteContact();
                case 5 -> updateContact();
                case 6 -> { saveContacts(); System.out.println("Contacts saved. Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addContact() {
        System.out.print("Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }

        // Check for duplicate
        for (String[] c : contacts) {
            if (c[0].equalsIgnoreCase(name)) { System.out.println("Contact already exists!"); return; }
        }

        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();
        if (!phone.matches("[0-9+\\-\\s]{7,15}")) {
            System.out.println("Invalid phone number format.");
            return;
        }

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        contacts.add(new String[]{name, phone, email});
        saveContacts();
        System.out.println("Contact added and saved.");
    }

    static void displayAll() {
        if (contacts.isEmpty()) { System.out.println("No contacts found."); return; }
        System.out.printf("%-5s %-20s %-15s %s%n", "#", "Name", "Phone", "Email");
        System.out.println("-".repeat(65));
        for (int i = 0; i < contacts.size(); i++) {
            String[] c = contacts.get(i);
            System.out.printf("%-5d %-20s %-15s %s%n", i + 1, c[0], c[1], c[2]);
        }
    }

    static void searchContact() {
        System.out.print("Enter name or phone to search: ");
        sc.nextLine();
        String kw = sc.nextLine().trim().toLowerCase();
        boolean found = false;
        for (String[] c : contacts) {
            if (c[0].toLowerCase().contains(kw) || c[1].contains(kw)) {
                if (!found) {
                    System.out.printf("%-20s %-15s %s%n", "Name", "Phone", "Email");
                    System.out.println("-".repeat(55));
                    found = true;
                }
                System.out.printf("%-20s %-15s %s%n", c[0], c[1], c[2]);
            }
        }
        if (!found) System.out.println("No contact found.");
    }

    static void deleteContact() {
        displayAll();
        System.out.print("Enter contact # to delete: ");
        int idx = getInt() - 1;
        if (idx < 0 || idx >= contacts.size()) { System.out.println("Invalid number."); return; }
        System.out.println("Deleting: " + contacts.get(idx)[0]);
        contacts.remove(idx);
        saveContacts();
        System.out.println("Contact deleted and saved.");
    }

    static void updateContact() {
        displayAll();
        System.out.print("Enter contact # to update: ");
        int idx = getInt() - 1;
        if (idx < 0 || idx >= contacts.size()) { System.out.println("Invalid number."); return; }
        String[] c = contacts.get(idx);
        System.out.println("Updating: " + c[0]);
        sc.nextLine();
        System.out.print("New Name (current: " + c[0] + ", press Enter to keep): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) c[0] = name;

        System.out.print("New Phone (current: " + c[1] + ", press Enter to keep): ");
        String phone = sc.nextLine().trim();
        if (!phone.isEmpty()) c[1] = phone;

        System.out.print("New Email (current: " + c[2] + ", press Enter to keep): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) c[2] = email;

        saveContacts();
        System.out.println("Contact updated and saved.");
    }

    static void saveContacts() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String[] c : contacts)
                pw.println(c[0] + "|" + c[1] + "|" + c[2]);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    static void loadContacts() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 3);
                if (parts.length == 3) contacts.add(parts);
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
