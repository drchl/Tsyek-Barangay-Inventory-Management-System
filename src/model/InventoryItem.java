package model;

/**
 * InventoryItem - Data model for inventory items
 * @author user
 */
public class InventoryItem {
    private String itemId;
    private String itemName;
    private String category;
    private int quantity;
    private String unit;
    private String location;
    private String condition;
    private String dateAcquired;

    // Default constructor
    public InventoryItem() {
    }

    // Constructor with all fields
    public InventoryItem(String itemId, String itemName, String category, 
                        int quantity, String unit, String location, 
                        String condition, String dateAcquired) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.condition = condition;
        this.dateAcquired = dateAcquired;
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(String dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    // Convert to Object array for JTable
    public Object[] toTableRow() {
        return new Object[]{
            itemId, itemName, category, quantity, 
            unit, location, condition, dateAcquired
        };
    }

    // Create from Object array
    public static InventoryItem fromTableRow(Object[] row) {
        InventoryItem item = new InventoryItem();
        item.setItemId(row[0] != null ? row[0].toString() : "");
        item.setItemName(row[1] != null ? row[1].toString() : "");
        item.setCategory(row[2] != null ? row[2].toString() : "");
        try {
            item.setQuantity(row[3] != null ? Integer.parseInt(row[3].toString()) : 0);
        } catch (NumberFormatException e) {
            item.setQuantity(0);
        }
        item.setUnit(row[4] != null ? row[4].toString() : "");
        item.setLocation(row[5] != null ? row[5].toString() : "");
        item.setCondition(row[6] != null ? row[6].toString() : "");
        item.setDateAcquired(row[7] != null ? row[7].toString() : "");
        return item;
    }

    // Convert to CSV line
    public String toCsvLine() {
        return String.format("\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\"",
            escapeQuotes(itemId), escapeQuotes(itemName), escapeQuotes(category),
            quantity, escapeQuotes(unit), escapeQuotes(location), 
            escapeQuotes(condition), escapeQuotes(dateAcquired));
    }

    // Create from CSV line
    public static InventoryItem fromCsvLine(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
        if (parts.length >= 8) {
            InventoryItem item = new InventoryItem();
            item.setItemId(parts[0]);
            item.setItemName(parts[1]);
            item.setCategory(parts[2]);
            try {
                item.setQuantity(Integer.parseInt(parts[3]));
            } catch (NumberFormatException e) {
                item.setQuantity(0);
            }
            item.setUnit(parts[4]);
            item.setLocation(parts[5]);
            item.setCondition(parts[6]);
            item.setDateAcquired(parts[7]);
            return item;
        }
        return null;
    }

    // Helper method to escape quotes in CSV
    private String escapeQuotes(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    // Simple CSV parser
    private static String[] parseCsvLine(String csvLine) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < csvLine.length() && csvLine.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString());
        
        return result.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", location='" + location + '\'' +
                ", condition='" + condition + '\'' +
                ", dateAcquired='" + dateAcquired + '\'' +
                '}';
    }
}