package analysis;

import make.RowObject;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ResultMap extends TreeMap<Float, TreeMap<RowObject, Integer>> {
    private static final int NUMBER_OF_RESULT_ROWS = 1000;
    private static final int NUMBER_OF_SOME_ROWS = 20;

    private int totalRows;

    public ResultMap() {
        super();
        totalRows = 0;
    }

    public synchronized void insert(RowObject rowObject) {
        Map<RowObject, Integer> resultMapItem = get(rowObject.getPrice());

        if (resultMapItem == null &&
                (totalRows < NUMBER_OF_RESULT_ROWS ||
                        rowObject.getPrice() < lastKey())) {
            TreeMap<RowObject, Integer> productIdMap = new TreeMap<>(Comparator.comparingInt(RowObject::getProductID));
            productIdMap.put(rowObject, 1);
            super.put(rowObject.getPrice(), productIdMap);
            totalRows++;
        } else if ((rowObject.getPrice() < lastKey() ||
                totalRows < NUMBER_OF_RESULT_ROWS)) {
            Integer productIdCounter = resultMapItem.get(rowObject);
            if (productIdCounter == null){
                resultMapItem.put(rowObject, 1);
                totalRows++;
            }else if (productIdCounter < NUMBER_OF_SOME_ROWS) {
                get(rowObject.getPrice()).replace(rowObject, productIdCounter + 1);
                totalRows++;
            }
        }

        if (totalRows > NUMBER_OF_RESULT_ROWS) {
            TreeMap<RowObject, Integer> productIdMap = get(lastKey());
            int count = productIdMap.get(productIdMap.lastKey());
            if (count > 1) {
                productIdMap.replace(productIdMap.lastKey(), count - 1);
            } else {
                if (productIdMap.size() > 1) {
                    productIdMap.remove(productIdMap.lastKey());
                } else {
                    remove(lastKey());
                }
            }
            totalRows--;
        }
    }
}
