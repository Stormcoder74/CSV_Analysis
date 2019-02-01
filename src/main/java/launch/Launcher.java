package launch;

import analysis.CSVReader;
import analysis.CSV_Key;
import analysis.ResultMap;
import make.CSVFileMaker;
import make.RowObject;
import make.RowRegistry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Launcher {
//    public static int NUMBER_OF_FILES = 100_000;
    public static int NUMBER_OF_FILES = 1000;
    public static int NUMBER_OF_THREADS = 100;

    public static void main(String[] args) {
//        makeFiles("csv");
        analysis("csv");
    }

    private static void makeFiles(String dir){
        RowRegistry rows = new RowRegistry();
        Semaphore semaphore = new Semaphore(NUMBER_OF_THREADS);
        for (int i = 0; i < NUMBER_OF_FILES; i++){
            Thread thread = new Thread(new CSVFileMaker(rows, dir, ((Integer) i).toString(), semaphore));
            thread.start();
        }
    }

    private static void analysis(String dir){
        Semaphore semaphore = new Semaphore(NUMBER_OF_THREADS);
        ResultMap resultMap = new ResultMap();
        String[] files =  new File(dir).list();
//        String[] files = {"0.csv"} ;
        List<Thread> threads = new ArrayList<>(NUMBER_OF_THREADS);

        for(String fileName: files){
            Thread thread = new Thread(new CSVReader(
                    new File(dir + File.separator + fileName)
                    , resultMap
                    , semaphore
            ));
            thread.start();
            System.out.println(thread.getName() + " started");
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
            for (Map.Entry<Float, List<RowObject>> row: resultMap.entrySet()) {
                row.getValue().iterator().forEachRemaining(element -> {
                    try {
                        bufWriter.write(element.toString());
                        bufWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
