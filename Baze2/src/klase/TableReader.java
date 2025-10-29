package klase;

import java.io.*;
import java.util.*;

public class TableReader {
    private List<Pair<Integer, String>> table;

    public TableReader() {
        table = new ArrayList<>();
    }

    public void readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            String columnName = "";
            int index = 1;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    columnName = line;
                    isFirstLine = false;
                } else {
                    String[] parts = line.split(" ", 2);
                    if (parts.length == 2) {
                        table.add(new Pair<>(index++, parts[1].trim().replace('-',' ')));
                    }
                }
            }
            table.add(0, new Pair<>(0, columnName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pair<Integer, String>> getTable() {
        return table;
    }

    public void printTable() {
        for (Pair<Integer, String> pair : table) {
            System.out.println(pair);
        }
    }
}
