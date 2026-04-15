// "Library Management System 
// Develop a system to manage books in a library. 
// Features: 
// • Add books (Book ID, Title, Author)  
// • Issue book to a user  
// • Return book  
// • Display available books  
// • Search by title or author  
// Constraints: 
// • A book cannot be issued if already issued  
// • Maintain issue status  "
import java.util.*;
public class LibraryManagement {
    static class Book {
        int id;
        String title;
        String author;
        boolean issued;
        String issuedTo;
        Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.issued = false;
            this.issuedTo = "";
        }
        void display() {
            System.out.printf("%-5d %-30s %-20s %-10s %s%n",
                id, title, author, issued ? "Issued" : "Available", issued ? "(To: " + issuedTo + ")" : "");
        }
    }
    static List<Book> books = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Display Available Books");
            System.out.println("6. Search by Title");
            System.out.println("7. Search by Author");
            System.out.println("8. Exit");
            System.out.print("Choose: ");
            switch (getInt()) {
                case 1 -> addBook();
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> displayAll(books);
                case 5 -> displayAvailable();
                case 6 -> searchByTitle();
                case 7 -> searchByAuthor();
                case 8 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    static void addBook() {
        System.out.print("Book ID: ");
        int id = getInt();
        for (Book b : books) if (b.id == id) { System.out.println("Book ID already exists!"); return; }
        System.out.print("Title: ");
        sc.nextLine();
        String title = sc.nextLine().trim();
        System.out.print("Author: ");
        String author = sc.nextLine().trim();
        books.add(new Book(id, title, author));
        System.out.println("Book added successfully!");
    }
    static void issueBook() {
        System.out.print("Enter Book ID to issue: ");
        int id = getInt();
        Book b = findById(id);
        if (b == null) { System.out.println("Book not found."); return; }
        if (b.issued) { System.out.println("Book already issued to: " + b.issuedTo); return; }
        System.out.print("Issue to (name): ");
        sc.nextLine();
        b.issuedTo = sc.nextLine().trim();
        b.issued = true;
        System.out.println("Book issued to " + b.issuedTo + " successfully.");
    }
    static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int id = getInt();
        Book b = findById(id);
        if (b == null) { System.out.println("Book not found."); return; }
        if (!b.issued) { System.out.println("Book was not issued."); return; }
        System.out.println("Book returned from: " + b.issuedTo);
        b.issued = false;
        b.issuedTo = "";
    }
    static void displayAll(List<Book> list) {
        if (list.isEmpty()) { System.out.println("No books found."); return; }
        System.out.printf("%-5s %-30s %-20s %-10s%n", "ID", "Title", "Author", "Status");
        System.out.println("-".repeat(70));
        for (Book b : list) b.display();
    }
    static void displayAvailable() {
        List<Book> avail = new ArrayList<>();
        for (Book b : books) if (!b.issued) avail.add(b);
        if (avail.isEmpty()) System.out.println("No books available.");
        else displayAll(avail);
    }
    static void searchByTitle() {
        System.out.print("Enter title keyword: ");
        sc.nextLine();
        String kw = sc.nextLine().toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book b : books) if (b.title.toLowerCase().contains(kw)) result.add(b);
        if (result.isEmpty()) System.out.println("No books found.");
        else displayAll(result);
    }
    static void searchByAuthor() {
        System.out.print("Enter author name: ");
        sc.nextLine();
        String kw = sc.nextLine().toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book b : books) if (b.author.toLowerCase().contains(kw)) result.add(b);
        if (result.isEmpty()) System.out.println("No books found.");
        else displayAll(result);
    }
    static Book findById(int id) {
        for (Book b : books) if (b.id == id) return b;
        return null;
    }
    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
