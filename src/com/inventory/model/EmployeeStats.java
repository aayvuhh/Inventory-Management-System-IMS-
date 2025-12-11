package com.inventory.model;

public class EmployeeStats {

    private Account employee;
    private double totalRevenue;
    private double totalProfit;

    public EmployeeStats(Account employee) {
        this.employee = employee;
    }

    public Account getEmployee() { return employee; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalProfit() { return totalProfit; }

    public void addSale(double revenue, double profit) {
        this.totalRevenue += revenue;
        this.totalProfit += profit;
    }

    public double getCommission() {
        return totalProfit * 0.10; // 10% commission on profit
    }

    public double getSalary() {
        return getCommission();
    }
}
