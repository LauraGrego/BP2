package klase;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FakatTableGenerator {
    private static final int MAX_ROWS = 10000;
    private List<String> Studenti = new ArrayList<>();
    private List<String> Indeksi = new ArrayList<>();
    private List<String> Ispiti = new ArrayList<>();

    private List<Integer> studentiId = new ArrayList<>();
    private List<Integer> indeksiId = new ArrayList<>();
    private List<Integer> ispitiId = new ArrayList<>();

    private List<Fakat> tabelaFakata = new ArrayList<>();

    public FakatTableGenerator(String studentFilePath, String indeksFilePath, String ispitFilePath) {
        TableReader studentReader = new TableReader();
        studentReader.readFile(studentFilePath);
        for (Pair<Integer, String> pair : studentReader.getTable().subList(1, studentReader.getTable().size())) {
            Studenti.add(pair.getValue());
            studentiId.add(pair.getKey());
        }

        TableReader indeksReader = new TableReader();
        indeksReader.readFile(indeksFilePath);
        for (Pair<Integer, String> pair : indeksReader.getTable().subList(1, indeksReader.getTable().size())) {
            Indeksi.add(pair.getValue());
            indeksiId.add(pair.getKey());
        }

        TableReader ispitReader = new TableReader();
        ispitReader.readFile(ispitFilePath);
        for (Pair<Integer, String> pair : ispitReader.getTable().subList(1, ispitReader.getTable().size())) {
            Ispiti.add(pair.getValue());
            ispitiId.add(pair.getKey());
        }
    }

    public void generateFactTable() {
        List<Fakat> factTable = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= MAX_ROWS; i++) {
            int studentIndex = random.nextInt(Studenti.size());
            int indeksIndex = random.nextInt(Indeksi.size());
            int ispitIndex = random.nextInt(Ispiti.size());

            String student = Studenti.get(studentIndex);
            String indeks = Indeksi.get(indeksIndex);
            String ispit = Ispiti.get(ispitIndex);

            int studentId = studentiId.get(studentIndex);
            int indeksId = indeksiId.get(indeksIndex);
            int ispitId = ispitiId.get(ispitIndex);

            int brojPrijavaIspita = random.nextInt(5) + 1;
            int ocena = random.nextInt(6) + 5;

            factTable.add(new Fakat(i, student, indeks, ispit, brojPrijavaIspita, ocena, studentId, indeksId, ispitId));
        }

        this.tabelaFakata = factTable;
    }

    public List<Fakat> getFakatList() {

        return this.tabelaFakata;
    }

    public void printFactTable(boolean printValues) {
        System.out.println("ID Student Indeks Ispit BrojPrijavaIspita Ocena");
        for (Fakat fact : this.tabelaFakata) {
            if (printValues) {
                System.out.println(fact.toStringWithValues());
            } else {
                System.out.println(fact);
            }
        }
    }

    public void saveFactTableToFile(String filePath, boolean useValues) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID StudentId IndeksId IspitId BrojPrijavaIspita Ocena\n");
            for (Fakat fact : tabelaFakata) {
                if (useValues) {
                    writer.write(fact.toStringWithValues() + "\n");
                } else {
                    writer.write(fact.toString() + "\n");
                }
            }
            System.out.println("Fact table saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
