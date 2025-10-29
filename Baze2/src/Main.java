import klase.*;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String studentFilePath = "src/Student.txt";
        String indeksFilePath = "src/Indeks.txt";
        String ispitFilePath = "src/Ispit.txt";

        FakatTableGenerator generator = new FakatTableGenerator(studentFilePath, indeksFilePath, ispitFilePath);

        generator.generateFactTable();
        //generator.printFactTable(false);
        generator.saveFactTableToFile("TabelaFakata.txt",false);
        System.out.println("----------------------------------------");
        List<Fakat> tbf = generator.getFakatList();

        FakatTableReader tableReader = new FakatTableReader(tbf );
        String condition =  "(studentID=4 AND indexID=5) OR (studentID=3 AND ispitID=6)";
        System.out.println("__________________________________");
        System.out.println("__________________________________");

        List<Fakat> filteredList = tableReader.filterByCondition(condition);
        System.out.println("\nTabela fakata bez bitmap indeksa");
        tableReader.printFilteredTable(filteredList, true);
        BitMapIndexMaker bm = new BitMapIndexMaker(tbf);
        //bm.printBM();
        System.out.println("\nTabela fakata sa bitmap indeksima");
        System.out.println("__________________________________");
        List<Fakat> f = tableReader.filterByConditionWithBM(condition,bm);
        tableReader.printFilteredTable(f, true);
        System.out.println("\nTabela fakata sa bitmap indeksima i agregatnom funkcjom");
        System.out.println("__________________________________");
        String[] agFs =  {"min", "max", "avg", "sum", "count"};
        Random random = new Random();
        int randomNumber = random.nextInt(5);
        tableReader.ispisTabeleSFunkcjiom(agFs[randomNumber], f);

    }
}