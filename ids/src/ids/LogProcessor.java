package ids;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class LogProcessor {

    private Queue<LogEntry>          logQueue;
    private HashMap<String, Integer> ipEventCount;

    public LogProcessor() {
        logQueue     = new LinkedList<>();
        ipEventCount = new HashMap<>();
    }

    public void enqueue(LogEntry entry) {
        logQueue.add(entry);
    }

    public LogEntry pollAndUpdate() {
        if (logQueue.isEmpty()) return null;
        LogEntry entry = logQueue.poll();
        String   ip    = entry.getIpAddress();
        ipEventCount.put(ip, ipEventCount.getOrDefault(ip, 0) + 1);
        return entry;
    }

    public int queueSize() { return logQueue.size(); }

    public int getEventCount(String ip) {
        return ipEventCount.getOrDefault(ip, 0);
    }

    public HashMap<String, Integer> getIpEventCount() { return ipEventCount; }

    public void displayIpCounts() {
        System.out.printf("  %-18s %-8s%n", "IP Address", "Events");
        System.out.println("  " + "-".repeat(28));
        if (ipEventCount.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        for (String ip : ipEventCount.keySet()) {
            System.out.printf("  %-18s %d%n", ip, ipEventCount.get(ip));
        }
    }

    public void reset() {
        logQueue.clear();
        ipEventCount.clear();
    }
}