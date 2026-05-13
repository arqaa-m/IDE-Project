package ids;

import java.util.Random;

public class LogGenerator {

    private static final Random random = new Random();

    private static final String[] IP_POOL = {
        "192.168.1.10", "192.168.1.22", "10.0.0.5",
        "172.16.0.8",   "203.0.113.42", "198.51.100.7",
        "192.168.2.15", "10.10.10.1",   "185.220.101.5",
        "45.33.32.156"
    };

    private static final String[] EVENT_TYPES = {
        "FAILED_LOGIN", "FAILED_LOGIN", "FAILED_LOGIN",
        "PORT_SCAN",    "PORT_SCAN",
        "AUTH_SUCCESS",
        "DATA_TRANSFER",
        "CONNECTION_REFUSED"
    };

    private static final String[] SEVERITIES = {
        "LOW", "LOW", "MEDIUM", "MEDIUM", "HIGH", "CRITICAL"
    };

    private static final int[] PORT_POOL = {
        21, 22, 23, 25, 53, 80, 110, 143,
        443, 445, 3306, 3389, 8080, 8443,
        139, 161, 5900, 6379
    };

    private long simulatedTime = System.currentTimeMillis();

    public LogEntry generate() {
        String ip        = IP_POOL[random.nextInt(IP_POOL.length)];
        String eventType = EVENT_TYPES[random.nextInt(EVENT_TYPES.length)];
        int    port      = PORT_POOL[random.nextInt(PORT_POOL.length)];
        String severity  = SEVERITIES[random.nextInt(SEVERITIES.length)];
        LogEntry entry = new LogEntry(ip, eventType, port, simulatedTime, severity);
        simulatedTime += 1000L + random.nextInt(4000);
        return entry;
    }

    public LogEntry generateLoginFail(String ip) {
        LogEntry entry = new LogEntry(ip, "FAILED_LOGIN", 22, simulatedTime, "HIGH");
        simulatedTime += 500L + random.nextInt(1500);
        return entry;
    }

    public LogEntry generatePortScan(String ip) {
        int port = 1 + random.nextInt(9999);
        LogEntry entry = new LogEntry(ip, "PORT_SCAN", port, simulatedTime, "MEDIUM");
        simulatedTime += 200L + random.nextInt(800);
        return entry;
    }
}