import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
        brute(canoeArray);
        sc.close();
        sc2.close();
    }

    public static void brute(int[][] arr) {
        // Number of posts 'n'
        int numPosts = arr.length;

        // A map containing paths as key, costs as value.
        // TreeMap uses a Red-Black tree, log(n) insertion
        TreeMap<Integer, ArrayList<Integer>> pathCosts = new TreeMap<>();

        // Retrieve all possible paths. O(n^2) runtime
        Set<ArrayList<Integer>> possibleSolutions = getPossiblePaths(numPosts);
        // System.out.println(possibleSolutions);

        // Fill map with all possible solutions and their cost.
        for (ArrayList<Integer> row : possibleSolutions) {
            int cost = 0;
            Integer current = null;
            // Look at previous post and current post to know where to go in cost matrix
            for (Integer next : row) {
                if (current != null) {
                    cost += arr[current - 1][next - 1];
                }
                current = next;
            }
            pathCosts.put(cost, row);
        }
        // System.out.println(pathCosts);
        System.out.println("Best path via Brute Force: " + pathCosts.firstEntry().getValue() + " with cost of " + pathCosts.firstKey());

    }
    public static Set<ArrayList<Integer>> getPossiblePaths(int numPosts) {
        Set<ArrayList<Integer>> pathList = new HashSet<>();

        for (int p = numPosts; p > 2; p--) {
            for (int i = 2; i < numPosts; i++) {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(1); // Add first post
                for (int j = i; j < p; j++) {
                    arr.add(j); // Add posts in between
                }
                arr.add(numPosts); // Add last post
                pathList.add(arr);
            }
        }

        return pathList;
    }
}
