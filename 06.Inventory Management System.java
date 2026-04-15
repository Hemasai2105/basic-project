// " Inventory Management System 
// Build a system to manage store inventory: 
// • Add products (ID, Name, Quantity, Price)  
// • Update stock  
// • Remove product  
// • Display inventory  
// Features: 
// • Alert when stock is low  
// • Calculate total inventory value " 
import java.util.*;
public class InventoryManagement {
    static final int LOW_STOCK_THRESHOLD = 5;
    static class Product {
        int id;
        String name;
        int quantity;
        double price;
        Product(int id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
        double totalValue() { return quantity * price; }

        void display() {
            String alert = quantity <= LOW_STOCK_THRESHOLD ? " *** LOW STOCK ***" : "";
            System.out.printf("%-5d %-20s %-10d ₹%-12.2f ₹%-12.2f%s%n",
                id, name, quantity, price, totalValue(), alert);
        }
    }
    static List<Product> inventory = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Inventory Management System =====");
            System.out.println("1. Add Product");
            System.out.println("2. Update Stock");
            System.out.println("3. Remove Product");
            System.out.println("4. Display Inventory");
            System.out.println("5. Low Stock Alert");
            System.out.println("6. Total Inventory Value");
            System.out.println("7. Search Product");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            switch (getInt()) {
                case 1 -> addProduct();
                case 2 -> updateStock();
                case 3 -> removeProduct();
                case 4 -> displayAll();
                case 5 -> showLowStock();
                case 6 -> showTotalValue();
                case 7 -> searchProduct();
                case 8 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    static void addProduct() {
        System.out.print("Product ID: ");
        int id = getInt();
        for (Product p : inventory) if (p.id == id) { System.out.println("Product ID exists!"); return; }
        System.out.print("Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        System.out.print("Quantity: ");
        int qty = getPositiveInt();
        System.out.print("Price per unit: ₹");
        double price = getPositiveDouble();
        inventory.add(new Product(id, name, qty, price));
        System.out.println("Product added!");
        if (qty <= LOW_STOCK_THRESHOLD) System.out.println("Warning: Initial stock is low!");
    }
    static void updateStock() {
        System.out.print("Product ID: ");
        int id = getInt();
        Product p = findById(id);
        if (p == null) { System.out.println("Product not found."); return; }
        System.out.println("1. Add Stock   2. Reduce Stock");
        System.out.print("Choose: ");
        int ch = getInt();
        System.out.print("Quantity: ");
        int qty = getPositiveInt();
        if (ch == 1) {
            p.quantity += qty;
            System.out.println("Stock updated. New qty: " + p.quantity);
        } else if (ch == 2) {
            if (qty > p.quantity) { System.out.println("Cannot reduce below 0. Available: " + p.quantity); return; }
            p.quantity -= qty;
            System.out.println("Stock updated. New qty: " + p.quantity);
            if (p.quantity <= LOW_STOCK_THRESHOLD) System.out.println("*** LOW STOCK ALERT for: " + p.name + " ***");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    static void removeProduct() {
        System.out.print("Product ID to remove: ");
        int id = getInt();
        Iterator<Product> it = inventory.iterator();
        while (it.hasNext()) {
            if (it.next().id == id) { it.remove(); System.out.println("Product removed."); return; }
        }
        System.out.println("Product not found.");
    }
    static void displayAll() {
        if (inventory.isEmpty()) { System.out.println("Inventory is empty."); return; }
        System.out.printf("%-5s %-20s %-10s %-13s %-13s%n", "ID", "Name", "Quantity", "Unit Price", "Total Value");
        System.out.println("-".repeat(65));
        for (Product p : inventory) p.display();
    }
    static void showLowStock() {
        boolean found = false;
        for (Product p : inventory) {
            if (p.quantity <= LOW_STOCK_THRESHOLD) {
                if (!found) {
                    System.out.println("=== LOW STOCK PRODUCTS (qty <= " + LOW_STOCK_THRESHOLD + ") ===");
                    found = true;
                }
                System.out.println("  " + p.name + " - Only " + p.quantity + " left!");
            }
        }
        if (!found) System.out.println("All products have sufficient stock.");
    }
    static void showTotalValue() {
        double total = 0;
        for (Product p : inventory) total += p.totalValue();
        System.out.printf("Total Inventory Value: ₹%.2f%n", total);
    }
    static void searchProduct() {
        System.out.print("Search by name: ");
        sc.nextLine();
        String kw = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Product p : inventory) {
            if (p.name.toLowerCase().contains(kw)) {
                if (!found) {
                    System.out.printf("%-5s %-20s %-10s %-13s%n", "ID", "Name", "Qty", "Price");
                    found = true;
                }
                p.display();
            }
        }
        if (!found) System.out.println("No product found.");
    }
    static Product findById(int id) {
        for (Product p : inventory) if (p.id == id) return p;
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
    static double getPositiveDouble() {
        while (true) {
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v > 0) return v;
                System.out.print("Must be positive: ");
            } catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
