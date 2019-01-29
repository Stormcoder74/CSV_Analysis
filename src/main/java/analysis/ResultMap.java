package analysis;

import make.RowObject;

import java.util.HashMap;
import java.util.TreeMap;

public class ResultMap extends TreeMap<CSV_Key, RowObject> {
    private static final int NUMBER_OF_RESULT_ROWS = 1000;
    public static final int NUMBER_OF_SOME_ROWS = 20;

    private HashMap<Integer, Integer> numbersOfSomeItems;
    private int totalRows;

    public ResultMap() {
        super();
        numbersOfSomeItems = new HashMap<>();
        totalRows = 0;
    }

    @Override
    public synchronized RowObject put(CSV_Key key, RowObject rowObject) {
        RowObject result = null;
        if (numbersOfSomeItems.get(rowObject.getProductID()) == null
                || numbersOfSomeItems.get(rowObject.getProductID()) < NUMBER_OF_SOME_ROWS) {
            if (totalRows < NUMBER_OF_RESULT_ROWS
                    || key.getPrice() < lastKey().getPrice()) {
                result = super.put(key, rowObject);
                int productIDCounter = numbersOfSomeItems.get(rowObject.getProductID());

                if (productIDCounter == 0) numbersOfSomeItems.put(rowObject.getProductID(), 1);
                else numbersOfSomeItems.replace(rowObject.getProductID(), ++productIDCounter);
                totalRows++;

                if (totalRows > NUMBER_OF_RESULT_ROWS) {
                    if (numbersOfSomeItems.get(lastKey().getProductID()) == 1) remove(lastKey());
                    else numbersOfSomeItems.replace(rowObject.getProductID(), --productIDCounter);
                    totalRows--;
                }
            }
        }

        return result;
    }
}
