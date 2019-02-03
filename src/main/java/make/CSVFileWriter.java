package make;

import launch.Launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class CSVFileWriter implements Runnable {
    //    private static final int NUMBER_OF_ROWS = 10_000_000;
    private static final int NUMBER_OF_ROWS = 10_000;

    private RowRegistry rows;
    private String dir;
    private String fileName;
    private Semaphore semaphore;

    public CSVFileWriter(RowRegistry rows, String dir, String fileName, Semaphore semaphore) {
        this.rows = rows;
        this.dir = dir;
        this.fileName = fileName;
        this.semaphore = semaphore;
    }

    public void run() {
        try {
            semaphore.acquire();
            File file = new File(dir + File.separator + fileName + ".csv");
            int index = 0;
            try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i <= NUMBER_OF_ROWS - 1; i++) {
                    index = (int) (Math.random() * (rows.length() - 1));
                    bufWriter.write(rows.get(index));
                    bufWriter.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }
    }
}
