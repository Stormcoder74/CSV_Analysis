package analysis;

import make.RowObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ResultMap extends TreeMap<Float, List<RowObject>> {
    private static final int NUMBER_OF_RESULT_ROWS = 1000;
    private static final int NUMBER_OF_SOME_ROWS = 20;

    private int totalRows;
    private int counter = 0;

    public ResultMap() {
        super();
        totalRows = 0;
    }

    public synchronized void insert(RowObject rowObject) {
        counter++;

        if (get(rowObject.getPrice()) == null &&
                (totalRows < NUMBER_OF_RESULT_ROWS ||
                        rowObject.getPrice() < lastKey())) {
            List<RowObject> list = new ArrayList<>();
            list.add(rowObject);
            super.put(rowObject.getPrice(), list);
            totalRows++;
        } else if ((rowObject.getPrice() < lastKey() ||
                totalRows < NUMBER_OF_RESULT_ROWS) &&
                get(rowObject.getPrice()).size() < NUMBER_OF_SOME_ROWS) {
            get(rowObject.getPrice()).add(rowObject);
            totalRows++;
        }

        if (totalRows > NUMBER_OF_RESULT_ROWS) {
            List<RowObject> list = get(lastKey());
            if (list.size() > 1) {
                list.remove(list.size() - 1);
            } else {
                remove(lastKey());
            }
            totalRows--;
        }
    }
}
