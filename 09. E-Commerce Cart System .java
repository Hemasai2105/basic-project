//  E-Commerce Cart System 
// Design a shopping cart system: 
// • Add products to cart  
// • Remove items  
// • Calculate total bill  
// • Apply discounts  
// Features: 
// • Product catalog  
// • Checkout system 
import java.util.*;
public class ECommerceCart {
    static class Product {
        int id;
        String name;
        double price;
        int stock;
        Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
        void display() {
            System.out.printf("%-4d %-25s ₹%-10.2f Stock: %d%n", id, name, price, stock);
        }
    }
    static class CartItem {
        Product product;
        int quantity;
        CartItem(Product p, int qty) { this.product = p; this.quantity = qty; }
        double subtotal() { return product.price * quantity; }
    }
    static Map<String, Double> discountCodes = new HashMap<>();
    static List<Product> catalog = new ArrayList<>();
    static Map<Integer, CartItem> cart = new LinkedHashMap<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        loadCatalog();
        loadDiscounts();
        while (true) {
            System.out.println("\n===== E-Commerce Shopping System =====");
            System.out.println("1. View Product Catalog");
            System.out.println("2. Add to Cart");
            System.out.println("3. Remove from Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            switch (getInt()) {
                case 1 -> showCatalog();
                case 2 -> addToCart();
                case 3 -> removeFromCart();
                case 4 -> viewCart(true);
                case 5 -> checkout();
                case 6 -> { System.out.println("Thank you for shopping!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    static void showCatalog() {
        System.out.printf("%-4s %-25s %-12s %s%n", "ID", "Name", "Price", "Stock");
        System.out.println("-".repeat(55));
        for (Product p : catalog) p.display();
    }
    static void addToCart() {
        showCatalog();
        System.out.print("Enter Product ID: ");
        int id = getInt();
        Product p = findProduct(id);
        if (p == null) { System.out.println("Product not found."); return; }
        if (p.stock == 0) { System.out.println("Out of stock!"); return; }
        System.out.print("Quantity: ");
        int qty = getPositiveInt();
        if (qty > p.stock) { System.out.println("Only " + p.stock + " available."); return; }
        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            int total = item.quantity + qty;
            if (total > p.stock) { System.out.println("Not enough stock. Max: " + p.stock); return; }
            item.quantity = total;
        } else {
            cart.put(id, new CartItem(p, qty));
        }
        System.out.println(p.name + " (x" + qty + ") added to cart.");
    }
    static void removeFromCart() {
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }
        viewCart(false);
        System.out.print("Enter Product ID to remove: ");
        int id = getInt();
        if (cart.remove(id) != null) System.out.println("Item removed.");
        else System.out.println("Item not in cart.");
    }
    static void viewCart(boolean showTotal) {
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }
        System.out.printf("%-4s %-25s %-10s %-8s %-10s%n", "ID", "Product", "Price", "Qty", "Subtotal");
        System.out.println("-".repeat(60));
        double total = 0;
        for (CartItem item : cart.values()) {
            System.out.printf("%-4d %-25s ₹%-9.2f %-8d ₹%-10.2f%n",
                item.product.id, item.product.name, item.product.price, item.quantity, item.subtotal());
            total += item.subtotal();
        }
        if (showTotal) System.out.printf("%nCart Total: ₹%.2f%n", total);
    }
    static void checkout() {
        if (cart.isEmpty()) { System.out.println("Cart is empty!"); return; }
        viewCart(false);
        double total = 0;
        for (CartItem item : cart.values()) total += item.subtotal();
        System.out.print("Enter Discount Code (or press Enter to skip): ");
        sc.nextLine();
        String code = sc.nextLine().trim().toUpperCase();
        double discount = 0;
        if (!code.isEmpty()) {
            if (discountCodes.containsKey(code)) {
                discount = discountCodes.get(code);
                System.out.printf("Discount applied: %.0f%% OFF%n", discount * 100);
            } else {
                System.out.println("Invalid discount code.");
            }
        }
        double discountAmount = total * discount;
        double finalTotal = total - discountAmount;
        System.out.println("\n========== ORDER SUMMARY ==========");
        System.out.printf("Subtotal       : ₹%.2f%n", total);
        if (discount > 0) System.out.printf("Discount (%.0f%%): -₹%.2f%n", discount * 100, discountAmount);
        System.out.printf("TOTAL PAYABLE  : ₹%.2f%n", finalTotal);
        System.out.println("====================================");
        System.out.print("Confirm order? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            for (CartItem item : cart.values()) item.product.stock -= item.quantity;
            cart.clear();
            System.out.println("Order placed successfully! Thank you!");
        } else {
            System.out.println("Order cancelled.");
        }
    }
    static void loadCatalog() {
        catalog.add(new Product(1, "Laptop", 55000, 10));
        catalog.add(new Product(2, "Wireless Mouse", 799, 50));
        catalog.add(new Product(3, "USB-C Hub", 1499, 30));
        catalog.add(new Product(4, "Mechanical Keyboard", 3500, 20));
        catalog.add(new Product(5, "Monitor 24\"", 15000, 8));
        catalog.add(new Product(6, "Webcam HD", 2200, 15));
        catalog.add(new Product(7, "Headphones", 4500, 25));
        catalog.add(new Product(8, "Mousepad XL", 599, 100));
    }
    static void loadDiscounts() {
        discountCodes.put("SAVE10", 0.10);
        discountCodes.put("SAVE20", 0.20);
        discountCodes.put("FIRST50", 0.50);
    }
    static Product findProduct(int id) {
        for (Product p : catalog) if (p.id == id) return p;
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
