package ids;

public class LogEntry {
    String ipAddress;
    String eventType;
    int port;
    long timestamp;
    String severity;

    public LogEntry(String ipAddress, String eventType, int port, long timestamp, String severity) {
        this.ipAddress = ipAddress;
        this.eventType = eventType;
        this.port = port;
        this.timestamp = timestamp;
        this.severity = severity;
    }

    public String getIpAddress() { return ipAddress; }
    public String getEventType() { return eventType; }
    public int    getPort()      { return port; }
    public long   getTimestamp() { return timestamp; }
    public String getSeverity()  { return severity; }

    public String toString() {
        java.time.Instant inst = java.time.Instant.ofEpochMilli(timestamp);
        return "[" + inst + "] " + ipAddress
                + " | " + eventType
                + " | port " + port
                + " | " + severity;
    }
}