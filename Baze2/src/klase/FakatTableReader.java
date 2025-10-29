package klase;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class FakatTableReader {
    private List<Fakat> tabelaFakata;

    public FakatTableReader(List<Fakat> tbf) {
        this.tabelaFakata = tbf;
    }


    public List<Fakat> filterByConditionWithBM(String con, BitMapIndexMaker bm) {
        Instant start = Instant.now();

        List<Fakat> filteredList = new ArrayList<>();
        String[] cons = con.split("\\s*OR\\s*");
        Set<Integer> allTheIds = new HashSet<>();

        for (String orCondition : cons) {
            String[] andConditions = orCondition.replace("(", "").replace(")", "").split("\\s*AND\\s*");
            List<Set<Integer>> andResults = new ArrayList<>();

            for (String andCondition : andConditions) {
                String[] kv = andCondition.split("=");
                int val = Integer.parseInt(kv[1].trim());
                String key = kv[0].trim();
                int[] ids = null;

                switch (key) {
                    case "studentID":
                        ids = bm.ids(val, 0, 10000);
                        break;
                    case "indexID":
                        ids = bm.ids(val, 1, 10000);
                        break;
                    case "ispitID":
                        ids = bm.ids(val, 2, 10000);
                        break;
                }

                if (ids != null) {
                    Set<Integer> idSet = new HashSet<>();
                    for (int id : ids) {
                        if (id > 0 && id <= tabelaFakata.size()) {
                            idSet.add(id);
                        }
                    }
                    andResults.add(idSet);
                }
            }

            if (!andResults.isEmpty()) {
                Set<Integer> intersection = new HashSet<>(andResults.get(0));
                for (Set<Integer> set : andResults) {
                    intersection.retainAll(set);
                }
                allTheIds.addAll(intersection);
            }
        }

        for (int id : allTheIds) {
            if (id > 0 && id <= tabelaFakata.size()) {
                filteredList.add(tabelaFakata.get(id - 1));
            }
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        long millis = duration.toMillis();
        Collections.sort(filteredList, Comparator.comparingInt(Fakat::getId));
        System.out.println("Duration with bitmap index");
        System.out.println("Duration in seconds: " + seconds);
        System.out.println("Duration in milliseconds: " + millis);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        return filteredList;
    }

    public List<Fakat> filterByCondition(String condition) {
        Instant start = Instant.now();

        List<Fakat> filteredList = new ArrayList<>();
        for (Fakat fakat : tabelaFakata) {
            if (evaluateCondition(fakat, condition)) {
                filteredList.add(fakat);
            }
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        long millis = duration.toMillis();
        System.out.println("Duration for search without index");
        System.out.println("Duration in seconds: " + seconds);
        System.out.println("Duration in milliseconds: " + millis);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        return filteredList;

    }

    private boolean evaluateCondition(Fakat fakat, String condition) {
        condition = condition
                .replace("studentID", String.valueOf(fakat.getStudentId()))
                .replace("indexID", String.valueOf(fakat.getIndeksId()))
                .replace("ispitID", String.valueOf(fakat.getIspitId()))
                .replace("=", "==")
                .replace("AND", "&&")
                .replace("OR", "||");

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            return (Boolean) engine.eval(condition);
        } catch (ScriptException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void ispisTabeleSFunkcjiom(String funkcija, List<Fakat> tf) {
        Map<String, Integer> brojPrijavaIspitaAgg = new HashMap<>();
        Map<String, Integer> ocenaAgg = new HashMap<>();
        Map<String, Integer> countAgg = new HashMap<>();

        for (Fakat fakat : tf) {
            String key = fakat.getStudent() + "_" + fakat.getIndeks() + "_" + fakat.getIspit();

            if (funkcija=="min"){
                brojPrijavaIspitaAgg.putIfAbsent(key, Integer.MAX_VALUE);
                ocenaAgg.putIfAbsent(key, Integer.MAX_VALUE);
            }
            else if (funkcija=="max"){
                brojPrijavaIspitaAgg.putIfAbsent(key, Integer.MIN_VALUE);
                ocenaAgg.putIfAbsent(key, Integer.MIN_VALUE);
            }
            else{
                brojPrijavaIspitaAgg.putIfAbsent(key, 0);
                ocenaAgg.putIfAbsent(key, 0);
            }
            countAgg.putIfAbsent(key, 0);

            switch (funkcija) {
                case "min":
                    int currentMin = ocenaAgg.getOrDefault(key, Integer.MAX_VALUE);
                    ocenaAgg.put(key, Math.min(currentMin, fakat.getOcena()));
                    int currentMin2 = brojPrijavaIspitaAgg.getOrDefault(key, Integer.MAX_VALUE);
                    brojPrijavaIspitaAgg.put(key, Math.min(currentMin2, fakat.getBrojPrijavaIspita()));
                    break;
                case "max":
                    int currentMax = ocenaAgg.getOrDefault(key, Integer.MIN_VALUE);
                    ocenaAgg.put(key, Math.max(currentMax, fakat.getOcena()));
                    int currentMax2 = brojPrijavaIspitaAgg.getOrDefault(key, Integer.MIN_VALUE);
                    brojPrijavaIspitaAgg.put(key, Math.max(currentMax2, fakat.getBrojPrijavaIspita()));
                    break;
                case "avg":
                    int count = countAgg.get(key);
                    count++;
                    countAgg.put(key, count);

                    int currentSumBrojPrijavaIspita = brojPrijavaIspitaAgg.get(key);
                    currentSumBrojPrijavaIspita += fakat.getBrojPrijavaIspita();
                    brojPrijavaIspitaAgg.put(key, currentSumBrojPrijavaIspita);

                    int currentSumOcena = ocenaAgg.get(key);
                    currentSumOcena += fakat.getOcena();
                    ocenaAgg.put(key, currentSumOcena);
                    break;
                case "count":
                    brojPrijavaIspitaAgg.put(key, brojPrijavaIspitaAgg.getOrDefault(key, 0) + 1);
                    ocenaAgg.put(key, ocenaAgg.getOrDefault(key, 0) + 1);
                    break;
                case "sum":
                    ocenaAgg.put(key, ocenaAgg.getOrDefault(key, 0) + fakat.getOcena());
                    brojPrijavaIspitaAgg.put(key, brojPrijavaIspitaAgg.getOrDefault(key, 0) + fakat.getBrojPrijavaIspita());
                    break;
            }
        }
        System.out.println("Student Indeks Ispit (" + funkcija + ")BrojPrijavaIspita (" + funkcija + ")Ocena");
        for (Map.Entry<String, Integer> entry : brojPrijavaIspitaAgg.entrySet()) {
            String[] parts = entry.getKey().split("_");
            String student = parts[0];
            String indeks = parts[1];
            String ispit = parts[2];
            int countAvg = brojPrijavaIspitaAgg.getOrDefault(entry.getKey(), 0);
            int currentAggValue = ocenaAgg.getOrDefault(entry.getKey(), 0);
            int count = countAgg.getOrDefault(entry.getKey(), 0);

            if ("avg".equals(funkcija)) {
                double avgBrojPrijavaIspita = count > 0 ? brojPrijavaIspitaAgg.get(entry.getKey()) * 1.0 / count : 0.0;
                double avgOcena = count > 0 ? ocenaAgg.get(entry.getKey()) * 1.0 / count : 0.0;
                System.out.printf("%s %s %s %.2f %.2f%n", student, indeks, ispit, avgBrojPrijavaIspita, avgOcena);
            } else {
                System.out.printf("%s %s %s %d %d%n", student, indeks, ispit, countAvg, currentAggValue);
            }
        }
    }

    public void printFilteredTable(List<Fakat> filteredList, boolean printValues) {
        if (printValues) {
            System.out.println("ID Student Indeks Ispit BrojPrijavaIspita Ocena");
            for (Fakat fact : filteredList) {
                System.out.println(fact.toStringWithValues());
            }
        } else {
            System.out.println("ID StudentId IndeksId IspitId BrojPrijavaIspita Ocena");
            for (Fakat fact : filteredList) {
                System.out.println(fact.toString());
            }
        }
    }

}
