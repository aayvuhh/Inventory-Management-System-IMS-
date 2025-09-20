package ims.core;

public class Product {
    protected String productID;
    protected String description;
    protected double price;
    protected int stockLevel;
    protected String expiryDate; // optional

    // Constructors
    public Product() {}

    public Product(String productID, String description, double price, int stockLevel, String expiryDate) {
        setProduct(productID, description, price, stockLevel, expiryDate);
    }

    // setProduct method
    public void setProduct(String productID, String description, double price, int stockLevel, String expiryDate) {
        this.productID = productID;
        this.description = description;
        this.price = price;
        this.stockLevel = stockLevel;
        this.expiryDate = expiryDate;
    }

    // getProduct method
    public String getProduct() {
        return String.format("ID: %s, Desc: %s, Price: %.2f, Stock: %d, Expiry: %s",
                productID, description, price, stockLevel, expiryDate);
    }

    // calculatePrice method
    public double calculatePrice() {
        return price; // base price
    }

    // checkStock method
    public boolean checkStock() {
        return stockLevel > 0;
    }
}
