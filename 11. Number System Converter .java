// 11. Number System Converter 
// Create a program that: 
// • Converts numbers between binary, decimal, octal, and hexadecimal  
// Features: 
// • Menu-driven system  
// • Validate user input  
import java.util.Scanner;

public class NumberConverter {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Number System Converter =====");
            System.out.println("Convert FROM:");
            System.out.println("1. Decimal");
            System.out.println("2. Binary");
            System.out.println("3. Octal");
            System.out.println("4. Hexadecimal");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            int choice = getInt();
            if (choice == 5) { System.out.println("Goodbye!"); return; }
            if (choice < 1 || choice > 4) { System.out.println("Invalid choice."); continue; }

            System.out.print("Enter the number: ");
            String input = sc.nextLine().trim();

            long decimal;
            try {
                decimal = switch (choice) {
                    case 1 -> parseDecimal(input);
                    case 2 -> parseBinary(input);
                    case 3 -> parseOctal(input);
                    case 4 -> parseHex(input);
                    default -> throw new Exception("Invalid");
                };
            } catch (Exception e) {
                System.out.println("Invalid input for the selected base.");
                continue;
            }

            System.out.println("\n--- Conversion Results ---");
            System.out.println("Decimal     : " + decimal);
            System.out.println("Binary      : " + Long.toBinaryString(decimal));
            System.out.println("Octal       : " + Long.toOctalString(decimal));
            System.out.println("Hexadecimal : " + Long.toHexString(decimal).toUpperCase());
            System.out.println("----------------------------");
        }
    }

    static long parseDecimal(String s) throws Exception {
        if (!s.matches("-?[0-9]+")) throw new Exception("Invalid decimal");
        return Long.parseLong(s);
    }

    static long parseBinary(String s) throws Exception {
        if (!s.matches("[01]+")) throw new Exception("Invalid binary");
        return Long.parseLong(s, 2);
    }

    static long parseOctal(String s) throws Exception {
        if (!s.matches("[0-7]+")) throw new Exception("Invalid octal");
        return Long.parseLong(s, 8);
    }

    static long parseHex(String s) throws Exception {
        if (!s.matches("[0-9a-fA-F]+")) throw new Exception("Invalid hex");
        return Long.parseLong(s, 16);
    }

    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }
}
