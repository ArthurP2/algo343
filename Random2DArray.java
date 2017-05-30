import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Arthur and Scott on 5/21/2017.
 */
public class Random2DArray {



    public static void main(String[] args){
        try {
            int[][] random;
            int n = 20;
            Random rand = new Random();
            File file = new File("test" + Integer.toString(n) + ".txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            random = new int[n][n];
            for (int ii = 0; ii < n; ii++) {
                int inc = 0;
                for (int qq = 0; qq < n; qq++) {
                    int x = rand.nextInt(9) + 1 + inc;
                    random[ii][qq] = x;
                    if (ii == qq) {
                        random[ii][qq] = 0;
                    } else if(ii > qq) {
                        random[ii][qq] = -1;
                    }
                    else {
                        inc = x + rand.nextInt(2) + 1;
                    }
                    if (random[ii][qq] != -1) {
                        bw.write(Integer.toString(random[ii][qq]) + "\t");
                    } else {
                        bw.write("NA" + "\t");
                    }
                    System.out.print(random[ii][qq] + "|");

                }
                bw.newLine();
                System.out.println();

            }
            bw.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }



}
