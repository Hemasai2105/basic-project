// ATM Simulation System 
// Create an ATM interface that: 
// • Authenticates user using PIN  
// • Allows withdrawal, deposit, balance check  
// • Shows mini statement  
// Rules: 
// • Limit number of attempts for PIN  
// • Daily withdrawal limit  
import java.util.*;
public class ATMSimulation {
    static class ATMAccount {
        String cardNumber;
        String pin;
        String name;
        double balance;
        double dailyWithdrawn;
        static final double DAILY_LIMIT = 20000;
        List<String> miniStatement = new ArrayList<>();
        ATMAccount(String card, String pin, String name, double balance) {
            this.cardNumber = card;
            this.pin = pin;
            this.name = name;
            this.balance = balance;
            miniStatement.add("Account opened. Balance: ₹" + balance);
        }
        boolean deposit(double amount) {
            if (amount <= 0) return false;
            balance += amount;
            miniStatement.add("DEP  ₹" + String.format("%.2f", amount) + "  Bal: ₹" + String.format("%.2f", balance));
            if (miniStatement.size() > 6) miniStatement.remove(0);
            return true;
        }
        boolean withdraw(double amount) {
            if (amount <= 0 || amount > balance) return false;
            if (dailyWithdrawn + amount > DAILY_LIMIT) return false;
            balance -= amount;
            dailyWithdrawn += amount;
            miniStatement.add("WDR  ₹" + String.format("%.2f", amount) + "  Bal: ₹" + String.format("%.2f", balance));
            if (miniStatement.size() > 6) miniStatement.remove(0);
            return true;
        }
    }
    static Map<String, ATMAccount> accounts = new HashMap<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        loadAccounts();
        System.out.println("========================================");
        System.out.println("        WELCOME TO JAVA BANK ATM        ");
        System.out.println("========================================");
        while (true) {
            System.out.print("\nInsert Card (Enter Card Number) or 0 to exit: ");
            String card = sc.nextLine().trim();
            if (card.equals("0")) { System.out.println("Thank you. Goodbye!"); break; }
            ATMAccount acc = accounts.get(card);
            if (acc == null) { System.out.println("Card not recognized."); continue; }
            if (!authenticate(acc)) continue;
            System.out.println("\nWelcome, " + acc.name + "!");
            runATMSession(acc);
        }
    }
    static boolean authenticate(ATMAccount acc) {
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("Enter PIN: ");
            String pin = sc.nextLine().trim();
            if (pin.equals(acc.pin)) return true;
            attempts--;
            if (attempts > 0)
                System.out.println("Wrong PIN. " + attempts + " attempt(s) remaining.");
            else
                System.out.println("Card blocked. Too many failed attempts.");
        }
        return false;
    }
    static void runATMSession(ATMAccount acc) {
        while (true) {
            System.out.println("\n--- ATM Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Mini Statement");
            System.out.println("5. Exit / Eject Card");
            System.out.print("Choose: ");
            switch (getInt()) {
                case 1 -> System.out.printf("Current Balance: ₹%.2f%n", acc.balance);
                case 2 -> {
                    System.out.print("Deposit Amount: ₹");
                    double amt = getDouble();
                    if (acc.deposit(amt)) System.out.printf("₹%.2f deposited. Balance: ₹%.2f%n", amt, acc.balance);
                    else System.out.println("Invalid amount.");
                }
                case 3 -> {
                    System.out.print("Withdrawal Amount: ₹");
                    double amt = getDouble();
                    if (amt > acc.balance) System.out.println("Insufficient balance.");
                    else if (acc.dailyWithdrawn + amt > ATMAccount.DAILY_LIMIT)
                        System.out.printf("Daily limit exceeded! Remaining limit: ₹%.2f%n",
                            ATMAccount.DAILY_LIMIT - acc.dailyWithdrawn);
                    else if (acc.withdraw(amt))
                        System.out.printf("Please collect ₹%.2f. Balance: ₹%.2f%n", amt, acc.balance);
                    else System.out.println("Transaction failed.");
                }
                case 4 -> {
                    System.out.println("--- Mini Statement ---");
                    System.out.println("Card: " + acc.cardNumber + " | Name: " + acc.name);
                    for (String t : acc.miniStatement) System.out.println("  " + t);
                }
                case 5 -> { System.out.println("Card ejected. Thank you!"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }
    static void loadAccounts() {
        accounts.put("1234567890", new ATMAccount("1234567890", "1234", "Arjun Kumar", 50000));
        accounts.put("9876543210", new ATMAccount("9876543210", "5678", "Priya Sharma", 120000));
        accounts.put("1111222233", new ATMAccount("1111222233", "4321", "Ravi Verma", 30000));
    }
    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
    static double getDouble() {
        while (true) {
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
