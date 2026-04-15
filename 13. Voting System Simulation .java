// 13. Voting System Simulation 
// Create a voting system: 
// • Register voters  
// • Cast votes  
// • Display results  
// Rules: 
// • One vote per user  
// • Prevent duplicate voting 
import java.util.*;

public class VotingSystem {

    static class Voter {
        String id;
        String name;
        boolean hasVoted;

        Voter(String id, String name) {
            this.id = id;
            this.name = name;
            this.hasVoted = false;
        }
    }

    static Map<String, Voter> voters = new HashMap<>();
    static Map<String, Integer> candidates = new LinkedHashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadCandidates();
        while (true) {
            System.out.println("\n===== Voting System =====");
            System.out.println("1. Register Voter");
            System.out.println("2. Cast Vote");
            System.out.println("3. View Results");
            System.out.println("4. Display Voters");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            switch (getInt()) {
                case 1 -> registerVoter();
                case 2 -> castVote();
                case 3 -> showResults();
                case 4 -> displayVoters();
                case 5 -> { System.out.println("Voting closed. Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void registerVoter() {
        System.out.print("Voter ID (e.g. AADHAR/EPIC): ");
        sc.nextLine();
        String id = sc.nextLine().trim().toUpperCase();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        if (voters.containsKey(id)) { System.out.println("Voter already registered!"); return; }
        System.out.print("Full Name: ");
        String name = sc.nextLine().trim();
        voters.put(id, new Voter(id, name));
        System.out.println("Voter " + name + " registered successfully.");
    }

    static void castVote() {
        System.out.print("Enter Voter ID: ");
        sc.nextLine();
        String id = sc.nextLine().trim().toUpperCase();
        Voter voter = voters.get(id);
        if (voter == null) { System.out.println("Voter not registered."); return; }
        if (voter.hasVoted) { System.out.println("You have already voted!"); return; }

        System.out.println("\nWelcome, " + voter.name + "!");
        System.out.println("--- Candidates ---");
        int i = 1;
        List<String> names = new ArrayList<>(candidates.keySet());
        for (String c : names) System.out.println(i++ + ". " + c);

        System.out.print("Enter candidate number: ");
        int choice = getIntInRange(1, names.size()) - 1;
        String selected = names.get(choice);

        candidates.put(selected, candidates.get(selected) + 1);
        voter.hasVoted = true;
        System.out.println("Vote cast successfully for: " + selected);
    }

    static void showResults() {
        System.out.println("\n=== ELECTION RESULTS ===");
        int totalVotes = candidates.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total Votes Cast: " + totalVotes);
        System.out.println();

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(candidates.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        System.out.printf("%-3s %-25s %-8s %s%n", "#", "Candidate", "Votes", "Percentage");
        System.out.println("-".repeat(55));

        for (int i = 0; i < sorted.size(); i++) {
            String name = sorted.get(i).getKey();
            int votes = sorted.get(i).getValue();
            double pct = totalVotes == 0 ? 0 : (votes * 100.0) / totalVotes;
            System.out.printf("%-3d %-25s %-8d %.2f%%%n", i + 1, name, votes, pct);
        }

        if (totalVotes > 0) {
            String winner = sorted.get(0).getKey();
            System.out.println("\n*** WINNER: " + winner + " ***");
        }
    }

    static void displayVoters() {
        if (voters.isEmpty()) { System.out.println("No voters registered."); return; }
        System.out.printf("%-20s %-15s %s%n", "Name", "ID", "Voted");
        System.out.println("-".repeat(45));
        for (Voter v : voters.values())
            System.out.printf("%-20s %-15s %s%n", v.name, v.id, v.hasVoted ? "Yes" : "No");
    }

    static void loadCandidates() {
        candidates.put("Arjun Reddy", 0);
        candidates.put("Priya Nair", 0);
        candidates.put("Karan Mehta", 0);
        candidates.put("Sunita Verma", 0);
    }

    static int getInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Invalid. Enter a number: "); }
        }
    }

    static int getIntInRange(int min, int max) {
        while (true) {
            int v = getInt();
            if (v >= min && v <= max) return v;
            System.out.print("Enter between " + min + " and " + max + ": ");
        }
    }
}
