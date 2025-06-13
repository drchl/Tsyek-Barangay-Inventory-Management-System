# Tsyek-Barangay-Inventory-Management-System

## Overview
Tsyek-Barangay-Inventory-Management-System is a proposed digital solution tailored to the operational needs of barangay offices. It offers a straightforward way for barangay staff to handle inventory records, making routine tasks simpler and faster. Through its centralized interface, it helps users record, organize, and update inventory data with minimal errors and effort. The system emphasizes simplicity and reliability, making it especially suitable for barangays with limited resources or technical training.

## ✅ Features

- **User Authentication**
  - Signup and login system
  - User credentials saved in a local `users.txt` file

### 🛡️ Audit Log

Tsyek includes a built-in **Audit Log** system to promote transparency and accountability. It automatically records all significant user actions and system events.

**🔍 Tracked Events:**

- **Login and Logout**
  - Logs the date and time when a user logs in or logs out

- **CRUD Operations**
  - Records whenever a user:
    - Adds a new inventory item
    - Updates existing item details
    - Deletes an item from the list

- **System and UI Interactions**
  - Opening the Audit Log window
  - Clicking the **Refresh** button (in both inventory and audit views)
  - Any important interface button click (e.g., Save, Clear, Delete)

**📁 Audit Log Storage:**
- All logs are saved locally in a CSV file
- Each log includes:
  - Timestamp
  - Username
  - Action performed
  - IP Address
  - Status
  - Details

This feature is especially useful for administrators who want to monitor user activity or audit system usage over time.


- **Inventory Operations (CRUD)**
  - Add new items with validation
  - View items in a JTable
  - Update and delete item records
  - Clear button to reset input fields

- **Inventory Fields**
  - Item ID
  - Item Name
  - Category
  - Quantity
  - Unit
  - Location
  - Condition (Unused, Used, Expired)
  - Date Acquired

- **Data Storage**
  - Inventory saved in `inventory_data.csv`
  - All data is locally stored — no external database required

---


