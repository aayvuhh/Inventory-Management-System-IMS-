package ims.core;

public class PerishableProduct extends Product {
    private String expiryDate;

    public PerishableProduct(String productID, String description, double price, int stockLevel, String expiryDate) {
        super(productID, description, price, stockLevel, expiryDate);
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean checkStock() {
        //still has stock and expiryDate is not null
        return super.checkStock() && expiryDate != null && !expiryDate.isEmpty();
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
