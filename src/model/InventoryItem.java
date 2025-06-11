
package model;

import java.io.Serializable;


public class InventoryItem implements Serializable {
     private String itemId, itemName, category, unit, location, condition, dateAcquired;
    private int quantity;

    public InventoryItem(String itemId, String itemName, String category, int quantity,
                         String unit, String location, String condition, String dateAcquired) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.condition = condition;
        this.dateAcquired = dateAcquired;
    }
    
}
