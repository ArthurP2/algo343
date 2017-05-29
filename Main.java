import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Arthur and Scott on 5/21/2017.
 */
public class Main {
	
	private static int n;
	private static ArrayList<Integer> x1;
    private static ArrayList<Integer> x2;
    private static TreeMap<Integer, ArrayList<Integer>> pathCosts;
    private static int[][] canoeArray;

    public static void main(String[] args){
        x1 = new ArrayList<>();
        x2 = new ArrayList<>();
        pathCosts = new TreeMap<>();
        String f = "test2";
        Scanner sc = null;
        Scanner sc2 = null;
        try {
            sc = new Scanner(new File(f));
            sc2 = new Scanner(new File(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        n = 0;
        while (sc.hasNextLine()) {
            Scanner scWords = new Scanner(sc.nextLine());
            while (scWords.hasNext()) {
                String s = scWords.next();
                n++;
            }
            break;
        }
        canoeArray = new int[n][n];
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
        Random2DArray r = new Random2DArray(n);
        canoeArray = r.giveMeArray();
        brute(canoeArray);
        dynamic(canoeArray);
        int[] cheapestCost = divide(0);
        String minPathDivide = buildDividePath(cheapestCost);
        System.out.println("Divide and Conquer Algorithm");
        System.out.println("Minimum Path:" + minPathDivide + "Minimum cost: " + cheapestCost[0]);
        sc.close();
        sc2.close();
    }

    public static void brute(int[][] canoe) {
        final int nVal = canoe.length;
        int minVal = -1;
        Set<Integer> minSet = new TreeSet<>();

        HashSet<Integer> startingSet = new HashSet<>();                         /* This set will hold 1 ..... n values. */
        for(int i = 1; i <= nVal; i++) {                                        /* Populates the starting set */
            startingSet.add(i);
        }

        Set<TreeSet<Integer>> setsOSets = getPowerSetIterative(startingSet);    /* Get the power set*/

        System.out.println("Brute Force Algorithm");
        for(Set<Integer> currSet : setsOSets) {
            Integer pathSum = 0;                                                /* Total cost for this path. */
            ArrayList<Integer> setList = new ArrayList<>(currSet);

            if(currSet.contains(1) && currSet.contains(nVal)) {                 /* If it contains 1 and n, get min value. */
                //TODO uncomment following line to view valid subsets.
                //System.out.println(currSet);
                for(int i = 0; i < currSet.size() - 1; i++) {                   /* i = Rx, i + 1 = Ry */
                    int cRow = setList.get(i) - 1;                          /* Get the row in the array of the path cost. */
                    int cCol = setList.get(i + 1) - 1;                      /* Get the col in the array of the path cost. */
                    pathSum += canoe[cRow][cCol];
                }

                if(pathSum < minVal || minVal == -1) {                          /* Assign minVal if pathSum is smaller. */
                    minVal = pathSum;
                    minSet = currSet;
                }
            }
        }

        /* Display the minimum set. */
        System.out.println("Minimum path: " + minSet.toString() + ", Minimum cost: " + minVal);
    }

    public static Set<TreeSet<Integer>> getPowerSetIterative(Set<Integer> theStartingSet) {
        Set<TreeSet<Integer>> powerSet = new HashSet<>();           //result set
        powerSet.add(new TreeSet<>());                              //base: add empty set

        for (Integer currentInt : theStartingSet) {                 //for every number in the range from 1 to n
            Set<TreeSet<Integer>> newSet = new HashSet<>();         //current set to replace the powerset

            for (TreeSet<Integer> subset : powerSet) {              //for every subset in the powerset so far
                if (subset.contains(1) || subset.contains(n-1)) {  //make sure old subsets that actually have
                    //useful values are kept in the powerset
                    newSet.add(subset);                            //add the current subset to the new powerset
                }

                TreeSet<Integer> newSubset = new TreeSet<>(subset); //Copy the current subset
                newSubset.add(currentInt);                          //and add the current int to it
                newSet.add(newSubset);                              //add the newly formed subset with one more element
                //than the previous to the powerset
            }
            powerSet = newSet;                                      //reassign powerset
        }
        return powerSet;

    }


    private static String buildDividePath(int[] minCost) {
        StringBuilder sb = new StringBuilder();
        sb.append("[1");
        for(int i = 1; i < minCost.length; i++) {
            if(minCost[i] > 0) {
                sb.append(", ");
                sb.append(minCost[i]);
            }
        }
        sb.append("]");

        return sb.toString();
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
        Integer[][] cheapArr = new Integer[n][n];

        /* Fill in top row of solution array
         * Will always be the same as the top row of the input
         */
        for (int i = 0 ; i < n; i++) {
                cheapArr[0][i] = arr[0][i];
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
                    if (cheapArr[i][k]
                            + arr[i][j] < minValue
                            || minValue == -1) {
                        minValue = cheapArr[i][k] + arr[i][j];
                    }
                }
                //find the minimum value of all cells above in the same column of the current cell
                //if any of these values are less than the current minimum obtained from looking to the left,
                //update the minimum to the value above as it is more optimal.
                for (int k = 0; k < i; k++) {
                    if (arr[k][j] != -1) {

                        if (cheapArr[k][j] < minValue || minValue == -1) {
                            minValue = cheapArr[k][j];
                        }
                    }
                }
                //Finally, update the current cell to the most optimal value obtained from the above loops.
                cheapArr[i][j] = minValue;
            }
        }

        System.out.println("Dynamic Programming Algorithm");

        System.out.println("Minimum path: " + recover(cheapArr).toString() + ", Minimum cost: " + cheapArr[n - 1][n - 1]);
    }
    public static int[] divide(int i) {
        int minVal = Integer.MAX_VALUE;
        int minJ = Integer.MAX_VALUE;
        int[] arr = new int[n + 1];

        if(i == n - 1) {        /* BASE CASE */
            arr[0] = 0;
            return arr;
        } else {
            for(int j = i + 1; j < n; j++) {
                int[] curArr = divide(j);
                int curVal = curArr[0] + canoeArray[i][j];

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
        //System.out.println("i: " + (i + 1) + ", j: " + (minJ + 1) + ", return value: " + retVal);
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
