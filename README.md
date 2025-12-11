# Inventory Management System (IMS)

A complete Java-based Inventory Management System built using Swing, JDBC (Apache Derby), and object-oriented design. This system supports manager and employee roles, authentication, stock control, sales tracking, reporting, and an approval workflow for stock requests.

## Features

### Authentication System

* Login with @psu.edu email only
* Registration requires first/last name, email, 10-digit phone number, and password (min 8 characters)
* Password reset requests:

  * Employee submits request
  * Manager approves or rejects
  * System generates a new password automatically

### User Roles

#### Manager

* Full control of all inventory
* Approves or rejects stock requests
* Creates and views purchase orders
* Views total revenue and total profit
* Views employee performance, salary, and commission
* Sees Employee of the Month (based on highest profit generated)
* Views password reset requests

#### Employee

* Updates inventory (add/edit items, adjust stock)
* Records sales
* Submits stock requests
* Views status of own stock requests

### Inventory Management

* Add/update products
* Track price, stock, category, reorder level
* Increase or decrease stock
* Stock updates automatically on approved requests
* Low stock detection

### Stock Request Workflow

Employees:

* Request stock by entering:

  * Product ID
  * Quantity
  * Cost per unit
  * Sale price per unit
* System calculates expected revenue and profit

Managers:

* Approve or reject requests
* Approval automatically increases stock

### Sales & Employee Performance

* Employees record sales
* System calculates revenue and profit
* Commission = 10% of profit
* Salary = total commission
* Employee of the Month = highest commission

### Purchase Orders (Manager Only)

* View suppliers
* View purchase orders
* View items inside each PO

### Reporting

* Stock Summary Report
* Low Stock Report

## Default Login Accounts

| Email                                   | Password | Role     |
| --------------------------------------- | -------- | -------- |
| [sarim@psu.edu](mailto:sarim@psu.edu)   | admin123 | Manager  |
| [eva@psu.edu](mailto:eva@psu.edu)       | admin123 | Manager  |
| [akshit@psu.edu](mailto:akshit@psu.edu) | admin123 | Employee |

## Tech Stack

* Java 17+
* Swing
* Apache Derby (embedded JDBC)
* IntelliJ IDEA

## Project Structure

```
src/com/inventory/
    Main.java
    db/DatabaseHelper.java
    model/...
    service/InventoryService.java
    service/AuthService.java
    ui/LoginFrame.java
    ui/RegistrationDialog.java
    ui/InventoryAppFrame.java
```

## How to Run

1. Open the project in IntelliJ IDEA
2. Ensure `derby.jar` is in the classpath
3. Delete existing `InventoryDB/` folder if needed (Derby will recreate it)
4. Run `Main.java`

## Recommended .gitignore

```
InventoryDB/
*.log
*.lck
.idea/
out/
*.iml
```

## Description

This project was built for CMPSC221 and demonstrates full-stack Java GUI development, OOP design, authentication systems, database interaction, and business logic modeling.

