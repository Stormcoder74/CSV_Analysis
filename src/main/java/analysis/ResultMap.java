package analysis;

import make.RowObject;

import java.util.HashMap;
import java.util.TreeMap;

public class ResultMap extends TreeMap<Float, RowObject> {
    private static final int NUMBER_OF_RESULT_ROWS = 1000;
    private static final int NUMBER_OF_SOME_ROWS = 20;

    private HashMap<Integer, Integer> numbersOfSomeItems;

    public ResultMap() {
        super();
        numbersOfSomeItems = new HashMap<>();
    }

    @Override
    public synchronized RowObject put(Float key, RowObject rowObject) {
        RowObject result = null;
        if (numbersOfSomeItems.get(rowObject.getProductID()) == null
                || numbersOfSomeItems.get(rowObject.getProductID()) < NUMBER_OF_SOME_ROWS){
            if (size() < NUMBER_OF_RESULT_ROWS
                    || key < lastKey()){
                result = super.put(key, rowObject);
                int productIDCounter = numbersOfSomeItems.get(rowObject.getProductID());
                if (productIDCounter == 0)
                    numbersOfSomeItems.put(rowObject.getProductID(), 1);
                else
                    numbersOfSomeItems.replace(rowObject.getProductID(), ++productIDCounter);
                if (size() > NUMBER_OF_RESULT_ROWS){
                    remove(lastKey());
                }
            }
        }

        return result;
    }
}
