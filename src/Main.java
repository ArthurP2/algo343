import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Marky on 5/21/2017.
 */
public class Main {

    public static void main(String[] args){
        Random2DArray r = new Random2DArray();
        String f = "test.txt";
        Scanner sc = null;
        Scanner sc2 = null;
        try {
            sc = new Scanner(new File(f));
            sc2 = new Scanner(new File(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int n = 0;
        while (sc.hasNextLine()) {
            Scanner scWords = new Scanner(sc.nextLine());
            while (scWords.hasNext()) {
                String s = scWords.next();
                n++;
            }
            break;
        }
        int[][] canoeArray = new int[n][n];
        int qq = 0;
        while (sc2.hasNextLine()) {
            Scanner scWords = new Scanner(sc2.nextLine());
            int ii = 0;
            while (scWords.hasNext()) {
                String s = scWords.next();
                int x = 0;
                if (!s.equals("NA")) {
                    x = Integer.parseInt(s);
                }
                canoeArray[qq][ii] = x;
                //System.out.print(s + "|");
                ii++;
            }
            qq++;
            //System.out.println();
        }
        for (int xx = 0; xx < n; xx++){
            for (int yy = 0; yy < n; yy++){
                System.out.print(canoeArray[xx][yy] + "|");
            }
            System.out.println();
        }

        sc.close();
        sc2.close();
    }

}
