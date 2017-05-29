import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Arthur and Scott on 5/21/2017.
 */
public class Main {
	//Size of the 2D matrix
	private static int n;

	//The 2D matrix of the canoe problem
    private static int[][] canoeArray;

    public static void main(String[] args){
        String f;
        // Get the fileName from the terminal or else return and exit.
        if(args.length > 0) {
            f = args[0];
            System.out.println("File succesfully read in, File size: " + n);
        } else {
            f= "test.txt";
            System.out.println("No input file given.");
            //  return;
        }
        Scanner sc = null;
        Scanner sc2 = null;
        try {
            sc = new Scanner(new File(f));
            sc2 = new Scanner(new File(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        n = 0;
        //Getting the length of the 2D array.
        while (sc.hasNextLine()) {
            Scanner scWords = new Scanner(sc.nextLine());
            while (scWords.hasNext()) {
                String s = scWords.next();
                n++;
            }
            break;
        }
        canoeArray = new int[n][n];
        //Filling the 2D array with the canoe values.
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
                ii++;
            }
            qq++;
        }

        //Printing the contents of the array from the file.
        for (int xx = 0; xx < n; xx++){
            for (int yy = 0; yy < n; yy++){
                System.out.print(canoeArray[xx][yy] + "|");
            }
            System.out.println();
        }
        //Creates a randomArray generator object.
        Random2DArray r = new Random2DArray(n);
        //Get a random array.
        canoeArray = r.giveMeArray();
        long start, end;
        start = System.currentTimeMillis();
        brute();
        end = System.currentTimeMillis();
        System.out.println("Brute took " + (end - start) + " milliseconds\n");

        start = System.currentTimeMillis();
        dynamic();
        end = System.currentTimeMillis();
        System.out.println("Dynamic took " + (end - start) + " milliseconds\n");

        start = System.currentTimeMillis();
        int[] cheapestCost = divide(0);
        end = System.currentTimeMillis();
        String minPathDivide = buildDividePath(cheapestCost);
        System.out.println("Divide and Conquer Algorithm");
        System.out.println("Minimum Path:" + minPathDivide + "Minimum cost: " + cheapestCost[0]);
        System.out.println("Dynamic took " + (end - start) + " milliseconds\n");

        sc.close();
        sc2.close();
    }

    /**
     * Brute Force. O(2^n) runtime.
     */
    public static void brute() {
        int minimum = -1;
        Set<Integer> minPath = new TreeSet<>();
        //Will put each location in Array in a set
        TreeSet<Integer> allLocations = new TreeSet<>();
        for(int ii = 1; ii <= n; ii++) {
            allLocations.add(ii);

        }
        //Get all paths possible form canoe array
        Set<TreeSet<Integer>> allPaths = getAllPathsInSet(allLocations);
        System.out.println("Brute Force Algorithm [O(2^n)]:");
        //Goes through all combonations, so 2^n.
        for(Set<Integer> pathCombo : allPaths) {
            //This will be the total cost for the path.
            Integer totalPath = 0;
            ArrayList<Integer> setList = new ArrayList<>(pathCombo);
            //If the possible combo has 1 and the N length, it must be one of the correct combos.
            if(pathCombo.contains(1) && pathCombo.contains(n)) {
                for(int ii = 0; ii < pathCombo.size() - 1; ii++) {
                    //Go through the array diagonally.
                    int xx = setList.get(ii) - 1;
                    int yy = setList.get(ii + 1) - 1;

                    totalPath += canoeArray[xx][yy];
                }
                //Check if the total path is smaller than -1.
                if( minimum == -1 || totalPath < minimum ) {
                    minimum = totalPath;
                    minPath = pathCombo;
                }
            }
        }

        // Display the minimum set
        System.out.println("Cheapest Path: " + minPath.toString() + ", Cheapest Cost: " + minimum);
    }

    /**
     * Gets all possible paths in a set.
     *
     * @param thePaths
     * @return a set of all possible paths.
     */
    public static Set<TreeSet<Integer>> getAllPathsInSet(Set<Integer> thePaths) {
        Set<TreeSet<Integer>> allPaths = new HashSet<>();
        //This set will hold tree sets, since it is the holder of all possible sets in the path.
        allPaths.add(new TreeSet<>());

        for (Integer xx : thePaths) {
            Set<TreeSet<Integer>> newCombos = new HashSet<>();

            for (TreeSet<Integer> combo : allPaths) {
                if (combo.contains(1) || combo.contains(n-1)) {
                    newCombos.add(combo);
                }

                TreeSet<Integer> newSubset = new TreeSet<>(combo);
                newSubset.add(xx);
                newCombos.add(newSubset);

            }
            allPaths = newCombos;
        }
        return allPaths;

    }


    /**
     * dynamic. O(nLogn)
     *
     */
    public static void dynamic() {

        Integer[][] cheapestPaths = new Integer[n][n];

        /* Fill in top row of solution array
         * Will always be the same as the top row of the input
         */
        for (int ii = 0 ; ii < n; ii++) {
                cheapestPaths[0][ii] = canoeArray[0][ii];
        }

        //Goes from the top then down
        for (int ii = 1; ii < n; ii++) {
            //Goes from the left then the right
            for (int qq = ii; qq < n; qq++) {
                int minimum = -1;


                //Goes through the values of the costs of all the paths left of the cell being looked at.
                for (int xx = ii; xx < qq; xx++) {
                    if (cheapestPaths[ii][xx] + canoeArray[ii][qq] < minimum || minimum == -1) {
                        minimum = cheapestPaths[ii][xx] +canoeArray[ii][qq];
                    }
                }
                //Goes through the values of the values on top of the current cell.
                for (int xx = 0; xx < ii; xx++) {
                    if (canoeArray[xx][qq] != -1) {
                        if (cheapestPaths[xx][qq] < minimum || minimum == -1) {
                            minimum = cheapestPaths[xx][qq];
                        }
                    }
                }
                cheapestPaths[ii][qq] = minimum;
            }
        }

        System.out.println("Dynamic Programming Algorithm");

        System.out.println("Minimum path: " + recover(cheapestPaths).toString() + ", Minimum cost: " + cheapestPaths[n - 1][n - 1]);
    }

    /**
     * Recovers the path from the array.
     *
     * @param cheapestPaths if the array of the paths.
     * @return the correct cheapest paths.
     */
    public static Set<Integer> recover(Integer[][] cheapestPaths) {
        Set<Integer>  cheapest = new TreeSet<>();

        cheapest.add(1); cheapest.add(n);
        int xx = n - 1;
        int yy = n - 1;
        while (xx > 0) {

            int current = cheapestPaths[xx][yy];
            int above   = cheapestPaths[xx-1][yy];

            if (current == above) {
                xx--;
            } else {
                int min = Integer.MAX_VALUE;
                int minIndex = Integer.MAX_VALUE;
                int ii;
                for (ii = xx; ii < yy; ii++) {

                    if (cheapestPaths[xx][ii] < min) {
                        min = cheapestPaths[xx][ii];
                        minIndex = ii;
                    }
                }
                cheapest.add(minIndex + 1);
                yy = minIndex;
            }
        }
        return cheapest;
    }


    /**
     * Divide. O(n^2);
     *
     * @param i is the starting point, which is 0.
     * @return an array.
     */
    public static int[] divide(int i) {
        int minValue = Integer.MAX_VALUE;
        int min = Integer.MAX_VALUE;
        int[] arr = new int[n + 1];
        //Base case. if the i is almost to the length, end it.
        if(i == n - 1) {
            arr[0] = 0;
            return arr;
        } else {
            for(int qq = i + 1; qq < n; qq++) {
                int[] tempArray = divide(qq);
                int value = tempArray[0] + canoeArray[i][qq];
                if (value < minValue) {
                    minValue = value;
                    min = qq;
                    System.arraycopy(tempArray, 0, arr, 0, arr.length);
                }
            }
        }
        arr[0] = minValue;
        arr[i + 1] = min + 1;
        return arr;
    }


    /**
     *
     * @param minPath array to transform.
     * @return a string
     */
    private static String buildDividePath(int[] minPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("[1");
        for(int i = 1; i < minPath.length; i++) {
            if(minPath[i] > 0) {
                sb.append(", ");
                sb.append(minPath[i]);
            }
        }
        sb.append("]");

        return sb.toString();
    }
    

}
