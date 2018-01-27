package swiftproduct.swift;

/**
 * Created by Charles on 11/17/15.
 */
public class Product {

    private int ID = 0;
    private String cmpName = null;
    private String productName = null;
    private double Price = 0.00;
    private double Unit = 0;
    private String shortDesc = null;

    public Product(int ID, String cmpName, String productName, double Price, double Unit, String shortDesc) {
        this.ID = ID;
        this.cmpName = cmpName;
        this.productName = productName;
        this.Price = Price;
        this.Unit = Unit;
        this.shortDesc = shortDesc;
    }


    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public double getUnit() {
        return Unit;
    }

    public void setUnit(double unit) {
        Unit = Unit;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCmpName() {return cmpName;}

    public void setCmpName(String cmpName) {this.cmpName = cmpName;}

    public int getID() {return ID;}

    public void setID(int ID) {this.ID = ID;}
}
