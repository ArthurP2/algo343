import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Marky on 5/21/2017.
 */
public class Main {
	

	private static int n;
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
        canoeArray = r.giveMeArray();
        brute(canoeArray);
        dynamic(canoeArray);
        divide(10, canoeArray);
        sc.close();
        sc2.close();
    }

    public static void brute(int[][] arr) {

        int numPosts = arr.length;

        // A map containing paths as key, costs as value.
        // TreeMap uses a Red-Black tree, log(n) insertion
        TreeMap<Integer, ArrayList<Integer>> pathCosts = new TreeMap<>();

        //O(n^2) runtime
        Set<ArrayList<Integer>> possibleSolutions = getPossiblePaths(numPosts);

        //Fill map
        for (ArrayList<Integer> row : possibleSolutions) {
            int cost = 0;
            Integer atual = null;
            for (Integer next : row) {
                if (atual != null) {
                    cost += arr[atual - 1][next - 1];
                }
                atual = next;
            }
            pathCosts.put(cost, row);
        }
        System.out.println("Best path via Brute Force: " + pathCosts.firstEntry().getValue() + " with cost of " + pathCosts.firstKey());

    }
    public static Set<ArrayList<Integer>> getPossiblePaths(int numPosts) {
        Set<ArrayList<Integer>> pathList = new HashSet<>();

        for (int p = numPosts; p > 2; p--) {
            for (int i = 2; i < numPosts; i++) {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(1);
                for (int j = i; j < p; j++) {
                    arr.add(j);
                }
                arr.add(numPosts);
                pathList.add(arr);
            }
        }

        return pathList;
    }
    public static void dynamic(int[][] arr) {
        int n = arr[0].length;
        Integer[][] solutionArr = new Integer[n][n];

        /* Fill in top row of solution array
         * Will always be the same as the top row of the input
         */
        for (int i = 0 ; i < n; i++) {
                solutionArr[0][i] = arr[0][i];
        }

        //Top to bottom
        for (int i = 1; i < n; i++) {
            //Left to right
            for (int j = i; j < n; j++) {
                int minValue = -1;

                //Find the minimum value of all values to the left of the current cell [i][j]
                //added onto the current cell. That is, the most optimal previous value plus the price
                //of renting a canoe in this particular column.
                for (int k = i; k < j; k++) {
                    if (solutionArr[i][k]
                            + arr[i][j] < minValue
                            || minValue == -1) {
                        minValue = solutionArr[i][k] + arr[i][j];
                    }
                }
                //find the minimum value of all cells above in the same column of the current cell
                //if any of these values are less than the current minimum obtained from looking to the left,
                //update the minimum to the value above as it is more optimal.
                for (int k = 0; k < i; k++) {
                    if (arr[k][j] != -1) {

                        if (solutionArr[k][j] < minValue || minValue == -1) {
                            minValue = solutionArr[k][j];
                        }
                    }
                }
                //Finally, update the current cell to the most optimal value obtained from the above loops.
                solutionArr[i][j] = minValue;
            }
        }

        System.out.println("Dynamic Programming Algorithm");


        System.out.println("Minimum path: " + recover(solutionArr).toString() + ", Minimum cost: " + solutionArr[n - 1][n - 1]);
    }
    public static int[] divide(int i, int[][] arr1) {
        int minVal = Integer.MAX_VALUE;
        int minJ = Integer.MAX_VALUE;
        int[] arr = new int[n + 1];

        if(i == n - 1) {        /* BASE CASE */
            arr[0] = 0;
            return arr;
        } else {
            for(int j = i + 1; j < n; j++) {
                int[] curArr = divide(j, arr1);
                int curVal = curArr[0] + arr1[i][j];

                if (curVal < minVal) {
                    minVal = curVal;
                    minJ = j;

                    System.arraycopy(curArr, 0, arr, 0, arr.length); //O(n)
                }
            }
        }
        arr[0] = minVal;                /* Updates the minimum value. */

        /* Add the current solution j value to the argument. */
        arr[i + 1] = minJ + 1;
        return arr;
    }
    
    public static Set<Integer> recover(Integer[][] cheapestArr) {
        int n = cheapestArr[0].length;
        Set<Integer>  cheap = new TreeSet<>(); /*O(log(n))*/

        cheap.add(1); cheap.add(n);
        int row = n - 1, col = n - 1;
        while (row > 0) {

            int current = cheapestArr[row][col];
            int above   = cheapestArr[row-1][col];

            if (current == above) {
                row--;
            } else {
                int min = Integer.MAX_VALUE;
                int minIndex = Integer.MAX_VALUE;
                int i;
                for (i = row; i < col; i++) {

                    if (cheapestArr[row][i] < min) {
                        min = cheapestArr[row][i];
                        minIndex = i;
                    }
                }
                cheap.add(minIndex + 1);
                col = minIndex;
            }
        }
        return cheap;
    }
}
