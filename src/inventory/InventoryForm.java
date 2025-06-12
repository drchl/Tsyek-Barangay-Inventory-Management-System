package inventory;

import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import loginSignup.logIn;
import util.InventoryFileManager;

/**
 * InventoryForm - Main inventory management interface
 * @author user
 */
public class InventoryForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InventoryForm.class.getName());
    private TableRowSorter<DefaultTableModel> sorter;
    private boolean isUpdating = false;

    /**
     * Creates new form InventoryForm
     */
    public InventoryForm(String username) {
        initComponents();
        
        // Custom initialization after NetBeans init
        setupForm(username);
        setupTable();
        setupTableSelection();
        loadInventoryData();
        
        // Final UI adjustments after everything is loaded
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Refresh the scroll pane to ensure custom UI is properly applied
            jScrollPane1.revalidate();
            jScrollPane1.repaint();
        });
    }
    
    /**
     * Setup form UI and logout functionality
     */
    private void setupForm(String username) {
        // Set username
        jLabel11.setText(username);
        
        // Setup input field formatting and validation
        setupInputFormatting();
        
        // Setup logout functionality
        Color defaultLabel12Bg = new Color(4, 63, 106);
        Color hoverLabel12Bg = new Color(4, 50, 86);

        jLabel13.setBackground(hoverLabel12Bg);
        jLabel13.setOpaque(true);

        jLabel12.setBackground(defaultLabel12Bg);
        jLabel12.setOpaque(true);
        jLabel12.setForeground(Color.WHITE);
        
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12.setBackground(hoverLabel12Bg);
                jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                jLabel12.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12.setBackground(defaultLabel12Bg);
                jLabel12.setForeground(Color.WHITE);
                jLabel12.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int result = JOptionPane.showConfirmDialog(
                    InventoryForm.this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    new logIn().setVisible(true);
                }
            }
        });
    }
    
    /**
     * Setup input formatting and real-time validation
     */
    private void setupInputFormatting() {
        // Add tooltips for better user guidance
        itemID.setToolTipText("Enter numeric ID (e.g., 001, 123, 1234)");
        itemName.setToolTipText("Enter item name (e.g., chair, laptop, pen)");
        category.setToolTipText("Choose: Equipment, Medical, Office, Event, or select 'Other...' to add custom category");
        quantity.setToolTipText("Enter positive number (e.g., 1, 5, 10)");
        unit.setToolTipText("Choose: pcs, ml, l, kg, lbs, or select 'Other...' to add custom unit");
        location.setToolTipText("Enter location (e.g., Garage, Room 101, Storage)");
        condition.setToolTipText("Choose: Unused, Used, Expired, or select 'Other...' to add custom condition");
        dateAcquired.setToolTipText("Enter date as DD/MM/YY (e.g., 15/03/24)");
        
        // Item ID: Only allow numbers
        itemID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume(); // Don't allow non-numeric input
                }
                // Limit to 6 digits
                if (itemID.getText().length() >= 6 && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
        
        // Quantity: Only allow numbers
        quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume(); // Don't allow non-numeric input
                }
                // Limit to 5 digits
                if (quantity.getText().length() >= 5 && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
        
        // Date: Auto-format as DD/MM/YY
        dateAcquired.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                String text = dateAcquired.getText();
                
                // Only allow digits and backspace
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                    return;
                }
                
                // Auto-format with slashes
                if (Character.isDigit(c)) {
                    if (text.length() == 2 || text.length() == 5) {
                        dateAcquired.setText(text + "/");
                    }
                    // Limit to 8 characters (DD/MM/YY)
                    if (text.length() >= 8) {
                        evt.consume();
                    }
                }
            }
        });
        
        // Category: Provide suggestions with dropdown-like behavior and custom option
        category.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JTextField textField = (JTextField) category.getEditor().getEditorComponent();
                String text = textField.getText();
                if (text.isEmpty()) return;
                
                String lowerText = text.toLowerCase();
                String[] suggestions = {"Equipment", "Medical", "Office", "Event", "Other..."};
                
                // Clear existing items and add matching suggestions
                category.removeAllItems();
                boolean foundMatch = false;
                
                for (String suggestion : suggestions) {
                    if (suggestion.toLowerCase().startsWith(lowerText)) {
                        category.addItem(suggestion);
                        if (!foundMatch) {
                            // Set the first match as selected and highlight the remaining text
                            textField.setText(suggestion);
                            textField.setSelectionStart(text.length());
                            textField.setSelectionEnd(suggestion.length());
                            foundMatch = true;
                        }
                    }
                }
                
                // If no matches found, add all items back
                if (!foundMatch) {
                    for (String suggestion : suggestions) {
                        category.addItem(suggestion);
                    }
                }
                
                category.showPopup();
            }
        });
        
        // Add action listener for "Other..." selection in category
        category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("Other...".equals(category.getSelectedItem())) {
                    handleCustomCategoryInput();
                }
            }
        });

        // Unit: Provide suggestions with dropdown-like behavior and custom option
        unit.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JTextField textField = (JTextField) unit.getEditor().getEditorComponent();
                String text = textField.getText();
                if (text.isEmpty()) return;
                
                String lowerText = text.toLowerCase();
                String[] suggestions = {"pcs", "ml", "l", "kg", "lbs", "Other..."};
                
                // Clear existing items and add matching suggestions
                unit.removeAllItems();
                boolean foundMatch = false;
                
                for (String suggestion : suggestions) {
                    if (suggestion.toLowerCase().startsWith(lowerText)) {
                        unit.addItem(suggestion);
                        if (!foundMatch) {
                            // Set the first match as selected and highlight the remaining text
                            textField.setText(suggestion);
                            textField.setSelectionStart(text.length());
                            textField.setSelectionEnd(suggestion.length());
                            foundMatch = true;
                        }
                    }
                }
                
                // If no matches found, add all items back
                if (!foundMatch) {
                    for (String suggestion : suggestions) {
                        unit.addItem(suggestion);
                    }
                }
                
                unit.showPopup();
            }
        });
        
        // Add action listener for "Other..." selection in unit
        unit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("Other...".equals(unit.getSelectedItem())) {
                    handleCustomUnitInput();
                }
            }
        });

        // Condition: Provide suggestions with dropdown-like behavior and custom option
        condition.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JTextField textField = (JTextField) condition.getEditor().getEditorComponent();
                String text = textField.getText();
                if (text.isEmpty()) return;
                
                String lowerText = text.toLowerCase();
                String[] suggestions = {"Unused", "Used", "Expired", "Other..."};
                
                // Clear existing items and add matching suggestions
                condition.removeAllItems();
                boolean foundMatch = false;
                
                for (String suggestion : suggestions) {
                    if (suggestion.toLowerCase().startsWith(lowerText)) {
                        condition.addItem(suggestion);
                        if (!foundMatch) {
                            // Set the first match as selected and highlight the remaining text
                            textField.setText(suggestion);
                            textField.setSelectionStart(text.length());
                            textField.setSelectionEnd(suggestion.length());
                            foundMatch = true;
                        }
                    }
                }
                
                // If no matches found, add all items back
                if (!foundMatch) {
                    for (String suggestion : suggestions) {
                        condition.addItem(suggestion);
                    }
                }
                
                condition.showPopup();
            }
        });
        
        // Add action listener for "Other..." selection in condition
        condition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("Other...".equals(condition.getSelectedItem())) {
                    handleCustomConditionInput();
                }
            }
        });
        
        // Item Name: Limit length and capitalize first letter
        itemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (itemName.getText().length() >= 50 && evt.getKeyChar() != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
            
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = itemName.getText();
                if (!text.isEmpty() && Character.isLowerCase(text.charAt(0))) {
                    itemName.setText(Character.toUpperCase(text.charAt(0)) + text.substring(1));
                }
            }
        });
        
        // Location: Limit length and capitalize first letter
        location.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (location.getText().length() >= 30 && evt.getKeyChar() != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
            
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = location.getText();
                if (!text.isEmpty() && Character.isLowerCase(text.charAt(0))) {
                    location.setText(Character.toUpperCase(text.charAt(0)) + text.substring(1));
                }
            }
        });
    }
    
    /**
     * Setup table styling and search functionality
     */
    private void setupTable() {
        // Setup table header styling
        JTableHeader header = inventoryTable.getTableHeader();
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

                label.setBackground(Color.decode("#073559"));
                label.setForeground(java.awt.Color.WHITE);
                label.setOpaque(true);
                label.setHorizontalAlignment(javax.swing.JLabel.CENTER);

                return label;
            }
        });
        
        // Setup table sorter for search functionality
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        sorter = new TableRowSorter<>(model);
        inventoryTable.setRowSorter(sorter);
        
        // Set selection mode to single row
        inventoryTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        // Setup custom scrollbar
        setupCustomScrollBar();
        
        // Setup search field styling and placeholder behavior
        setupSearchField();
    }
    
    /**
     * Setup custom scrollbar for the inventory table
     */
    private void setupCustomScrollBar() {
        // Apply modern scrollbar UI to both vertical and horizontal scrollbars
        jScrollPane1.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        // Set scrollbar policies for better appearance
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Style the scroll pane border to match the modern theme
        jScrollPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        
        // Set scrollbar width for better visibility and modern appearance
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(14, 0));
        jScrollPane1.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 14));
        
        // Enhance table appearance to complement the modern scrollbar
        inventoryTable.setRowHeight(25); // Slightly taller rows for better readability
        inventoryTable.setShowGrid(true);
        inventoryTable.setGridColor(new Color(220, 220, 220)); // Subtle grid lines
        inventoryTable.setSelectionBackground(new Color(70, 130, 180, 50)); // Subtle selection highlight
        inventoryTable.setSelectionForeground(Color.BLACK);
        
        // Remove focus border from table
        inventoryTable.setFocusable(false);
        
        // Set scroll speed for smoother scrolling
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);
    }
    
    /**
     * Setup search field with placeholder text and styling
     */
    private void setupSearchField() {
        // Set placeholder text
        jTextField1.setText("Search items...");
        jTextField1.setForeground(Color.GRAY);
        
        // Add focus listeners for placeholder behavior
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextField1.getText().equals("Search items...")) {
                    jTextField1.setText("");
                    jTextField1.setForeground(Color.BLACK);
                }
                jTextField1.setBackground(new Color(255, 255, 200)); // Light yellow highlight
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextField1.getText().isEmpty()) {
                    jTextField1.setText("Search items...");
                    jTextField1.setForeground(Color.GRAY);
                }
                jTextField1.setBackground(Color.WHITE); // Reset to white
            }
        });
        
        // Add key listener for real-time search
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = jTextField1.getText();
                if (!text.equals("Search items...") && !text.isEmpty()) {
                    // Highlight search field when actively searching
                    jTextField1.setBackground(new Color(200, 255, 200)); // Light green
                } else if (text.isEmpty()) {
                    jTextField1.setBackground(Color.WHITE);
                }
            }
        });
    }
    
    /**
     * Setup table row selection to populate form fields
     */
    private void setupTableSelection() {
        inventoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !isUpdating) {
                    int selectedRow = inventoryTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        populateFields(selectedRow);
                    }
                }
            }
        });
    }
    
    /**
     * Populate form fields with data from selected table row
     */
    private void populateFields(int row) {
        try {
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            
            // Convert view row index to model row index (important for sorted tables)
            int modelRow = inventoryTable.convertRowIndexToModel(row);
            
            // Populate text fields
            itemID.setText(getValueAsString(model.getValueAt(modelRow, 0)));
            itemName.setText(getValueAsString(model.getValueAt(modelRow, 1)));
            quantity.setText(getValueAsString(model.getValueAt(modelRow, 3)));
            location.setText(getValueAsString(model.getValueAt(modelRow, 5)));
            dateAcquired.setText(getValueAsString(model.getValueAt(modelRow, 7)));
            
            // Populate combo boxes
            category.setSelectedItem(getValueAsString(model.getValueAt(modelRow, 2)));
            unit.setSelectedItem(getValueAsString(model.getValueAt(modelRow, 4)));
            condition.setSelectedItem(getValueAsString(model.getValueAt(modelRow, 6)));
            
        } catch (Exception ex) {
            logger.warning("Error populating fields: " + ex.getMessage());
        }
    }
    
    /**
     * Convert object to string safely
     */
    private String getValueAsString(Object value) {
        return value != null ? value.toString() : "";
    }
    
    /**
     * Load inventory data from CSV file
     */
    private void loadInventoryData() {
        try {
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            InventoryFileManager.loadFromFile(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading inventory data: " + ex.getMessage(), 
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
            logger.severe("Error loading inventory data: " + ex.getMessage());
        }
    }
    
    /**
     * Validate input fields with comprehensive format checking
     */
    private boolean validateInput() {
        // Validate Item ID
        if (!validateItemID()) return false;
        
        // Validate Item Name
        if (!validateItemName()) return false;
        
        // Validate Category
        if (!validateCategory()) return false;
        
        // Validate Quantity
        if (!validateQuantity()) return false;
        
        // Validate Unit
        if (!validateUnit()) return false;
        
        // Validate Location
        if (!validateLocation()) return false;
        
        // Validate Condition
        if (!validateCondition()) return false;
        
        // Validate Date
        if (!validateDate()) return false;
        
        return true;
    }
    
    /**
     * Validate Item ID format (should be numeric, e.g., 001, 123)
     */
    private boolean validateItemID() {
        String id = itemID.getText().trim();
        if (id.isEmpty()) {
            showValidationError("Item ID is required!", itemID);
            return false;
        }
        
        if (!id.matches("\\d{1,6}")) {
            showValidationError("Item ID must be numeric (e.g., 001, 123, 1234)!", itemID);
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate Item Name (should not be empty, reasonable length)
     */
    private boolean validateItemName() {
        String name = itemName.getText().trim();
        if (name.isEmpty()) {
            showValidationError("Item Name is required!", itemName);
            return false;
        }
        
        if (name.length() < 2) {
            showValidationError("Item Name must be at least 2 characters long!", itemName);
            return false;
        }
        
        if (name.length() > 50) {
            showValidationError("Item Name cannot exceed 50 characters!", itemName);
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate Category (must not be empty and not be "Other...")
     */
    private boolean validateCategory() {
        String categoryValue = getComboBoxValue(category);
        if (categoryValue.isEmpty()) {
            showValidationError("Category is required! Please select a category or choose 'Other...' to add a custom one", null);
            category.requestFocus();
            return false;
        }
        
        if ("Other...".equals(categoryValue)) {
            showValidationError("Please select a specific category or add a custom one", null);
            category.requestFocus();
            return false;
        }
        
        // Accept any non-empty value that's not "Other..."
        return true;
    }
    
    /**
     * Validate Quantity (must be positive integer)
     */
    private boolean validateQuantity() {
        String qty = quantity.getText().trim();
        if (qty.isEmpty()) {
            showValidationError("Quantity is required!", quantity);
            return false;
        }
        
        try {
            int qtyValue = Integer.parseInt(qty);
            if (qtyValue <= 0) {
                showValidationError("Quantity must be a positive number (e.g., 1, 5, 10)!", quantity);
                return false;
            }
            if (qtyValue > 10000) {
                showValidationError("Quantity cannot exceed 10,000!", quantity);
                return false;
            }
        } catch (NumberFormatException e) {
            showValidationError("Quantity must be a valid number (e.g., 1, 5, 10)!", quantity);
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate Unit (must not be empty and not be "Other...")
     */
    private boolean validateUnit() {
        String unitValue = getComboBoxValue(unit);
        if (unitValue.isEmpty()) {
            showValidationError("Unit is required! Please select a unit or choose 'Other...' to add a custom one", null);
            unit.requestFocus();
            return false;
        }
        
        if ("Other...".equals(unitValue)) {
            showValidationError("Please select a specific unit or add a custom one", null);
            unit.requestFocus();
            return false;
        }
        
        // Accept any non-empty value that's not "Other..."
        return true;
    }
    
    /**
     * Validate Location (should not be empty, reasonable length)
     */
    private boolean validateLocation() {
        String loc = location.getText().trim();
        if (loc.isEmpty()) {
            showValidationError("Location is required!", location);
            return false;
        }
        
        if (loc.length() < 2) {
            showValidationError("Location must be at least 2 characters long!", location);
            return false;
        }
        
        if (loc.length() > 30) {
            showValidationError("Location cannot exceed 30 characters!", location);
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate Condition (must not be empty and not be "Other...")
     */
    private boolean validateCondition() {
        String conditionValue = getComboBoxValue(condition);
        if (conditionValue.isEmpty()) {
            showValidationError("Condition is required! Please select a condition or choose 'Other...' to add a custom one", null);
            condition.requestFocus();
            return false;
        }
        
        if ("Other...".equals(conditionValue)) {
            showValidationError("Please select a specific condition or add a custom one", null);
            condition.requestFocus();
            return false;
        }
        
        // Accept any non-empty value that's not "Other..."
        return true;
    }
    
    /**
     * Validate Date format (DD/MM/YY)
     */
    private boolean validateDate() {
        String dateText = dateAcquired.getText().trim();
        if (dateText.isEmpty()) {
            showValidationError("Date Acquired is required!", dateAcquired);
            return false;
        }
        
        if (!dateText.matches("\\d{2}/\\d{2}/\\d{2}")) {
            showValidationError("Date must be in DD/MM/YY format (e.g., 15/03/24)!", dateAcquired);
            return false;
        }
        
        // Additional date validation
        String[] parts = dateText.split("/");
        try {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            
            if (day < 1 || day > 31) {
                showValidationError("Day must be between 01 and 31!", dateAcquired);
                return false;
            }
            
            if (month < 1 || month > 12) {
                showValidationError("Month must be between 01 and 12!", dateAcquired);
                return false;
            }
            
            if (year < 0 || year > 99) {
                showValidationError("Year must be between 00 and 99!", dateAcquired);
                return false;
            }
            
        } catch (NumberFormatException e) {
            showValidationError("Invalid date format! Use DD/MM/YY (e.g., 15/03/24)", dateAcquired);
            return false;
        }
        
        return true;
    }
    
    /**
     * Show validation error message and focus the problematic field
     */
    private void showValidationError(String message, javax.swing.JTextField field) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
        if (field != null) {
            field.requestFocus();
            field.selectAll(); // Highlight the text for easy correction
        }
    }
    
    /**
     * Clear all input fields
     */
    private void clearFields() {
        itemID.setText("");
        itemName.setText("");
        quantity.setText("");
        location.setText("");
        dateAcquired.setText("");
        
        // Clear combo boxes by setting to first item or empty
        category.setSelectedIndex(-1);
        unit.setSelectedIndex(-1);
        condition.setSelectedIndex(-1);
        
        // Clear table selection
        inventoryTable.clearSelection();
    }
    
    /**
     * Check for duplicate Item ID
     */
    private boolean isDuplicateItemId(String itemId, int excludeRow) {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            if (i != excludeRow) {
                Object value = model.getValueAt(i, 0);
                if (value != null && value.toString().equals(itemId)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get combo box value safely
     */
    private String getComboBoxValue(JComboBox<String> comboBox) {
        if (comboBox == null) {
            return "";
        }
        Object selected = comboBox.getSelectedItem();
        return selected != null ? selected.toString() : "";
    }
    
    /**
     * Handle custom category input
     */
    private void handleCustomCategoryInput() {
        String customCategory = JOptionPane.showInputDialog(this, 
            "Enter custom category:", 
            "Custom Category", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (customCategory != null && !customCategory.trim().isEmpty()) {
            customCategory = customCategory.trim();
            // Capitalize first letter
            customCategory = Character.toUpperCase(customCategory.charAt(0)) + customCategory.substring(1);
            
            // Check if it doesn't already exist
            boolean exists = false;
            for (int i = 0; i < category.getItemCount(); i++) {
                if (category.getItemAt(i).equalsIgnoreCase(customCategory)) {
                    exists = true;
                    break;
                }
            }
            
            if (!exists) {
                // Add to dropdown (before "Other...")
                category.removeItem("Other...");
                category.addItem(customCategory);
                category.addItem("Other...");
            }
            
            category.setSelectedItem(customCategory);
        } else {
            // User cancelled or entered empty, reset to first item
            category.setSelectedIndex(0);
        }
    }
    
    /**
     * Handle custom unit input
     */
    private void handleCustomUnitInput() {
        String customUnit = JOptionPane.showInputDialog(this, 
            "Enter custom unit (e.g., boxes, meters, gallons):", 
            "Custom Unit", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (customUnit != null && !customUnit.trim().isEmpty()) {
            customUnit = customUnit.trim().toLowerCase(); // Units typically lowercase
            
            // Check if it doesn't already exist
            boolean exists = false;
            for (int i = 0; i < unit.getItemCount(); i++) {
                if (unit.getItemAt(i).equalsIgnoreCase(customUnit)) {
                    exists = true;
                    break;
                }
            }
            
            if (!exists) {
                // Add to dropdown (before "Other...")
                unit.removeItem("Other...");
                unit.addItem(customUnit);
                unit.addItem("Other...");
            }
            
            unit.setSelectedItem(customUnit);
        } else {
            // User cancelled or entered empty, reset to first item
            unit.setSelectedIndex(0);
        }
    }
    
    /**
     * Handle custom condition input
     */
    private void handleCustomConditionInput() {
        String customCondition = JOptionPane.showInputDialog(this, 
            "Enter custom condition (e.g., Damaged, Refurbished, New):", 
            "Custom Condition", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (customCondition != null && !customCondition.trim().isEmpty()) {
            customCondition = customCondition.trim();
            // Capitalize first letter
            customCondition = Character.toUpperCase(customCondition.charAt(0)) + customCondition.substring(1);
            
            // Check if it doesn't already exist
            boolean exists = false;
            for (int i = 0; i < condition.getItemCount(); i++) {
                if (condition.getItemAt(i).equalsIgnoreCase(customCondition)) {
                    exists = true;
                    break;
                }
            }
            
            if (!exists) {
                // Add to dropdown (before "Other...")
                condition.removeItem("Other...");
                condition.addItem(customCondition);
                condition.addItem("Other...");
            }
            
            condition.setSelectedItem(customCondition);
        } else {
            // User cancelled or entered empty, reset to first item
            condition.setSelectedIndex(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        background = new javax.swing.JPanel();
        panel1 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        inventoryBg = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        search = new javax.swing.JButton();
        unit = new javax.swing.JComboBox<>();
        dateAcquired = new javax.swing.JTextField();
        itemID = new javax.swing.JTextField();
        itemName = new javax.swing.JTextField();
        location = new javax.swing.JTextField();
        category = new javax.swing.JComboBox<>();
        condition = new javax.swing.JComboBox<>();
        quantity = new javax.swing.JTextField();
        delete = new javax.swing.JButton();
        add = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1440, 1024));
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));

        background.setBackground(new java.awt.Color(9, 54, 96));
        background.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        background.setMaximumSize(new java.awt.Dimension(1440, 1024));
        background.setMinimumSize(new java.awt.Dimension(1440, 1024));

        panel1.setBackground(new java.awt.Color(50, 110, 147));
        panel1.setPreferredSize(new java.awt.Dimension(900, 800));
        panel1.setRequestFocusEnabled(false);
        panel1.setLayout(null);

        title.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setText("INVENTORY SYSTEM");
        panel1.add(title);
        title.setBounds(390, 40, 380, 44);

        inventoryBg.setBackground(new java.awt.Color(72, 128, 161));

        inventoryTable.setAutoCreateRowSorter(true);
        inventoryTable.setBackground(new java.awt.Color(217, 217, 217));
        inventoryTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "Category", "Quantity", "Unit", "Location", "Condition", "Date Acquired"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        inventoryTable.setShowGrid(true);
        jScrollPane1.setViewportView(inventoryTable);

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        search.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventoryBgLayout = new javax.swing.GroupLayout(inventoryBg);
        inventoryBg.setLayout(inventoryBgLayout);
        inventoryBgLayout.setHorizontalGroup(
            inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryBgLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1049, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(inventoryBgLayout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        inventoryBgLayout.setVerticalGroup(
            inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryBgLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        panel1.add(inventoryBg);
        inventoryBg.setBounds(0, 125, 1120, 410);
        
        unit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        unit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pcs", "ml", "l", "kg", "lbs", "Other..." }));
        unit.setEditable(true);
        panel1.add(unit);
        unit.setBounds(200, 675, 170, 45);

        dateAcquired.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateAcquired.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateAcquiredActionPerformed(evt);
            }
        });
        panel1.add(dateAcquired);
        dateAcquired.setBounds(780, 675, 170, 45);

        itemID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemIDActionPerformed(evt);
            }
        });
        panel1.add(itemID);
        itemID.setBounds(200, 595, 170, 45);

        itemName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNameActionPerformed(evt);
            }
        });
        panel1.add(itemName);
        itemName.setBounds(390, 595, 170, 45);
        
        location.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panel1.add(location);
        location.setBounds(390, 675, 170, 45);

        category.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Equipment", "Medical", "Office", "Event", "Other..." }));
        category.setEditable(true);
        panel1.add(category);
        category.setBounds(580, 595, 170, 45);
        
        condition.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        condition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unused", "Used", "Expired", "Other..." }));
        condition.setEditable(true);
        panel1.add(condition);
        condition.setBounds(580, 675, 170, 45);

        quantity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityActionPerformed(evt);
            }
        });
        panel1.add(quantity);
        quantity.setBounds(780, 595, 170, 45);

        delete.setBackground(new java.awt.Color(9, 54, 96));
        delete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setText("DELETE");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        panel1.add(delete);
        delete.setBounds(600, 730, 110, 40);

        add.setBackground(new java.awt.Color(9, 54, 96));
        add.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setText("ADD");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        panel1.add(add);
        add.setBounds(210, 730, 110, 40);

        clear.setBackground(new java.awt.Color(9, 54, 96));
        clear.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        clear.setForeground(new java.awt.Color(255, 255, 255));
        clear.setText("CLEAR");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        panel1.add(clear);
        clear.setBounds(800, 730, 110, 40);

        update.setBackground(new java.awt.Color(9, 54, 96));
        update.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        update.setForeground(new java.awt.Color(255, 255, 255));
        update.setText("UPDATE");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        panel1.add(update);
        update.setBounds(410, 730, 110, 40);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quantity");
        panel1.add(jLabel1);
        jLabel1.setBounds(780, 570, 80, 25);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Date Acquired");
        panel1.add(jLabel2);
        jLabel2.setBounds(780, 650, 120, 25);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Item Name");
        panel1.add(jLabel3);
        jLabel3.setBounds(390, 570, 100, 25);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Category");
        panel1.add(jLabel4);
        jLabel4.setBounds(580, 570, 80, 25);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Item ID");
        panel1.add(jLabel5);
        jLabel5.setBounds(200, 570, 70, 25);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Unit");
        panel1.add(jLabel6);
        jLabel6.setBounds(200, 650, 50, 25);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Location");
        panel1.add(jLabel7);
        jLabel7.setBounds(390, 650, 80, 25);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Condition");
        panel1.add(jLabel8);
        jLabel8.setBounds(580, 650, 90, 25);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(4, 63, 106));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Welcome,");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Admin");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Log out");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel12.setOpaque(true);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Inventory");
        jLabel13.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(98, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel11)))
                .addGap(84, 84, 84))
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(79, 79, 79)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(401, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(938, 938, 938))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(201, 201, 201))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(90, Short.MAX_VALUE))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    // =================================================================================
    // EVENT HANDLERS - These match your NetBeans button configurations
    // =================================================================================

    private void categoryActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // Combo box action - no special handling needed
    }                                        

    private void dateAcquiredActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {                                       
        String keyword = jTextField1.getText().trim();
        
        // Don't search if placeholder text is showing
        if (keyword.equals("Search items...")) {
            keyword = "";
        }
        
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null); // Show all rows
            if (!jTextField1.getText().equals("Search items...")) {
                JOptionPane.showMessageDialog(this, 
                    "Search cleared. Showing all items.", 
                    "Search", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword)); // Case-insensitive search
            
            // Show feedback about search results
            int visibleRows = inventoryTable.getRowCount();
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            int totalRows = model.getRowCount();
            
            if (visibleRows == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No items found matching: \"" + keyword + "\"", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else if (visibleRows == 1) {
                // Highlight the single found row
                inventoryTable.setRowSelectionInterval(0, 0);
                JOptionPane.showMessageDialog(this, 
                    "Found 1 item matching: \"" + keyword + "\"", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Highlight the first row if multiple found
                inventoryTable.setRowSelectionInterval(0, 0);
                JOptionPane.showMessageDialog(this, 
                    "Found " + visibleRows + " items matching: \"" + keyword + "\"", 
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }                                      

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Don't trigger search if placeholder text is still showing
        if (!jTextField1.getText().equals("Search items...")) {
            searchActionPerformed(evt);
        }
    }                                           

    private void addActionPerformed(java.awt.event.ActionEvent evt) {                                    
        if (!validateInput()) {
            return;
        }
        
        try {
            String itemIdText = itemID.getText().trim();
            
            // Check for duplicate Item ID
            if (isDuplicateItemId(itemIdText, -1)) {
                JOptionPane.showMessageDialog(this, 
                    "Item ID already exists! Please use a different ID.", 
                    "Duplicate ID Error", 
                    JOptionPane.ERROR_MESSAGE);
                itemID.requestFocus();
                return;
            }
            
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            
            // Create new row data with null checks and safer parsing
            Object[] row = {
                itemID.getText().trim(),                    // Item ID
                itemName.getText().trim(),                  // Item Name
                getComboBoxValue(category),                 // Category
                parseQuantity(quantity.getText().trim()),   // Quantity (safer parsing)
                getComboBoxValue(unit),                     // Unit
                location.getText().trim(),                  // Location
                getComboBoxValue(condition),                // Condition
                dateAcquired.getText().trim()               // Date Acquired
            };
            
            // Add row to table
            model.addRow(row);
            
            // Save to file
            InventoryFileManager.saveToFile(model);
            
            // Clear form fields
            clearFields();
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Item added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for quantity.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
            quantity.requestFocus();
            quantity.selectAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error adding item: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Better debugging
            if (logger != null) {
                logger.severe("Error adding item: " + ex.getMessage());
            }
        }
    }

    // Helper method for safer quantity parsing
    private Integer parseQuantity(String quantityText) {
        if (quantityText == null || quantityText.isEmpty()) {
            return 0;
        }
        try {
            return Integer.valueOf(quantityText);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid quantity format: " + quantityText);
        }
    }                                

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if (!validateInput()) {
            return;
        }

        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a row to update.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            int modelRow = inventoryTable.convertRowIndexToModel(selectedRow);
            
            String newItemId = itemID.getText().trim();
            
            // Check for duplicate Item ID (excluding current row)
            if (isDuplicateItemId(newItemId, modelRow)) {
                JOptionPane.showMessageDialog(this, 
                    "Item ID already exists! Please use a different ID.", 
                    "Duplicate ID Error", 
                    JOptionPane.ERROR_MESSAGE);
                itemID.requestFocus();
                return;
            }

            isUpdating = true; // Prevent selection event during update
            
            // Update row data
            model.setValueAt(itemID.getText().trim(), modelRow, 0);                    // Item ID
            model.setValueAt(itemName.getText().trim(), modelRow, 1);                  // Item Name
            model.setValueAt(getComboBoxValue(category), modelRow, 2);                 // Category
            model.setValueAt(Integer.parseInt(quantity.getText().trim()), modelRow, 3); // Quantity
            model.setValueAt(getComboBoxValue(unit), modelRow, 4);                     // Unit
            model.setValueAt(location.getText().trim(), modelRow, 5);                  // Location
            model.setValueAt(getComboBoxValue(condition), modelRow, 6);                // Condition
            model.setValueAt(dateAcquired.getText().trim(), modelRow, 7);              // Date Acquired
            
            InventoryFileManager.saveToFile(model);
            clearFields();
            
            isUpdating = false;
            
            JOptionPane.showMessageDialog(this, 
                "Item updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            isUpdating = false;
            JOptionPane.showMessageDialog(this, 
                "Error updating item: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            logger.severe("Error updating item: " + ex.getMessage());
        }
    }                                      

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {                                      
        clearFields();
    }                                     

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {                                       
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a row to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            int modelRow = inventoryTable.convertRowIndexToModel(selectedRow);
            
            // Get item name for confirmation
            Object itemNameObj = model.getValueAt(modelRow, 1);
            String itemNameStr = itemNameObj != null ? itemNameObj.toString() : "Unknown Item";

            int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete item: " + itemNameStr + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                model.removeRow(modelRow);
                InventoryFileManager.saveToFile(model);
                clearFields();
                
                JOptionPane.showMessageDialog(this, 
                    "Item deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
                    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error deleting item: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            logger.severe("Error deleting item: " + ex.getMessage());
        }
    }                                      

    private void itemIDActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
    }                                      

    private void itemNameActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void quantityActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    /**
     * Main method for testing
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new InventoryForm("Admin").setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton add;
    private javax.swing.JPanel background;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JButton clear;
    private javax.swing.JComboBox<String> condition;
    private javax.swing.JTextField dateAcquired;
    private javax.swing.JButton delete;
    private javax.swing.JPanel inventoryBg;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JTextField itemID;
    private javax.swing.JTextField itemName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField location;
    private javax.swing.JPanel panel1;
    private javax.swing.JTextField quantity;
    private javax.swing.JButton search;
    private javax.swing.JLabel title;
    private javax.swing.JComboBox<String> unit;
    private javax.swing.JButton update;
    // End of variables declaration                   
}