package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AuditLog - Represents a single audit log entry
 */
public class AuditLog {
    private LocalDateTime timestamp;
    private String username;
    private String action;
    private String ipAddress;
    private String status;
    private String details;
    
    public AuditLog(String username, String action, String status, String details) {
        this.timestamp = LocalDateTime.now();
        this.username = username != null ? username : "Unknown";
        this.action = action != null ? action : "UNKNOWN_ACTION";
        this.ipAddress = getLocalIPAddress();
        this.status = status != null ? status : "UNKNOWN";
        this.details = details != null ? details : "";
    }
    
    // Get local IP address
    private String getLocalIPAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
    
    // Convert to CSV format for file storage
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
            timestamp.format(formatter),
            escapeCSV(username),
            escapeCSV(action),
            escapeCSV(ipAddress),
            escapeCSV(status),
            escapeCSV(details)
        );
    }
    
    // Escape special characters for CSV
    private String escapeCSV(String value) {
        if (value == null) return "";
        // Replace quotes with double quotes and ensure no line breaks
        return value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ");
    }
    
    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getIpAddress() { return ipAddress; }
    public String getStatus() { return status; }
    public String getDetails() { return details; }
    
    @Override
    public String toString() {
        return String.format("AuditLog[%s, %s, %s, %s, %s, %s]", 
            timestamp, username, action, ipAddress, status, details);
    }
}