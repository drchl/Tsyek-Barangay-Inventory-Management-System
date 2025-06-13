package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

/**
 * AuditLogger - Handles writing audit logs to file
 */
public class AuditLogger {
    private static final Logger logger = Logger.getLogger(AuditLogger.class.getName());
    private static final String AUDIT_LOG_FILE = "audit_log.csv";
    private static final String HEADER = "Timestamp,Username,Action,IP Address,Status,Details";
    
    // Authentication actions
    public static final String ACTION_LOGIN_ATTEMPT = "LOGIN_ATTEMPT";
    public static final String ACTION_LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String ACTION_LOGIN_FAILED = "LOGIN_FAILED";
    public static final String ACTION_LOGOUT = "LOGOUT";
    public static final String ACTION_PASSWORD_CHANGE = "PASSWORD_CHANGE";
    public static final String ACTION_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    
    // Inventory CRUD actions
    public static final String ACTION_ADD_ITEM = "ADD_ITEM";
    public static final String ACTION_UPDATE_ITEM = "UPDATE_ITEM";
    public static final String ACTION_DELETE_ITEM = "DELETE_ITEM";
    public static final String ACTION_LOAD_INVENTORY = "LOAD_INVENTORY";
    public static final String ACTION_SEARCH_INVENTORY = "SEARCH_INVENTORY";
    public static final String ACTION_VIEW_AUDIT_LOG = "VIEW_AUDIT_LOG";
    public static final String ACTION_REFRESH_AUDIT_LOG = "REFRESH_AUDIT_LOG";
    
    // Status constants
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_WARNING = "WARNING";
    public static final String STATUS_INFO = "INFO";
    
    /**
     * Initialize audit log file with header if it doesn't exist
     */
    public static void initializeAuditLog() {
        try {
            File file = new File(AUDIT_LOG_FILE);
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(HEADER + System.lineSeparator());
                }
                logger.info("Audit log file created: " + AUDIT_LOG_FILE);
            }
        } catch (IOException e) {
            logger.severe("Error initializing audit log file: " + e.getMessage());
        }
    }
    
    /**
     * Write audit log entry to file
     */
    public static void writeLog(AuditLog auditLog) {
        try {
            // Ensure audit log file exists
            initializeAuditLog();
            
            // Append log entry to file
            Files.write(
                Paths.get(AUDIT_LOG_FILE),
                (auditLog.toCSV() + System.lineSeparator()).getBytes(),
                StandardOpenOption.APPEND
            );
            
            // Also log to console for debugging
            logger.info("Audit Log: " + auditLog.toCSV());
            
        } catch (IOException e) {
            logger.severe("Error writing to audit log: " + e.getMessage());
        }
    }
    
    // =================================================================================
    // AUTHENTICATION LOGGING METHODS
    // =================================================================================
    
    /**
     * Log successful login
     */
    public static void logSuccessfulLogin(String username) {
        AuditLog log = new AuditLog(
            username,
            ACTION_LOGIN_SUCCESS,
            STATUS_SUCCESS,
            "User successfully logged in"
        );
        writeLog(log);
    }
    
    /**
     * Log failed login attempt
     */
    public static void logFailedLogin(String username, String reason) {
        AuditLog log = new AuditLog(
            username,
            ACTION_LOGIN_FAILED,
            STATUS_FAILED,
            "Login failed: " + reason
        );
        writeLog(log);
    }
    
    /**
     * Log logout
     */
    public static void logLogout(String username) {
        AuditLog log = new AuditLog(
            username,
            ACTION_LOGOUT,
            STATUS_SUCCESS,
            "User logged out"
        );
        writeLog(log);
    }
    
    /**
     * Log login attempt
     */
    public static void logLoginAttempt(String username) {
        AuditLog log = new AuditLog(
            username,
            ACTION_LOGIN_ATTEMPT,
            STATUS_INFO,
            "Login attempt initiated"
        );
        writeLog(log);
    }
    
    /**
     * Log password change
     */
    public static void logPasswordChange(String username) {
        AuditLog log = new AuditLog(
            username,
            ACTION_PASSWORD_CHANGE,
            STATUS_SUCCESS,
            "Password changed successfully"
        );
        writeLog(log);
    }
    
    /**
     * Log account locked
     */
    public static void logAccountLocked(String username, String reason) {
        AuditLog log = new AuditLog(
            username,
            ACTION_ACCOUNT_LOCKED,
            STATUS_WARNING,
            "Account locked: " + reason
        );
        writeLog(log);
    }
    
    // =================================================================================
    // INVENTORY CRUD LOGGING METHODS
    // =================================================================================
    
    /**
     * Log general inventory actions (CRUD operations, searches, etc.)
     */
    public static void logInventoryAction(String username, String action, String status, String details) {
        AuditLog log = new AuditLog(
            username,
            action,
            status,
            details
        );
        writeLog(log);
    }
    
    /**
     * Log item addition
     */
    public static void logAddItem(String username, String itemId, String itemName, String category, String quantity) {
        AuditLog log = new AuditLog(
            username,
            ACTION_ADD_ITEM,
            STATUS_SUCCESS,
            String.format("Added new item: ID=%s, Name=%s, Category=%s, Quantity=%s", itemId, itemName, category, quantity)
        );
        writeLog(log);
    }
    
    /**
     * Log item update
     */
    public static void logUpdateItem(String username, String oldItemId, String oldItemName, String newItemId, String newItemName, String category, String quantity) {
        AuditLog log = new AuditLog(
            username,
            ACTION_UPDATE_ITEM,
            STATUS_SUCCESS,
            String.format("Updated item: Original[ID=%s, Name=%s] -> New[ID=%s, Name=%s, Category=%s, Quantity=%s]", 
                oldItemId, oldItemName, newItemId, newItemName, category, quantity)
        );
        writeLog(log);
    }
    
    /**
     * Log item deletion
     */
    public static void logDeleteItem(String username, String itemId, String itemName, String category, String quantity) {
        AuditLog log = new AuditLog(
            username,
            ACTION_DELETE_ITEM,
            STATUS_SUCCESS,
            String.format("Deleted item: ID=%s, Name=%s, Category=%s, Quantity=%s", itemId, itemName, category, quantity)
        );
        writeLog(log);
    }
    
    /**
     * Log inventory data loading
     */
    public static void logLoadInventory(String username, int itemCount) {
        AuditLog log = new AuditLog(
            username,
            ACTION_LOAD_INVENTORY,
            STATUS_SUCCESS,
            String.format("Loaded inventory data: %d items loaded from file", itemCount)
        );
        writeLog(log);
    }
    
    /**
     * Log inventory search
     */
    public static void logSearchInventory(String username, String keyword, int foundItems, int totalItems) {
        AuditLog log = new AuditLog(
            username,
            ACTION_SEARCH_INVENTORY,
            STATUS_SUCCESS,
            String.format("Searched for keyword: '%s' - Found %d items out of %d total", keyword, foundItems, totalItems)
        );
        writeLog(log);
    }
    
    /**
     * Log audit log access
     */
    public static void logViewAuditLog(String username) {
        AuditLog log = new AuditLog(
            username,
            ACTION_VIEW_AUDIT_LOG,
            STATUS_SUCCESS,
            "Accessed audit log viewer"
        );
        writeLog(log);
    }
    
    /**
     * Log audit log refresh
     */
    public static void logRefreshAuditLog(String username) {
        AuditLog log = new AuditLog(
            username,
            ACTION_REFRESH_AUDIT_LOG,
            STATUS_SUCCESS,
            "Refreshed audit log data manually"
        );
        writeLog(log);
    }
    
    /**
     * Get audit log file path
     */
    public static String getAuditLogFilePath() {
        return new File(AUDIT_LOG_FILE).getAbsolutePath();
    }
    
    // Add this method to your AuditLogger class
public static void flush() {
    // If you're using a FileWriter or BufferedWriter in AuditLogger,
    // make sure to flush it. Example:
    
    // If using static FileWriter/BufferedWriter:
    // if (writer != null) {
    //     try {
    //         writer.flush();
    //     } catch (IOException e) {
    //         // Handle error
    //     }
    // }
    
    // Or if you're opening/closing the file each time (which is safer):
    // No action needed, but ensure proper file closing in your log methods
}
}