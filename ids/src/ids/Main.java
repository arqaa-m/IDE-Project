package ids;

import java.util.Scanner;
import java.util.HashMap;

public class Main {

    static LogGenerator    generator    = new LogGenerator();
    static LogProcessor    processor    = new LogProcessor();
    static AlertManager    alertManager = new AlertManager();
    static DetectionEngine engine       = new DetectionEngine(alertManager);
    static Scanner         scanner      = new Scanner(System.in);

    public static void main(String[] args) {
        alertManager.pushRule(AlertManager.RULE_BRUTE_FORCE);
        alertManager.pushRule(AlertManager.RULE_PORT_SCAN);

        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": configureRules(); break;
                case "2": runSimulation();  break;
                case "3": generateReport(); break;
                case "4": running = false;  break;
                default:
                    System.out.println("  Invalid option. Enter 1, 2, 3, or 4.");
            }
        }
        System.out.println("\n  [IDS] Session ended. Goodbye.");
    }

    static void configureRules() {
        System.out.println();
        System.out.println("  === RULE CONFIGURATION ===");
        alertManager.displayActiveRules();
        System.out.println();
        System.out.println("  A) Enable  Brute Force Detection");
        System.out.println("  B) Disable Brute Force Detection");
        System.out.println("  C) Enable  Port Scan Detection");
        System.out.println("  D) Disable Port Scan Detection");
        System.out.println("  E) Pop top rule off stack");
        System.out.println("  F) Back to main menu");
        System.out.print  ("  Choice: ");

        String choice = scanner.nextLine().trim().toUpperCase();
        switch (choice) {
            case "A":
                alertManager.pushRule(AlertManager.RULE_BRUTE_FORCE);
                System.out.println("  [ON]  Brute Force Detection enabled.");
                break;
            case "B":
                alertManager.removeRule(AlertManager.RULE_BRUTE_FORCE);
                System.out.println("  [OFF] Brute Force Detection disabled.");
                break;
            case "C":
                alertManager.pushRule(AlertManager.RULE_PORT_SCAN);
                System.out.println("  [ON]  Port Scan Detection enabled.");
                break;
            case "D":
                alertManager.removeRule(AlertManager.RULE_PORT_SCAN);
                System.out.println("  [OFF] Port Scan Detection disabled.");
                break;
            case "E":
                String popped = alertManager.popRule();
                if (popped == null) System.out.println("  Stack is empty.");
                else                System.out.println("  Popped rule: " + popped);
                break;
            case "F":
                break;
            default:
                System.out.println("  Invalid option.");
        }
        System.out.println();
        alertManager.displayActiveRules();
        System.out.println();
    }

    static void runSimulation() {
        processor.reset();
        alertManager.reset();
        engine.reset();

        alertManager.pushRule(AlertManager.RULE_BRUTE_FORCE);
        alertManager.pushRule(AlertManager.RULE_PORT_SCAN);

        System.out.println();
        System.out.println("  === LIVE SIMULATION ===");
        System.out.print  ("  How many log entries to simulate? (10-200): ");

        int count = 50;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
            if (count < 10)  count = 10;
            if (count > 200) count = 200;
        } catch (NumberFormatException e) {
            System.out.println("  Invalid input -- using default of 50.");
        }

        System.out.println();
        System.out.println("  Generating " + count + " entries...");
        System.out.println("  " + "-".repeat(60));

        for (int i = 0; i < count; i++) {
            processor.enqueue(generator.generate());
        }

        LogEntry entry;
        int processed = 0;
        while ((entry = processor.pollAndUpdate()) != null) {
            System.out.println("  " + entry);
            engine.analyze(entry);
            processed++;
        }

        System.out.println("  " + "-".repeat(60));
        System.out.println("  Done. Entries processed: " + processed);
        System.out.println("  Alerts fired: " + alertManager.alertCount());
        System.out.println();
    }

    static void generateReport() {
        System.out.println();
        System.out.println("  === SIMULATION REPORT ===");
        System.out.println();

        System.out.println("  -- IP EVENT SUMMARY --");
        HashMap<String, Integer> counts = processor.getIpEventCount();
        if (counts.isEmpty()) {
            System.out.println("  (no simulation has been run yet)");
        } else {
            System.out.printf("  %-18s %-8s %-13s %s%n",
                    "IP Address", "Events", "Login Fails", "Ports Probed");
            System.out.println("  " + "-".repeat(54));
            for (String ip : counts.keySet()) {
                System.out.printf("  %-18s %-8d %-13d %d%n",
                        ip,
                        processor.getEventCount(ip),
                        engine.getLoginFailCount(ip),
                        engine.getDistinctPortCount(ip));
            }
        }

        System.out.println();
        System.out.println("  -- ALERT LOG --");
        alertManager.displayAlertLog();

        System.out.println();
        System.out.println("  -- ACTIVE RULES --");
        alertManager.displayActiveRules();

        System.out.println();
        System.out.println("  End of report.");
        System.out.println();
    }

    static void printBanner() {
        System.out.println();
        System.out.println("  +---------------------------------------+");
        System.out.println("  |  Network Intrusion Detection System   |");
        System.out.println("  +---------------------------------------+");
        System.out.println("  Both detection rules are ON by default.");
        System.out.println("  Use option 1 to configure rules.");
        System.out.println();
    }

    static void printMenu() {
        System.out.println("  +-----------------------+");
        System.out.println("  |      MAIN MENU        |");
        System.out.println("  +-----------------------+");
        System.out.println("  |  1. Configure rules   |");
        System.out.println("  |  2. Run simulation    |");
        System.out.println("  |  3. Generate report   |");
        System.out.println("  |  4. Exit              |");
        System.out.println("  +-----------------------+");
        System.out.print  ("  > ");
    }
}