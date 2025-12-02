# Inventory Management System - User Manual
## Version 3.0 Professional Edition

---

## Table of Contents
1. [Introduction](#introduction)
2. [What's New in Version 3.0](#whats-new-in-version-30)
3. [Getting Started](#getting-started)
4. [User Interface Overview](#user-interface-overview)
5. [Product Management](#product-management)
6. [Supplier Management](#supplier-management)
7. [Customer Management](#customer-management)
8. [Reports & Analytics](#reports--analytics)
9. [Technical Reference](#technical-reference)
10. [Troubleshooting](#troubleshooting)

---

## Introduction

The Inventory Management System (IMS) is a comprehensive desktop application designed to help businesses manage their inventory, suppliers, customers, and generate detailed reports. Built with Java Swing and SQLite database, it provides a professional, user-friendly interface for all your inventory needs.

### Key Features
- ✅ **Product Management**: Add, edit, search, and track inventory products
- ✅ **Supplier Management**: Maintain supplier contacts and information
- ✅ **Customer Management**: Track customer details and relationships
- ✅ **Stock Control**: Increase/decrease stock levels with validation
- ✅ **Search Functionality**: Quickly find products by ID, name, or category
- ✅ **Reports**: Generate stock summaries and low-stock alerts
- ✅ **Professional UI**: Modern, color-coded interface with status notifications
- ✅ **Database Persistence**: All data saved in SQLite database

---

## What's New in Version 3.0

### Major Improvements
1. **Complete UI Redesign**
   - Modern, professional color scheme
   - Enhanced visual hierarchy with headers and borders
   - Color-coded buttons (Green=Add, Red=Delete, Blue=Actions, Orange=Warnings)

2. **New Customer Management Tab**
   - Add and manage customer information
   - Track customer contacts
   - Full CRUD operations

3. **Enhanced Product Management**
   - Search functionality to find products quickly
   - Click table rows to auto-populate form (easy editing)
   - Clear form button to reset inputs
   - Delete product functionality
   - Better validation with helpful error messages

4. **Improved User Experience**
   - Status bar showing real-time feedback
   - Better button organization and labeling
   - Emoji icons for visual recognition
   - Confirmation dialogs for destructive actions
   - Non-editable tables (prevents accidental changes)

5. **Technical Enhancements**
   - Fixed validation bug (messages now show after validation)
   - Added getAllCustomers() method in InventoryService
   - Improved error handling throughout
   - Better code organization and documentation

---

## Getting Started

### Prerequisites
- Java JDK 11 or higher
- IntelliJ IDEA (or any Java IDE)
- 50 MB disk space

### Installation & Running

#### Option 1: IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA
2. Click **File → Open** and select the project folder
3. The required libraries should be automatically detected
4. Click the **Run** button (green play icon) or press **Shift + F10**

#### Option 2: Command Line
```bash
# Compile
javac -cp ".:lib/sqlite-jdbc-3.45.0.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar" -d out *.java database/*.java

# Run
java -cp "out:lib/sqlite-jdbc-3.45.0.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar" com.inventory.Main
```

**Windows Users**: Replace `:` with `;` in classpath

### First Launch
When you first run the application:
1. The database file (`inventory.db`) is automatically created
2. Demo data is loaded including:
   - 3 sample suppliers
   - 5 sample products
   - 3 sample customers
3. The main window opens showing the Products tab

---

## User Interface Overview

### Main Window Layout

```
┌─────────────────────────────────────────────────────┐
│  File   Help                                  [Menu]│
├─────────────────────────────────────────────────────┤
│  [📦 Products] [🏢 Suppliers] [👥 Customers] [📊 Reports]  │
├─────────────────────────────────────────────────────┤
│                                                     │
│              [TAB CONTENT AREA]                     │
│                                                     │
│                                                     │
├─────────────────────────────────────────────────────┤
│  ℹ️ Status: Ready                                   │
└─────────────────────────────────────────────────────┘
```

### Color Coding
- **🟢 Green Buttons**: Add/Save operations
- **🔴 Red Buttons**: Delete operations
- **🔵 Blue Buttons**: Primary actions (stock changes, search)
- **🟠 Orange Buttons**: Warning actions (decrease stock, alerts)
- **⚪ White Buttons**: Neutral actions (clear, refresh, show all)
- **Gray Headers**: Section headers with titles

### Status Bar
The bottom status bar shows:
- Current operation status
- Success messages (green text)
- Warnings (orange text)
- Errors (red text)
- Information (blue text)

---

## Product Management

### Products Tab Overview

The Products tab is divided into three main sections:
1. **Product Details Form** (top)
2. **Product Inventory Table** (middle)
3. **Action Buttons** (bottom)

### Adding a New Product

**Step-by-Step:**

1. Navigate to the **📦 Products** tab
2. Fill in the product details:
   - **Product ID**: Unique identifier (e.g., "P001", "LAPTOP-01")
   - **Product Name**: Descriptive name (e.g., "Wireless Mouse")
   - **Category**: Product category (e.g., "Electronics", "Office Supplies")
   - **Unit Price ($)**: Price per unit (e.g., 24.99)
   - **Stock Level**: Current quantity (e.g., 100)
   - **Reorder Level**: Minimum stock before reorder alert (e.g., 20)

3. Click **💾 Add/Update Product** button
4. Success message appears in status bar
5. Product appears in the table below

**Required Fields:**
- Product ID ✓
- Product Name ✓
- All others are optional (defaults to 0 or "General")

**Example:**
```
Product ID:      P101
Product Name:    USB-C Cable 2m
Category:        Accessories
Unit Price:      12.99
Stock Level:     150
Reorder Level:   30
```

### Updating an Existing Product

**Method 1: Click to Edit**
1. Click on any row in the product table
2. Form automatically populates with product data
3. Modify any fields
4. Click **💾 Add/Update Product**

**Method 2: Re-enter ID**
1. Type the existing Product ID in the form
2. Enter new values for other fields
3. Click **💾 Add/Update Product**

### Searching for Products

**Search Feature:**
1. Use the search box at the top of the table
2. Enter search term (searches ID, Name, and Category)
3. Click **Search** button
4. Matching products display in table
5. Click **Show All** to return to full list

**Search Examples:**
- Search "USB" → finds all USB products
- Search "Electronics" → finds all electronics
- Search "P001" → finds product with ID P001

### Managing Stock Levels

#### Increasing Stock (Receiving Inventory)
1. Select a product from the table
2. Click **⬆️ Increase Stock** button
3. Enter quantity to add (e.g., 50)
4. Click OK
5. Stock level updates automatically

**Use Case:** Receiving shipment from supplier

#### Decreasing Stock (Selling/Issuing Products)
1. Select a product from the table
2. Click **⬇️ Decrease Stock** button
3. Enter quantity to remove (e.g., 10)
4. Click OK
5. Stock level decreases automatically

**Use Case:** Recording sales or product issues

**Validation:**
- Cannot decrease below zero (error message shown)
- Must enter positive numbers
- Automatic calculation prevents errors

### Deleting Products

**Warning:** This action cannot be undone!

1. Select product from table
2. Click **🗑️ Delete Product** button
3. Confirmation dialog appears
4. Click **Yes** to confirm or **No** to cancel
5. Product removed from inventory

### Other Actions

**🔄 Clear Form Button:**
- Resets all form fields to empty
- Clears table selection
- Use before adding new product

**🔃 Refresh Button:**
- Reloads product data from database
- Updates table with latest information
- Use after other operations or database changes

---

## Supplier Management

### Suppliers Tab Overview

The Suppliers tab manages your supplier contacts and information.

**Layout:**
1. **Supplier Details Form** (top)
2. **Supplier List Table** (middle)
3. **Action Buttons** (bottom)

### Adding a New Supplier

**Step-by-Step:**

1. Navigate to **🏢 Suppliers** tab
2. Fill in supplier details:
   - **Supplier Name**: Company name (e.g., "Tech Supplies Inc")
   - **Email Address**: Contact email (e.g., "sales@techsupplies.com")
   - **Phone Number**: Contact phone (e.g., "555-0100")

3. Click **💾 Add Supplier** button
4. Supplier appears in table with auto-generated ID

**Required Fields:**
- Supplier Name ✓
- Email and Phone are optional

**Example:**
```
Supplier Name:   Global Electronics Wholesale
Email Address:   orders@globalelec.com
Phone Number:    +1-555-234-5678
```

### Viewing Suppliers

The supplier table displays:
- **ID**: Auto-generated supplier ID
- **Name**: Supplier company name
- **Email**: Contact email address
- **Phone**: Contact phone number

### Buttons

**💾 Add Supplier:**
- Saves new supplier to database
- Validates required fields
- Shows success message

**🔄 Clear Form:**
- Resets all form fields
- Clears selection

**🔃 Refresh:**
- Reloads supplier list from database
- Updates table display

---

## Customer Management

### Customers Tab Overview

The Customers tab helps track your customer information.

**Layout:**
1. **Customer Details Form** (top)
2. **Customer List Table** (middle)
3. **Action Buttons** (bottom)

### Adding a New Customer

**Step-by-Step:**

1. Navigate to **👥 Customers** tab
2. Fill in customer details:
   - **Customer Name**: Full name or company (e.g., "John Smith")
   - **Email Address**: Customer email (e.g., "john@email.com")
   - **Phone Number**: Contact number (e.g., "555-1234")

3. Click **💾 Add Customer** button
4. Customer appears in table with auto-generated ID

**Required Fields:**
- Customer Name ✓
- Email and Phone are optional

**Example:**
```
Customer Name:   Acme Corporation
Email Address:   purchasing@acmecorp.com
Phone Number:    +1-555-999-8888
```

### Viewing Customers

The customer table displays:
- **ID**: Auto-generated customer ID
- **Name**: Customer name
- **Email**: Email address
- **Phone**: Phone number

### Buttons

**💾 Add Customer:**
- Saves new customer to database
- Validates required fields
- Shows success message

**🔄 Clear Form:**
- Resets all form fields
- Clears selection

**🔃 Refresh:**
- Reloads customer list from database
- Updates table display

---

## Reports & Analytics

### Reports Tab Overview

The Reports tab generates detailed inventory reports for analysis and decision-making.

**Layout:**
1. **Report Generation Buttons** (top)
2. **Report Display Area** (large text area)

### Stock Summary Report

**Purpose:** Complete overview of all inventory

**To Generate:**
1. Navigate to **📊 Reports** tab
2. Click **📋 Stock Summary Report** button
3. Report displays in text area

**Report Contents:**
```
╔════════════════════════════════════════════════════════════╗
║            STOCK SUMMARY REPORT                            ║
╚════════════════════════════════════════════════════════════╝

Product ID      Name                           Category        Stock     Price
─────────────────────────────────────────────────────────────────────────────
P001            External Hard Drive 1TB        Electronics       50    $89.99
P002            Stainless Steel Spoons         Kitchenware      200    $14.99
P003            USB Flash Drive 32GB           Electronics       20    $12.50
...

Total Products: 5
```

**Information Shown:**
- All products in inventory
- Complete product details
- Current stock levels
- Unit prices
- Total product count

**Use Cases:**
- Monthly inventory review
- Stock valuation
- Product catalog
- Audit compliance

### Low Stock Alert Report

**Purpose:** Identify products needing reorder

**To Generate:**
1. Navigate to **📊 Reports** tab
2. Click **⚠️ Low Stock Alert** button
3. Report shows products at/below reorder level

**Report Contents:**
```
╔════════════════════════════════════════════════════════════╗
║              LOW STOCK ALERT REPORT                        ║
╚════════════════════════════════════════════════════════════╝

Product ID      Name                           Current Stock   Reorder Level
──────────────────────────────────────────────────────────────────────────
P003            USB Flash Drive 32GB                     20              15 ⚠

Products Requiring Reorder: 1
```

**Information Shown:**
- Products below reorder threshold
- Current stock levels
- Reorder levels
- Count of products needing attention

**Indicators:**
- ⚠️ Warning symbol for low stock items
- ✓ Green message if all adequately stocked

**Use Cases:**
- Daily stock monitoring
- Purchase order planning
- Preventing stockouts
- Inventory optimization

### Report Actions

**📋 Stock Summary Report Button:**
- Generates complete inventory report
- Updates report display area
- Status bar shows success

**⚠️ Low Stock Alert Button:**
- Generates low stock report
- Highlights products needing reorder
- Status bar shows warning count

**🔄 Clear Report Button:**
- Clears report display area
- Useful before generating new report

---

## Technical Reference

### Database Structure

**Database File:** `inventory.db` (SQLite)
**Location:** Project root directory

**Tables:**

#### products
| Column | Type | Description |
|--------|------|-------------|
| product_id | TEXT PRIMARY KEY | Unique product identifier |
| product_name | TEXT NOT NULL | Product name |
| category | TEXT | Product category |
| unit_price | REAL | Price per unit |
| stock_level | INTEGER | Current quantity |
| expiry_date | TEXT | Expiration date (ISO format) |
| reorder_level | INTEGER | Minimum stock threshold |

#### suppliers
| Column | Type | Description |
|--------|------|-------------|
| supplier_id | INTEGER PRIMARY KEY | Auto-increment ID |
| supplier_name | TEXT NOT NULL | Supplier name |
| contact_email | TEXT | Email address |
| phone_number | TEXT | Phone number |

#### customers
| Column | Type | Description |
|--------|------|-------------|
| customer_id | INTEGER PRIMARY KEY | Auto-increment ID |
| customer_name | TEXT NOT NULL | Customer name |
| email_address | TEXT | Email address |
| phone_number | TEXT | Phone number |

#### reports
| Column | Type | Description |
|--------|------|-------------|
| report_id | INTEGER PRIMARY KEY | Auto-increment ID |
| manager_id | INTEGER | Manager who generated report |
| report_type | TEXT | Type (STOCK_SUMMARY, LOW_STOCK) |
| content | TEXT | Report content |
| generated_at | TIMESTAMP | Generation timestamp |

### Button Reference Guide

#### Products Tab Buttons

| Button | Color | Icon | Function | Validation |
|--------|-------|------|----------|------------|
| Add/Update Product | 🟢 Green | 💾 | Creates new or updates existing product | ID and Name required |
| Clear Form | ⚪ White | 🔄 | Resets all form fields | None |
| Delete Product | 🔴 Red | 🗑️ | Removes selected product | Requires selection + confirmation |
| Increase Stock | 🔵 Blue | ⬆️ | Adds quantity to stock | Requires selection + positive number |
| Decrease Stock | 🟠 Orange | ⬇️ | Removes quantity from stock | Requires selection + sufficient stock |
| Refresh | ⚪ White | 🔃 | Reloads product list | None |
| Search | 🔵 Blue | - | Filters products by keyword | None |
| Show All | ⚪ White | - | Removes search filter | None |

#### Suppliers Tab Buttons

| Button | Color | Icon | Function | Validation |
|--------|-------|------|----------|------------|
| Add Supplier | 🟢 Green | 💾 | Creates new supplier | Name required |
| Clear Form | ⚪ White | 🔄 | Resets form fields | None |
| Refresh | ⚪ White | 🔃 | Reloads supplier list | None |

#### Customers Tab Buttons

| Button | Color | Icon | Function | Validation |
|--------|-------|------|----------|------------|
| Add Customer | 🟢 Green | 💾 | Creates new customer | Name required |
| Clear Form | ⚪ White | 🔄 | Resets form fields | None |
| Refresh | ⚪ White | 🔃 | Reloads customer list | None |

#### Reports Tab Buttons

| Button | Color | Icon | Function | Validation |
|--------|-------|------|----------|------------|
| Stock Summary Report | 🔵 Blue | 📋 | Generates complete inventory report | None |
| Low Stock Alert | 🟠 Orange | ⚠️ | Generates reorder alert report | None |
| Clear Report | ⚪ White | 🔄 | Clears report display | None |

### Menu Bar Options

**File Menu:**
- **Exit**: Closes application and database connection

**Help Menu:**
- **About**: Shows version and developer information

### Architecture Overview

```
┌─────────────────────────────────────────┐
│     Presentation Layer (UI)             │
│     InventoryAppFrame.java              │
├─────────────────────────────────────────┤
│     Business Logic Layer                │
│     InventoryService.java               │
├─────────────────────────────────────────┤
│     Data Access Layer (DAO)             │
│     ProductDAO, SupplierDAO,            │
│     CustomerDAO, ReportDAO              │
├─────────────────────────────────────────┤
│     Database Layer                      │
│     DatabaseManager.java                │
│     SQLite (inventory.db)               │
└─────────────────────────────────────────┘
```

**Design Patterns Used:**
- **MVC (Model-View-Controller)**: Separates UI, logic, and data
- **DAO (Data Access Object)**: Abstracts database operations
- **Singleton**: DatabaseManager ensures single connection

---

## Troubleshooting

### Common Issues & Solutions

#### Issue 1: Application Won't Start
**Symptoms:**
- Error: "SQLite JDBC driver not found"
- Error: "NoClassDefFoundError: org/slf4j/LoggerFactory"

**Solution:**
1. Verify all required JAR files exist in `lib/` directory:
   - sqlite-jdbc-3.45.0.0.jar
   - slf4j-api-2.0.9.jar
   - slf4j-simple-2.0.9.jar
2. In IntelliJ: **File → Project Structure → Libraries** → Add all JARs
3. Or follow BUILD.md instructions

#### Issue 2: "Product ID and Name are required!" when adding product
**Cause:** Validation prevents empty required fields

**Solution:**
1. Ensure Product ID field is not empty
2. Ensure Product Name field is not empty
3. Other fields can be empty (will use defaults)

#### Issue 3: Cannot decrease stock
**Error:** "Insufficient stock for product"

**Cause:** Trying to decrease more than available

**Solution:**
1. Check current stock level in table
2. Enter quantity less than or equal to current stock
3. Use "Increase Stock" if you need to add inventory

#### Issue 4: Search returns no results
**Cause:** No products match search term

**Solution:**
1. Check spelling
2. Try partial search (e.g., "USB" instead of "USB Cable")
3. Click "Show All" to see full inventory
4. Search is case-insensitive

#### Issue 5: Table not updating after operation
**Symptoms:** Changes don't appear immediately

**Solution:**
1. Click the **🔃 Refresh** button
2. Or switch to another tab and back
3. Data is saved in database correctly

#### Issue 6: Database locked error
**Symptoms:** "Database is locked" message

**Cause:** Multiple instances accessing database

**Solution:**
1. Close all instances of the application
2. Delete `inventory.db-journal` file if it exists
3. Restart application
4. Run only one instance at a time

#### Issue 7: Demo data loaded every time
**Symptoms:** Duplicate products after multiple runs

**Solution:**
- Demo data only loads if database is empty
- If you see duplicates, manually delete duplicates or delete `inventory.db` file and restart

### Getting Help

**Error Messages:**
- Read the error dialog carefully
- Check status bar for hints
- Look for color-coded messages

**Best Practices:**
- Use **Clear Form** before adding new records
- Always **Refresh** after manual database changes
- Keep Product IDs unique
- Click **Exit** from File menu (ensures proper database closure)

### Data Backup

**Important Files:**
- `inventory.db` - Main database file
- `inventory.db-journal` - Temporary transaction log

**Backup Procedure:**
1. Close the application
2. Copy `inventory.db` to backup location
3. Store backups regularly

**Restore Procedure:**
1. Close the application
2. Replace `inventory.db` with backup file
3. Restart application

---

## Quick Reference Card

### Keyboard Shortcuts
- **F5**: Refresh (when in tables)
- **Escape**: Clear selection
- **Tab**: Move between form fields
- **Enter**: Submit form (same as clicking Add button)

### Common Workflows

**Adding Product:**
1. Products tab → Fill form → Add/Update → Done

**Updating Product:**
1. Products tab → Click table row → Edit form → Add/Update → Done

**Recording Sale:**
1. Products tab → Select product → Decrease Stock → Enter quantity → Done

**Receiving Shipment:**
1. Products tab → Select product → Increase Stock → Enter quantity → Done

**Checking Low Stock:**
1. Reports tab → Low Stock Alert → Review list → Done

**Finding Product:**
1. Products tab → Enter search term → Search → Review results → Done

### Status Messages

| Color | Meaning | Example |
|-------|---------|---------|
| 🔵 Blue | Information | "Loaded 25 products" |
| 🟢 Green | Success | "Product saved successfully!" |
| 🟠 Orange | Warning | "3 products need reordering!" |
| 🔴 Red | Error | "Product not found" |

---

## Version History

### Version 3.0 (Current)
- Complete UI redesign with professional styling
- Added Customer Management tab
- Added search functionality for products
- Added delete functionality
- Enhanced validation and error handling
- Added status bar with color-coded messages
- Fixed validation bug
- Improved table interactions (click to edit)
- Added clear form buttons throughout
- Enhanced button organization with icons

### Version 2.0
- SQLite database integration
- Added DAO pattern for data access
- Persistent data storage
- Enhanced reporting capabilities
- Improved code structure

### Version 1.0
- Initial release
- In-memory storage
- Basic product and supplier management

---

## Credits

**Development Team:**
- Ava Iranmanesh
- Akshit Nayyar
- Syed Sarim Reza

**Course:** CMPSC 221 - Java Programming
**Supervisor:** Dr. Hussain
**Institution:** [Your Institution Name]
**Year:** 2025

---

## License

This project is developed for educational purposes as part of CMPSC 221 coursework.

---

**End of User Manual**

For technical issues or questions, please refer to the README.md and BUILD.md files in the project directory.
