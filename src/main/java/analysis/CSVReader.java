package analysis;

import make.RowObject;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class CSVReader implements Runnable {
    private File file;
    private ResultMap resultMap;
    private Semaphore semaphore;

    public CSVReader(File file, ResultMap resultMap, Semaphore semaphore) {
        this.file = file;
        this.resultMap = resultMap;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + "  started");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine())!= null){
                    RowObject rowObject = new RowObject("empty");
                    int index = 0;
                    try (Scanner scanner = new Scanner(line)){
                        scanner.useDelimiter(",");
                        while (scanner.hasNext()){
                            String data = scanner.next();
                            switch (index){
                                case 0:
                                    rowObject.setProductID(Integer.parseInt(data));
                                    break;
                                case 1:
                                    rowObject.setName(data);
                                    break;
                                case 2:
                                    rowObject.setCondition(data);
                                    break;
                                case 3:
                                    rowObject.setState(data);
                                    break;
                                case 4:
                                    rowObject.setPrice(Float.parseFloat(data));
                                    break;
                            }
                            index++;
                        }
                    }
                    resultMap.insert(rowObject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + "  finished");
        }
    }
}
