package ids;

import java.util.HashMap;
import java.util.HashSet;

public class DetectionEngine {

    public static final int BRUTE_FORCE_THRESHOLD = 5;
    public static final int PORT_SCAN_THRESHOLD   = 6;

    private AlertManager alertManager;

    private HashMap<String, Integer>          loginFailCounts;
    private HashMap<String, HashSet<Integer>> portsSeen;
    private HashSet<String>                   bruteForceAlerted;
    private HashSet<String>                   portScanAlerted;

    public DetectionEngine(AlertManager alertManager) {
        this.alertManager  = alertManager;
        loginFailCounts    = new HashMap<>();
        portsSeen          = new HashMap<>();
        bruteForceAlerted  = new HashSet<>();
        portScanAlerted    = new HashSet<>();
    }

    public void analyze(LogEntry entry) {
        String ip        = entry.getIpAddress();
        String eventType = entry.getEventType();
        int    port      = entry.getPort();

        trackPort(ip, port);

        if (eventType.equals("FAILED_LOGIN")) {
            loginFailCounts.put(ip, loginFailCounts.getOrDefault(ip, 0) + 1);
        }

        checkBruteForce(ip);
        checkPortScan(ip);
    }

    private void trackPort(String ip, int port) {
        if (!portsSeen.containsKey(ip)) {
            portsSeen.put(ip, new HashSet<>());
        }
        portsSeen.get(ip).add(port);
    }

    private void checkBruteForce(String ip) {
        if (bruteForceAlerted.contains(ip)) return;
        int fails = loginFailCounts.getOrDefault(ip, 0);
        if (fails >= BRUTE_FORCE_THRESHOLD) {
            String msg = "BRUTE FORCE -- IP: " + ip + " | Failed logins: " + fails;
            alertManager.fireAlert(AlertManager.RULE_BRUTE_FORCE, msg);
            bruteForceAlerted.add(ip);
        }
    }

    private void checkPortScan(String ip) {
        if (portScanAlerted.contains(ip)) return;
        HashSet<Integer> ports = portsSeen.getOrDefault(ip, new HashSet<>());
        if (ports.size() >= PORT_SCAN_THRESHOLD) {
            String msg = "PORT SCAN -- IP: " + ip + " | Distinct ports: " + ports.size();
            alertManager.fireAlert(AlertManager.RULE_PORT_SCAN, msg);
            portScanAlerted.add(ip);
        }
    }

    public int getLoginFailCount(String ip) {
        return loginFailCounts.getOrDefault(ip, 0);
    }

    public int getDistinctPortCount(String ip) {
        HashSet<Integer> ports = portsSeen.get(ip);
        return (ports == null) ? 0 : ports.size();
    }

    public void reset() {
        loginFailCounts.clear();
        portsSeen.clear();
        bruteForceAlerted.clear();
        portScanAlerted.clear();
    }
}