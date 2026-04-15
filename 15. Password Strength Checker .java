// 15. Password Strength Checker 
// Build a program that: 
// • Takes password input  
// • Checks strength based on:  
// o Length  
// o Uppercase, lowercase, digits, special characters  
// Output: 
// • Weak / Medium / Strong  
// • Suggestions to improve password 
import java.util.Scanner;

public class PasswordChecker {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Password Strength Checker =====");
            System.out.print("Enter password (or 'exit'): ");
            String password = sc.nextLine();
            if (password.equalsIgnoreCase("exit")) { System.out.println("Goodbye!"); break; }
            analyzePassword(password);
        }
    }

    static void analyzePassword(String pwd) {
        int score = 0;
        List<String> suggestions = new java.util.ArrayList<>();
        List<String> strengths = new java.util.ArrayList<>();

        // Length check
        int len = pwd.length();
        if (len >= 16) { score += 3; strengths.add("Excellent length (16+)"); }
        else if (len >= 12) { score += 2; strengths.add("Good length (12+)"); }
        else if (len >= 8) { score += 1; strengths.add("Minimum length (8+)"); }
        else { suggestions.add("Use at least 8 characters (currently " + len + ")"); }

        // Uppercase
        boolean hasUpper = !pwd.equals(pwd.toLowerCase());
        if (hasUpper) { score++; strengths.add("Has uppercase letters"); }
        else suggestions.add("Add uppercase letters (A-Z)");

        // Lowercase
        boolean hasLower = !pwd.equals(pwd.toUpperCase());
        if (hasLower) { score++; strengths.add("Has lowercase letters"); }
        else suggestions.add("Add lowercase letters (a-z)");

        // Digits
        boolean hasDigit = pwd.chars().anyMatch(Character::isDigit);
        if (hasDigit) { score++; strengths.add("Has numeric digits"); }
        else suggestions.add("Add numbers (0-9)");

        // Special characters
        boolean hasSpecial = pwd.chars().anyMatch(c -> "!@#$%^&*()_+-=[]{}|;':\",./<>?".indexOf(c) >= 0);
        if (hasSpecial) { score += 2; strengths.add("Has special characters"); }
        else suggestions.add("Add special characters (!@#$%...)");

        // No common patterns
        String lower = pwd.toLowerCase();
        String[] commonPatterns = {"password", "123456", "qwerty", "abc123", "admin", "letmein", "welcome"};
        boolean isCommon = false;
        for (String p : commonPatterns) {
            if (lower.contains(p)) { isCommon = true; break; }
        }
        if (isCommon) { score -= 2; suggestions.add("Avoid common words like 'password', '123456'"); }
        else { score++; strengths.add("No common patterns found"); }

        // Repeated characters
        boolean hasRepeat = false;
        for (int i = 0; i < pwd.length() - 2; i++) {
            if (pwd.charAt(i) == pwd.charAt(i + 1) && pwd.charAt(i + 1) == pwd.charAt(i + 2)) {
                hasRepeat = true; break;
            }
        }
        if (hasRepeat) { score--; suggestions.add("Avoid repeating characters (e.g. aaa, 111)"); }

        // Determine strength
        String strength;
        String indicator;
        if (score >= 8) { strength = "VERY STRONG"; indicator = "█████ 100%"; }
        else if (score >= 6) { strength = "STRONG"; indicator = "████░ 80%"; }
        else if (score >= 4) { strength = "MEDIUM"; indicator = "███░░ 60%"; }
        else if (score >= 2) { strength = "WEAK"; indicator = "██░░░ 40%"; }
        else { strength = "VERY WEAK"; indicator = "█░░░░ 20%"; }

        System.out.println("\n--- Password Analysis ---");
        System.out.println("Password : " + maskPassword(pwd));
        System.out.println("Strength : " + strength + "  " + indicator);
        System.out.println("Score    : " + Math.max(score, 0) + "/10");

        if (!strengths.isEmpty()) {
            System.out.println("\nStrengths:");
            for (String s : strengths) System.out.println("  [+] " + s);
        }

        if (!suggestions.isEmpty()) {
            System.out.println("\nSuggestions to improve:");
            for (String s : suggestions) System.out.println("  [!] " + s);
        } else {
            System.out.println("\nExcellent! Your password is very secure.");
        }
    }

    static String maskPassword(String pwd) {
        if (pwd.length() <= 3) return "*".repeat(pwd.length());
        return pwd.charAt(0) + "*".repeat(pwd.length() - 2) + pwd.charAt(pwd.length() - 1);
    }
}
