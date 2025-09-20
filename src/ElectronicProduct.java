package ims.core;

public class ElectronicProduct extends Product {
    private int warrantyPeriod; // in months

    public ElectronicProduct(String productID, String description, double price, int stockLevel, int warrantyPeriod) {
        super(productID, description, price, stockLevel, null);
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override
    public double calculatePrice() {
        // add 5% warranty fee to base price
        return price * 1.05;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }
}
