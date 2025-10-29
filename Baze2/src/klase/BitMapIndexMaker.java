package klase;

import java.util.List;

public class BitMapIndexMaker {
    String[] stundetIdBM=new String[9]; ;
    String[] indexIdBM=new String[9]; ;
    String[] ispitIdBM=new String[9]; ;

    public String makeBitRep(List<Fakat> fkt, int id, int flag){
        StringBuilder str = new StringBuilder();
        for (Fakat f : fkt) {
            switch (flag) {
                case 0:
                    str.append(f.getStudentId() == id ? "1" : "0");
                    break;
                case 1:
                    str.append(f.getIndeksId() == id ? "1" : "0");
                    break;
                case 2:
                    str.append(f.getIspitId() == id ? "1" : "0");
                    break;
            }
        }
        return str.toString();
    }

    public BitMapIndexMaker(List<Fakat> tbf){
        for (int i=0; i<9; i++){
            stundetIdBM[i] = makeBitRep(tbf, i+1, 0);
            indexIdBM[i] = makeBitRep(tbf, i+1, 1);
            ispitIdBM[i] = makeBitRep(tbf, i+1, 2);
        }
    }

    public void printBM(){
        System.out.println("Student");
        for (String str : this.stundetIdBM){
            System.out.println(str);
        }
        System.out.println("Indeks");
        for (String str : this.indexIdBM){
            System.out.println(str);
        }
        System.out.println("Ispit");
        for (String str : this.ispitIdBM){
            System.out.println(str);
        }
    }

    public int[] ids(int id, int flag, int max){
        int[] niz = new int[max];
        int cnt = 0;
        String str = "";
        switch (flag){
            case 0:
                str = stundetIdBM[id-1];
                break;
            case 1:
                str = indexIdBM[id-1];
                break;
            case 2:
                str = ispitIdBM[id-1];
                break;
        }
        for (int i = 0; i<str.length(); i++){
            if (str.charAt(i)=='1') niz[cnt++]=i+1;
        }
        return niz;
    }

}
