package util;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.logging.Logger;

/**
 * InventoryFileManager - Handles CSV file operations for inventory data
 * @author user
 */
public class InventoryFileManager {
    private static final String CSV_FILE = "inventory_data.csv";
    private static final Logger logger = Logger.getLogger(InventoryFileManager.class.getName());
    
    /**
     * Save table data to CSV file
     */
    public static void saveToFile(DefaultTableModel model) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write header
            writer.println("Item ID,Item Name,Category,Quantity,Unit,Location,Condition,Date Acquired");
            
            // Write data rows
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    String valueStr = (value != null) ? value.toString() : "";
                    
                    // Escape commas in data by wrapping in quotes
                    if (valueStr.contains(",")) {
                        valueStr = "\"" + valueStr + "\"";
                    }
                    
                    row.append(valueStr);
                    if (j < model.getColumnCount() - 1) {
                        row.append(",");
                    }
                }
                writer.println(row.toString());
            }
            
            logger.info("Data saved successfully to " + CSV_FILE);
            
        } catch (IOException e) {
            logger.severe("Error saving to file: " + e.getMessage());
            throw new RuntimeException("Failed to save data to file", e);
        }
    }
    
    /**
     * Load table data from CSV file
     */
    public static void loadFromFile(DefaultTableModel model) {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            logger.info("CSV file does not exist yet. Starting with empty table.");
            return; // No file to load, start with empty table
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header line
            
            if (line == null) {
                logger.info("CSV file is empty.");
                return;
            }
            
            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                
                String[] data = parseCSVLine(line);
                
                if (data.length >= 8) {
                    Object[] row = new Object[8];
                    row[0] = data[0]; // Item_ID
                    row[1] = data[1]; // Item_Name
                    row[2] = data[2]; // Category
                    
                    // Parse quantity safely
                    try {
                        row[3] = data[3].isEmpty() ? 0 : Integer.parseInt(data[3].trim());
                    } catch (NumberFormatException e) {
                        row[3] = 0; // Default to 0 if parsing fails
                    }
                    
                    row[4] = data[4]; // Unit
                    row[5] = data[5]; // Location
                    row[6] = data[6]; // Condition
                    row[7] = data[7]; // Date_Acquired
                    
                    model.addRow(row);
                    rowCount++;
                }
            }
            
            logger.info("Loaded " + rowCount + " rows from " + CSV_FILE);
            
        } catch (IOException e) {
            logger.severe("Error loading from file: " + e.getMessage());
            throw new RuntimeException("Failed to load data from file", e);
        }
    }
    
    /**
     * Parse a CSV line handling quoted values
     */
    private static String[] parseCSVLine(String line) {
        String[] result = new String[8];
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int fieldIndex = 0;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result[fieldIndex] = current.toString();
                current.setLength(0);
                fieldIndex++;
                if (fieldIndex >= 8) break;
            } else {
                current.append(c);
            }
        }
        
        // Add the last field
        if (fieldIndex < 8) {
            result[fieldIndex] = current.toString();
        }
        
        // Fill remaining fields with empty strings
        for (int i = fieldIndex + 1; i < 8; i++) {
            result[i] = "";
        }
        
        return result;
    }
    
    /**
     * Check if CSV file exists
     */
    public static boolean fileExists() {
        return new File(CSV_FILE).exists();
    }
    
    /**
     * Get CSV file path
     */
    public static String getFilePath() {
        return new File(CSV_FILE).getAbsolutePath();
    }
}