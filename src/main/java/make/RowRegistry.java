package make;

public class RowRegistry {
//    private static final int SIZE = 100_000_000;
    private static final int SIZE = 1_000_000;
    private String[] rows;

    public RowRegistry() {
        rows = new String[SIZE];
        for (int i = 0; i < SIZE - 1; i++){
            rows[i] = new RowObject().toString();
        }
    }

    String get(int index){
        return rows[index];
    }

    int length(){
        return rows.length;
    }
}
