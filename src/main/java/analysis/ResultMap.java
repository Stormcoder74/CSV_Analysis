package analysis;

import make.RowObject;

import java.util.TreeMap;

public class ResultMap extends TreeMap<RowObject, Integer> {
    private static final int NUMBER_OF_RESULT_ROWS = 1000;
    public static final int NUMBER_OF_SOME_ROWS = 20;

    private int totalRows;
    private int counter = 0;

    public ResultMap() {
        super();
        totalRows = 0;
    }

    @Override
    public synchronized Integer put(RowObject rowObject, Integer amount) {
        Integer result = null;
        counter++;

        if (get(rowObject) == null &&
                (totalRows < NUMBER_OF_RESULT_ROWS ||
                        rowObject.getPrice() < lastKey().getPrice())) {
            result = super.put(rowObject, 1);
            totalRows++;
        } else if ((rowObject.getPrice() < lastKey().getPrice() ||
                totalRows < NUMBER_OF_RESULT_ROWS) &&
                get(rowObject) < NUMBER_OF_SOME_ROWS) {
            result = replace(rowObject, get(rowObject) + 1);
            totalRows++;
        }

        if (totalRows > NUMBER_OF_RESULT_ROWS) {
            if (get(lastKey()) > 1) {
                replace(lastKey(), get(lastKey()) - 1);
            } else {
                remove(lastKey());
            }
            totalRows--;
        }

        return result;
    }
}
