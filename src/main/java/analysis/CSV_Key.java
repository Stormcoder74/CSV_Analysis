package analysis;

public class CSV_Key implements Comparable<CSV_Key> {
    private float price;
    private int productID;

    public CSV_Key(float price, int productID) {
        this.price = price;
        this.productID = productID;
    }

    @Override
    public int compareTo(CSV_Key csvKey) {
        return (int) (price * 100 - csvKey.getPrice() * 100);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
