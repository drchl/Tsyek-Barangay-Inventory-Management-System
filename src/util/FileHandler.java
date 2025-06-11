
package util;

import java.io.*;
import java.util.ArrayList;
import model.InventoryItem;



public class FileHandler {
     private static final String FILE_NAME = "inventory_data.dat";

    public static void saveToFile(ArrayList<InventoryItem> itemList) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        out.writeObject(itemList);
        out.close();
    }

    public static ArrayList<InventoryItem> loadFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME));
        ArrayList<InventoryItem> itemList = (ArrayList<InventoryItem>) in.readObject();
        in.close();
        return itemList;
    }
}
