package klase;

public class Fakat {
    private int id;
    private String student;
    private String indeks;
    private String ispit;
    private int brojPrijavaIspita;
    private int ocena;

    private int studentId;
    private int IndeksId;
    private int IspitId;

    public Fakat(int id, String student, String indeks, String ispit, int brojPrijavaIspita, int ocena, int studentId, int indeksId, int ispitId ) {
        this.id = id;
        this.student = student;
        this.indeks = indeks;
        this.ispit = ispit;
        this.brojPrijavaIspita = brojPrijavaIspita;
        this.ocena = ocena;
        this.studentId = studentId;
        this.IndeksId = indeksId;
        this.IspitId = ispitId;
    }

    public int getId() {
        return id;
    }

    public String getStudent() {
        return student;
    }

    public String getIndeks() {
        return indeks;
    }

    public String getIspit() {
        return ispit;
    }

    public int getBrojPrijavaIspita() {
        return brojPrijavaIspita;
    }

    public int getOcena() {
        return ocena;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getIndeksId() {
        return IndeksId;
    }

    public int getIspitId() {
        return IspitId;
    }
    public String toStringWithValues(){
        return id + " " + student + " " + indeks + " " + ispit + " " + brojPrijavaIspita + " " + ocena;
    }
    @Override
    public String toString() {
        return id + " " + studentId + " " + IndeksId + " " + IspitId + " " + brojPrijavaIspita + " " + ocena;
    }

    public void printFakatWithAll(){
       System.out.println(id + " " + studentId + " " + IndeksId + " " + IspitId + " " + brojPrijavaIspita + " " + ocena + " " + student + " " + indeks + " " + ispit);

    }
}
