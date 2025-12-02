# Inventory Management System - Build Instructions

## Version 2.0 - Database-Enabled Edition

---

## Overview

This Inventory Management System (IMS) is a Java-based desktop application that provides comprehensive inventory tracking, supplier management, customer management, and reporting capabilities. Version 2.0 introduces SQLite database integration for persistent data storage.

---

## System Requirements

- **Java Development Kit (JDK)**: Version 11 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 512 MB RAM
- **Disk Space**: 50 MB for application and database

---

## Project Structure

```
Inventory-Management-System-IMS-/
├── lib/
│   ├── sqlite-jdbc-3.45.0.0.jar     # SQLite JDBC driver
│   ├── slf4j-api-2.0.9.jar          # SLF4J API (required by SQLite JDBC)
│   └── slf4j-simple-2.0.9.jar       # SLF4J Simple implementation
├── database/
│   ├── DatabaseManager.java          # Database connection manager
│   ├── ProductDAO.java                # Product database operations
│   ├── SupplierDAO.java               # Supplier database operations
│   ├── CustomerDAO.java               # Customer database operations
│   └── ReportDAO.java                 # Report database operations
├── model/
│   ├── Item.java                      # Base item class
│   ├── Product.java                   # Product entity
│   ├── Supplier.java                  # Supplier entity
│   ├── Customer.java                  # Customer entity
│   ├── PurchaseOrder.java             # Purchase order entity
│   ├── OrderItem.java                 # Order item entity
│   ├── Report.java                    # Report entity
│   ├── User.java                      # User base class
│   ├── InventoryManager.java          # Inventory manager user
│   ├── StoreStaff.java                # Store staff user
│   ├── OrderStatus.java               # Order status enum
│   └── ReportType.java                # Report type enum
├── service/
│   └── InventoryService.java          # Business logic layer
├── ui/
│   └── InventoryAppFrame.java         # Swing GUI interface
├── Main.java                          # Application entry point
├── inventory.db                       # SQLite database (created on first run)
├── BUILD.md                           # This file
└── README.md                          # Project documentation
```

---

## Building the Application

### Option 1: Using Command Line (Recommended)

#### Step 1: Navigate to Project Directory
```bash
cd Inventory-Management-System-IMS-
```

#### Step 2: Compile All Java Files
```bash
javac -cp ".:lib/sqlite-jdbc-3.45.0.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar" *.java database/*.java
```

**Windows Users**: Use semicolon instead of colon:
```cmd
javac -cp ".;lib/sqlite-jdbc-3.45.0.0.jar;lib/slf4j-api-2.0.9.jar;lib/slf4j-simple-2.0.9.jar" *.java database/*.java
```

#### Step 3: Run the Application
```bash
java -cp ".:lib/sqlite-jdbc-3.45.0.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar" com.inventory.Main
```

**Windows Users**:
```cmd
java -cp ".;lib/sqlite-jdbc-3.45.0.0.jar;lib/slf4j-api-2.0.9.jar;lib/slf4j-simple-2.0.9.jar" com.inventory.Main
```

---

### Option 2: Using IntelliJ IDEA

1. **Open Project**
   - Open IntelliJ IDEA
   - Select `File > Open`
   - Navigate to the project directory and click `OK`

2. **Add Required Libraries**
   - Right-click on the `lib` folder
   - Select `Add as Library` (this will add all JAR files in the folder)
   - Click `OK`
   - Alternatively, add each JAR file individually:
     - `lib/sqlite-jdbc-3.45.0.0.jar`
     - `lib/slf4j-api-2.0.9.jar`
     - `lib/slf4j-simple-2.0.9.jar`

3. **Configure Run Configuration**
   - Go to `Run > Edit Configurations`
   - Click `+` and select `Application`
   - Set Main class: `com.inventory.Main`
   - Click `Apply` and `OK`

4. **Run the Application**
   - Click the green play button or press `Shift + F10`

---

### Option 3: Using Eclipse

1. **Import Project**
   - Open Eclipse
   - Select `File > Import > Existing Projects into Workspace`
   - Browse to the project directory and click `Finish`

2. **Add Required Libraries**
   - Right-click on the project in Package Explorer
   - Select `Build Path > Configure Build Path`
   - Click `Add External JARs`
   - Navigate to the `lib` folder and select all JAR files:
     - `sqlite-jdbc-3.45.0.0.jar`
     - `slf4j-api-2.0.9.jar`
     - `slf4j-simple-2.0.9.jar`
   - Click `Open`
   - Click `Apply and Close`

3. **Run the Application**
   - Right-click on `Main.java`
   - Select `Run As > Java Application`

---

## Database Information

### Database Location
The SQLite database file `inventory.db` will be created automatically in the project root directory on first run.

### Database Schema
The application creates the following tables automatically:

- **products**: Stores product information (ID, name, category, price, stock, expiry date, reorder level)
- **suppliers**: Stores supplier information (ID, name, email, phone)
- **customers**: Stores customer information (ID, name, email, phone)
- **purchase_orders**: Stores purchase order records
- **reports**: Stores generated reports
- **sales_transactions**: Stores sales transaction history

### Demo Data
The application automatically loads demo data on first run, including:
- 3 sample suppliers
- 5 sample products
- 3 sample customers

---

## Features

### 1. Product Management
- Add new products with complete details
- Update stock levels (increase/decrease)
- Search products by ID, name, or category
- Track low-stock items automatically
- Support for expiry dates (perishable items)

### 2. Supplier Management
- Add and maintain supplier information
- Track supplier contact details
- Link suppliers to purchase orders

### 3. Customer Management
- Add and track customer information
- Record customer contact details
- Link customers to sales transactions

### 4. Reporting
- **Stock Summary Report**: Complete inventory overview
- **Low Stock Alert Report**: Products below reorder level
- Export reports (future enhancement)

### 5. Purchase Orders
- Create purchase orders linked to suppliers
- Track order status (Created, Sent, Received, Cancelled)
- Manage order items and quantities

### 6. Sales Transactions
- Issue products to customers (record sales)
- Process product returns
- Automatic stock level updates

---

## Troubleshooting

### Issue: "ClassNotFoundException: org.sqlite.JDBC" or "NoClassDefFoundError: org/slf4j/LoggerFactory"
**Solution**: Ensure all required JAR files are in the classpath when compiling and running:
- `lib/sqlite-jdbc-3.45.0.0.jar`
- `lib/slf4j-api-2.0.9.jar`
- `lib/slf4j-simple-2.0.9.jar`

### Issue: "Database connection failed"
**Solution**:
- Check that all library files exist in the `lib/` directory
- Ensure you have write permissions in the project directory
- Verify Java version is 11 or higher

### Issue: "No products displayed after adding"
**Solution**: Click the "Refresh" button to reload data from the database.

### Issue: Compilation errors about packages
**Solution**: Ensure you're compiling from the parent directory of the `com` folder structure.

---

## Development Notes

### Architecture
The application follows a layered architecture:
- **Presentation Layer**: Swing GUI (`InventoryAppFrame.java`)
- **Business Logic Layer**: Service classes (`InventoryService.java`)
- **Data Access Layer**: DAO classes (ProductDAO, SupplierDAO, etc.)
- **Database Layer**: SQLite database managed by `DatabaseManager.java`

### Design Patterns Used
- **Singleton Pattern**: DatabaseManager ensures single database connection
- **DAO Pattern**: Separates data access logic from business logic
- **MVC Pattern**: Model (entities), View (GUI), Controller (service layer)

---

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Advanced reporting with date range filters
- [ ] Export reports to PDF/Excel
- [ ] Barcode scanning integration
- [ ] Multi-location inventory tracking
- [ ] Email notifications for low stock
- [ ] REST API for mobile app integration

---

## Credits

**Course**: CMPSC 221 - Java Programming
**Supervisor**: Dr. Hussain
**Development Team**:
- Ava Iranmanesh
- Akshit Nayyar
- Syed Sarim Reza

**Version**: 2.0
**Last Updated**: December 2025

---

## License

This project is developed for educational purposes as part of CMPSC 221 coursework.
