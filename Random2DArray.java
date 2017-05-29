import java.util.Random;

/**
 * Created by Arthur and Scott on 5/21/2017.
 */
public class Random2DArray {

    int[][] random;

    public Random2DArray(int n){

        Random rand = new Random();


        random = new int[n][n];
        for (int ii = 0; ii < n; ii++){
            int inc = 0;
            for (int qq = 0; qq < n; qq++){
                int x = rand.nextInt(9) + 1 + inc;
                random[ii][qq] = x;
                if (ii >= qq){
                    random[ii][qq] = 0;
                } else {
                    inc = x + rand.nextInt(2) + 1;
                }
                System.out.print(random[ii][qq] + "|");

            }
            System.out.println();

        }

    }

    public int[][] giveMeArray(){
        return random;
    }

}
