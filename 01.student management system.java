// ". Student Management System 
// Design a console-based application to manage student records. 
// The system should allow users to: 
// • Add a new student (ID, name, marks in multiple subjects)  
// • Display all students  
// • Search student by ID  
// • Calculate average marks and grade  
// • Update and delete student records  
// Constraints: 
// • Use arrays or collections  
// • Handle invalid inputs gracefully"
import java.util.*;
public class StudentManagement {
    static class Student {
        int id;
        String name;
        double[] marks;
        Student(int id, String name, double[] marks) {
            this.id=id;
            this.name=name;
            this.marks=marks;
        }
        double average(){
            double sum=0;
            for (double m : marks) sum +=m;
            return sum/marks.length;
        }
        String grade(){
            double avg=average();
            if (avg>=90) return "A+";
            else if (avg>=80) return "A";
            else if (avg>=70) return "B";
            else if (avg>=60) return "C";
            else if (avg>=50) return "D";
            else return "F";
        }

        void display(){
            System.out.println("-------------------------------");
            System.out.println("ID     : " + id);
            System.out.println("Name   : " + name);
            System.out.print("Marks  : ");
            for (int i=0;i<marks.length;i++)
                System.out.print("Sub" + (i + 1) + "=" + marks[i] + (i < marks.length - 1 ? "  " : ""));
            System.out.println();
            System.out.printf("Average: %.2f%n", average());
            System.out.println("Grade  : " + grade());
        }
    }

    static List<Student> students=new ArrayList<>();
    static Scanner sc=new Scanner(System.in);

    public static void main(String[] args){
        while (true) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            int choice=getInt();
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayAll();
                case 3 -> searchById();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addStudent() {
        System.out.print("Enter Student ID: ");
        int id = getInt();
        for (Student s : students) {
            if (s.id == id) { System.out.println("ID already exists!"); return; }
        }
        System.out.print("Enter Name: ");
        sc.nextLine();
        String name = sc.nextLine().trim();
        System.out.print("How many subjects? ");
        int n = getInt();
        if (n <= 0) { System.out.println("Invalid number of subjects."); return; }
        double[] marks = new double[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Marks for Subject " + (i + 1) + " (0-100): ");
            marks[i] = getDouble();
            if (marks[i] < 0 || marks[i] > 100) {
                System.out.println("Marks must be between 0 and 100."); i--;
            }
        }
        students.add(new Student(id, name, marks));
        System.out.println("Student added successfully!");
    }

    static void displayAll() {
        if (students.isEmpty()) { System.out.println("No students found."); return; }
        for (Student s : students) s.display();
        System.out.println("-------------------------------");
    }

    static void searchById() {
        System.out.print("Enter Student ID to search: ");
        int id = getInt();
        for (Student s : students) {
            if (s.id == id) { s.display(); return; }
        }
        System.out.println("Student not found.");
    }

    static void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        int id = getInt();
        for (Student s : students) {
            if (s.id == id) {
                System.out.print("New Name (current: " + s.name + "): ");
                sc.nextLine();
                String name = sc.nextLine().trim();
                if (!name.isEmpty()) s.name = name;
                System.out.print("Update marks? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    for (int i = 0; i < s.marks.length; i++) {
                        System.out.print("New marks for Subject " + (i + 1) + ": ");
                        double m = getDouble();
                        if (m >= 0 && m <= 100) s.marks[i] = m;
                        else System.out.println("Invalid, keeping old value.");
                    }
                }
                System.out.println("Student updated.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int id = getInt();
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            if (it.next().id == id) { it.remove(); System.out.println("Student deleted."); return; }
        }
        System.out.println("Student not found.");
    }

    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid input. Enter a number: "); }
        }
    }

    static double getDouble() {
        while (true) {
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid input. Enter a number: "); }
        }
    }
}
