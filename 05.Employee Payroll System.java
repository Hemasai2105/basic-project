// "Employee Payroll System 
// Design a payroll system that: 
// • Stores employee details (ID, Name, Salary)  
// • Calculates gross salary (including allowances)  
// • Deducts tax based on salary slabs  
// • Generates salary slip  
// Constraints: 
// • Use OOP concepts  
// • Different employee types (Manager, Developer) "
import java.util.*;
public class EmployeePayroll {
    abstract static class Employee {
        int id;
        String name;
        double baseSalary;
        Employee(int id, String name, double baseSalary) {
            this.id = id;
            this.name = name;
            this.baseSalary = baseSalary;
        }
        abstract double getAllowances();
        abstract String getType();
        double grossSalary() { return baseSalary + getAllowances(); }
        double taxDeduction() {
            double gross = grossSalary();
            if (gross <= 25000) return 0;
            else if (gross <= 50000) return gross * 0.10;
            else if (gross <= 100000) return gross * 0.20;
            else return gross * 0.30;
        }
        double netSalary() { return grossSalary() - taxDeduction(); }
        void generateSlip() {
            System.out.println("\n============================================");
            System.out.println("           SALARY SLIP");
            System.out.println("============================================");
            System.out.printf("Employee ID   : %d%n", id);
            System.out.printf("Name          : %s%n", name);
            System.out.printf("Designation   : %s%n", getType());
            System.out.println("--------------------------------------------");
            System.out.printf("Basic Salary  : ₹%10.2f%n", baseSalary);
            System.out.printf("Allowances    : ₹%10.2f%n", getAllowances());
            System.out.printf("Gross Salary  : ₹%10.2f%n", grossSalary());
            System.out.println("--------------------------------------------");
            System.out.printf("Tax Deduction : ₹%10.2f%n", taxDeduction());
            System.out.println("--------------------------------------------");
            System.out.printf("NET SALARY    : ₹%10.2f%n", netSalary());
            System.out.println("============================================");
        }
    }
    static class Manager extends Employee {
        Manager(int id, String name, double salary) { super(id, name, salary); }
        @Override
        double getAllowances() {
            return baseSalary * 0.40 + 5000; // HRA 40% + Travel allowance
        }
        @Override String getType() { return "Manager"; }
    }
    static class Developer extends Employee {
        Developer(int id, String name, double salary) { super(id, name, salary); }
        @Override
        double getAllowances() {
            return baseSalary * 0.25 + 2000; // HRA 25% + internet allowance
        }
        @Override String getType() { return "Developer"; }
    }
    static List<Employee> employees = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Employee Payroll System =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Generate Salary Slip");
            System.out.println("3. Display All Employees");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            switch (getInt()) {
                case 1 -> addEmployee();
                case 2 -> generateSlip();
                case 3 -> displayAll();
                case 4 -> deleteEmployee();
                case 5 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    static void addEmployee() {
        System.out.print("Employee ID: ");
        int id = getInt();
        for (Employee e : employees) if (e.id == id) { System.out.println("ID already exists!"); return; }
        System.out.print("Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        System.out.println("Type: 1. Manager  2. Developer");
        System.out.print("Choose: ");
        int type = getInt();
        if (type != 1 && type != 2) { System.out.println("Invalid type."); return; }
        System.out.print("Basic Salary: ₹");
        double salary = getDouble();
        if (salary <= 0) { System.out.println("Salary must be positive."); return; }
        employees.add(type == 1 ? new Manager(id, name, salary) : new Developer(id, name, salary));
        System.out.println("Employee added.");
    }
    static void generateSlip() {
        System.out.print("Enter Employee ID: ");
        int id = getInt();
        for (Employee e : employees) if (e.id == id) { e.generateSlip(); return; }
        System.out.println("Employee not found.");
    }
    static void displayAll() {
        if (employees.isEmpty()) { System.out.println("No employees."); return; }
        System.out.printf("%-5s %-20s %-12s %-12s %-12s%n", "ID", "Name", "Type", "Gross", "Net");
        System.out.println("-".repeat(65));
        for (Employee e : employees)
            System.out.printf("%-5d %-20s %-12s ₹%-11.2f ₹%-11.2f%n",
                e.id, e.name, e.getType(), e.grossSalary(), e.netSalary());
    }
    static void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        int id = getInt();
        Iterator<Employee> it = employees.iterator();
        while (it.hasNext()) {
            if (it.next().id == id) { it.remove(); System.out.println("Employee removed."); return; }
        }
        System.out.println("Not found.");
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
