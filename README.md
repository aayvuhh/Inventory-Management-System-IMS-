# Inventory Management System (IMS) — Course Project

**Version 2.0 - Database-Enabled Edition**

This repository contains the coursework for **CMPSC 221** (Java Programming).
**Team**: Ava Iranmanesh, Akshit Nayyar, Syed Sarim Reza
**Supervisor**: Dr. Hussain

---

## Overview

The Inventory Management System (IMS) is a comprehensive desktop application designed to handle and monitor all activities related to stock and inventory within a business. Built with Java and Swing GUI, this system provides a professional, user-friendly interface for inventory managers and staff to maintain real-time tracking of products, suppliers, and customers.

**Version 2.0** introduces **SQLite database integration** for persistent data storage, replacing the previous in-memory approach. All inventory data is now automatically saved and persists across application sessions.

---

## Key Features

### 📦 Product Management
- Add, update, and track products with complete details (ID, name, category, price, stock, expiry date)
- Real-time stock level monitoring with automatic low-stock alerts
- Support for both perishable and non-perishable items
- Advanced search functionality by product ID, name, or category
- Configurable reorder levels for automatic inventory alerts

### 🏢 Supplier Management
- Maintain comprehensive supplier database
- Track supplier contact information (name, email, phone)
- Link suppliers to purchase orders for complete supply chain visibility

### 👥 Customer Management
- Store and manage customer information
- Track customer contact details
- Record sales and return transactions

### 📊 Reporting System
- **Stock Summary Report**: Complete overview of all inventory items
- **Low Stock Alert Report**: Identifies products requiring reorder
- Historical report tracking with timestamps
- Professional report formatting with visual indicators

### 🛒 Purchase Order Management
- Create and manage purchase orders
- Track order status (Created → Sent to Supplier → Received → Cancelled)
- Link orders to specific suppliers
- Calculate total order amounts automatically

### 💰 Sales Transactions
- Process customer sales with automatic stock updates
- Handle product returns efficiently
- Real-time inventory adjustment

---

## Technical Architecture

### Technology Stack
- **Language**: Java (JDK 11+)
- **GUI Framework**: Java Swing
- **Database**: SQLite 3.45.0
- **Architecture**: Layered architecture with DAO pattern

### Design Patterns
- **Singleton Pattern**: Database connection management
- **DAO Pattern**: Clean separation of data access logic
- **MVC Pattern**: Model-View-Controller architecture

### Database Schema
The system automatically creates and manages the following tables:
- `products` - Product inventory with stock tracking
- `suppliers` - Supplier information and contacts
- `customers` - Customer database
- `purchase_orders` - Order management and tracking
- `reports` - Historical report storage
- `sales_transactions` - Sales history and analytics

---

## Quick Start

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Minimum 512 MB RAM
- 50 MB disk space

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Inventory-Management-System-IMS-
   ```

2. **Compile the application**
   ```bash
   javac -cp ".:lib/sqlite-jdbc-3.45.0.0.jar" *.java database/*.java
   ```

3. **Run the application**
   ```bash
   java -cp ".:lib/sqlite-jdbc-3.45.0.0.jar" com.inventory.Main
   ```

**For detailed build instructions**, see [BUILD.md](BUILD.md)

---

## What's New in Version 2.0

✅ **SQLite Database Integration** - Persistent data storage
✅ **Professional Code Structure** - Well-organized packages and classes
✅ **Comprehensive JavaDoc Comments** - Fully documented codebase
✅ **DAO Pattern Implementation** - Clean data access layer
✅ **Professional Demo Data** - Realistic sample data for testing
✅ **Enhanced Error Handling** - Robust exception management
✅ **Improved Variable Naming** - Clear, descriptive identifiers throughout
✅ **Code Quality Improvements** - Removed typos and inconsistencies

---

## Project Structure

```
IMS/
├── database/          # Data Access Objects (DAO)
├── model/             # Entity classes (Product, Supplier, Customer, etc.)
├── service/           # Business logic layer
├── ui/                # Swing GUI components
├── lib/               # External libraries (SQLite JDBC driver)
└── Main.java          # Application entry point
```

---

## System Purpose

The Inventory Management System aims to:

- **Automate** stock tracking and inventory operations
- **Reduce wastage** through accurate stock monitoring
- **Prevent shortages** with low-stock alerts and reorder levels
- **Optimize costs** by maintaining appropriate inventory levels
- **Save time** through instant search, automated updates, and quick reporting
- **Provide accuracy** with database-backed persistent storage
- **Enable insights** through comprehensive reporting capabilities

This system transforms manual inventory management into an efficient, automated process, helping businesses maintain optimal stock levels while reducing operational overhead.

---

## Documentation

- [BUILD.md](BUILD.md) - Detailed build and installation instructions
- Code is fully documented with JavaDoc comments
- Each class includes comprehensive inline documentation

---

## License

This project is developed for educational purposes as part of CMPSC 221 coursework.

---

## Credits

**Development Team**:
- Ava Iranmanesh
- Akshit Nayyar
- Syed Sarim Reza

**Course**: CMPSC 221 - Java Programming
**Supervisor**: Dr. Hussain
**Version**: 2.0 (Database-Enabled)
**Last Updated**: December 2025 
