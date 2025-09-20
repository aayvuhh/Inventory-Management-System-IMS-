package ims.core;

public class DigitalProduct extends Product {
    private String licenseKey;

    public DigitalProduct(String productID, String description, double price, String licenseKey) {
        super(productID, description, price, Integer.MAX_VALUE, null);
        this.licenseKey = licenseKey;
    }

    @Override
    public boolean checkStock() {
        // digital product is always available
        return true;
    }

    public String getLicenseKey() {
        return licenseKey;
    }
}
