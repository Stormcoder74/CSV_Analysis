package make;

public class RowObject {
//    public static final int NAMES_DIAPASON = 1_000_000_000;
    private static final int NAMES_DIAPASON = 1_000_000;
    private static final int PRICE_DIAPASON = 10_000;

    private static int nextID = 10;
    private int productID;
    private String name;
    private String condition;
    private String state;
    private float price;

    public RowObject() {
        productID = nextID++;
        name = ((Integer)((int)(Math.random() * NAMES_DIAPASON))).toString();
        condition = name.substring(0, name.length() / 2);
        state = name.substring(name.length() / 2);
        price = ((float)((int)(Math.random() * PRICE_DIAPASON * 100))) / 100;
    }

    public RowObject(String empty) {}

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return productID + "," +
                name + "," +
                condition + "," +
                state + "," +
                price;
    }
}
