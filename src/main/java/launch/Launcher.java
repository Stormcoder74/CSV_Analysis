package launch;

import analysis.CSVReader;
import analysis.ResultMap;
import make.CSVFileWriter;
import make.RowObject;
import make.RowRegistry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

public class Launcher {
    //    public static int NUMBER_OF_FILES = 100_000;
    private static int NUMBER_OF_FILES = 1000;
    private static int NUMBER_OF_THREADS = 50;

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
//        makeFiles("csv");
        analysis("csv");
        System.out.println("time spent: " + (System.currentTimeMillis() - time) / 1000 + " seconds");
    }

    private static void makeFiles(String dir) {
        RowRegistry rows = new RowRegistry();
        Semaphore semaphore = new Semaphore(NUMBER_OF_THREADS);
        for (int i = 0; i < NUMBER_OF_FILES; i++) {
            Thread thread = new Thread(new CSVFileWriter(rows, dir, ((Integer) i).toString(), semaphore));
            thread.start();
        }
    }

    private static void analysis(String dir) {
        Semaphore semaphore = new Semaphore(NUMBER_OF_THREADS);
        ResultMap resultMap = new ResultMap();

        String[] files =  new File(dir).list();
        List<Thread> threads = new ArrayList<>(NUMBER_OF_THREADS);

        for (String fileName : files) {
            Thread thread = new Thread(new CSVReader(
                    new File(dir + File.separator + fileName)
                    , resultMap
                    , semaphore
            ));
            thread.start();
            threads.add(thread);
        }
        threads.iterator().forEachRemaining(element -> {
            try {
                element.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(new File("output.csv")))) {
            for (TreeMap<RowObject, Integer> resMapItem : resultMap.values()) {
                for (RowObject row : resMapItem.keySet()) {
                    for (int i = 0; i < resMapItem.get(row); i++) {
                        try {
                            bufWriter.write(row.toString());
                            bufWriter.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}