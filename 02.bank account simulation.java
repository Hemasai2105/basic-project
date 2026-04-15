// "  Bank Account Simulation 
// Create a banking system where users can: 
// • Create an account (Account Number, Name, Balance)  
// • Deposit money  
// • Withdraw money (with balance validation)  
// • View account details  
// • Transfer money between accounts  
// Rules: 
// • Prevent overdraft  
// • Maintain transaction history (optional enhancement)  "

import java.util.*;
public class BankAccount {
    static class Account {
        String accNumber;
        String name;
        double balance;
        List<String> transactions=new ArrayList<>();

        Account(String accNumber, String name, double balance) {
            this.accNumber=accNumber;
            this.name=name;
            this.balance=balance;
            transactions.add("Account created with balance: ₹" + balance);
        }

        void deposit(double amount) {
            if (amount <= 0) { System.out.println("Deposit amount must be positive."); return; }
            balance += amount;
            transactions.add("Deposited: ₹" + amount + " | Balance: ₹" + balance);
            System.out.println("₹" + amount + " deposited. New balance: ₹" + balance);
        }

        boolean withdraw(double amount) {
            if (amount <= 0) { System.out.println("Withdrawal amount must be positive."); return false; }
            if (amount > balance) { System.out.println("Insufficient balance! Available: ₹" + balance); return false; }
            balance -= amount;
            transactions.add("Withdrawn: ₹" + amount + " | Balance: ₹" + balance);
            System.out.println("₹" + amount + " withdrawn. New balance: ₹" + balance);
            return true;
        }

        void display() {
            System.out.println("-------------------------------");
            System.out.println("Account No : " + accNumber);
            System.out.println("Name       : " + name);
            System.out.printf("Balance    : ₹%.2f%n", balance);
        }

        void showHistory() {
            System.out.println("--- Transaction History for " + accNumber + " ---");
            for (String t : transactions) System.out.println("  " + t);
        }
    }

    static Map<String, Account> accounts = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Bank Account System =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Account Details");
            System.out.println("5. Transfer Money");
            System.out.println("6. Transaction History");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            switch (getInt()) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> viewAccount();
                case 5 -> transfer();
                case 6 -> showHistory();
                case 7 -> { System.out.println("Thank you for banking with us!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void createAccount() {
        System.out.print("Enter Account Number: ");
        String num = sc.nextLine().trim();
        if (accounts.containsKey(num)) { System.out.println("Account already exists!"); return; }
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Initial Deposit Amount: ₹");
        double bal = getDouble();
        if (bal < 0) { System.out.println("Balance cannot be negative."); return; }
        accounts.put(num, new Account(num, name, bal));
        System.out.println("Account created successfully!");
    }

    static Account getAccount() {
        System.out.print("Enter Account Number: ");
        String num = sc.nextLine().trim();
        Account a = accounts.get(num);
        if (a == null) System.out.println("Account not found.");
        return a;
    }

    static void deposit() {
        Account a = getAccount();
        if (a == null) return;
        System.out.print("Amount to deposit: ₹");
        a.deposit(getDouble());
    }

    static void withdraw() {
        Account a = getAccount();
        if (a == null) return;
        System.out.print("Amount to withdraw: ₹");
        a.withdraw(getDouble());
    }

    static void viewAccount() {
        Account a = getAccount();
        if (a != null) a.display();
    }

    static void transfer() {
        System.out.print("From Account Number: ");
        Account from = accounts.get(sc.nextLine().trim());
        System.out.print("To Account Number: ");
        Account to = accounts.get(sc.nextLine().trim());
        if (from == null || to == null) { System.out.println("One or both accounts not found."); return; }
        System.out.print("Amount to transfer: ₹");
        double amount = getDouble();
        if (from.withdraw(amount)) {
            to.deposit(amount);
            from.transactions.set(from.transactions.size() - 1,
                "Transferred ₹" + amount + " to " + to.accNumber + " | Balance: ₹" + from.balance);
            to.transactions.set(to.transactions.size() - 1,
                "Received ₹" + amount + " from " + from.accNumber + " | Balance: ₹" + to.balance);
            System.out.println("Transfer successful!");
        }
    }

    static void showHistory() {
        Account a = getAccount();
        if (a != null) a.showHistory();
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
