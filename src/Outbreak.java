import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        if (n == 0) {
            return new boolean[0];
        }

        int[] par = new int[n];
        Arrays.fill(par, -1);
        int[] ord = new int[n];
        int size = 0;

        par[0] = 0;
        ord[size++] = 0;

        // Root the tree at 0 and build topological order from root to leaves
        for (int h = 0; h < size; h++) {
            int v = ord[h];
            for (int u : nb[v]) {
                if (par[u] < 0) {
                    par[u] = v;
                    ord[size++] = u;
                }
            }
        }

        // Bottom-up DP from leaves to root
        long[] take = new long[n];
        long[] skip = new long[n];

        for (int h = size - 1; h >= 0; h--) {
            int v = ord[h];
            take[v] += cost[v];
            if (v != 0) {
                int p = par[v];
                take[p] += Math.min(take[v], skip[v]);
                skip[p] += take[v];
            }
        }

        // Top-down reconstruction of who is tested
        boolean[] tested = new boolean[n];
        tested[0] = take[0] <= skip[0];
        for (int h = 1; h < size; h++) {
            int v = ord[h];
            int p = par[v];
            tested[v] = !tested[p] || (take[v] <= skip[v]);
        }

        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     */
    public static int longestChain(int n, int[][] nb) {
        if (n <= 1) {
            return 0;
        }

        int[] par = new int[n];
        Arrays.fill(par, -1);
        int[] ord = new int[n];
        int size = 0;

        par[0] = 0;
        ord[size++] = 0;

        for (int h = 0; h < size; h++) {
            int v = ord[h];
            for (int u : nb[v]) {
                if (par[u] < 0) {
                    par[u] = v;
                    ord[size++] = u;
                }
            }
        }

        int[] top1 = new int[n];
        int[] top2 = new int[n];
        int chain = 0;

        for (int h = size - 1; h >= 0; h--) {
            int v = ord[h];
            chain = Math.max(chain, top1[v] + top2[v]);
            if (v != 0) {
                int p = par[v];
                int branch = top1[v] + 1;
                if (branch > top1[p]) {
                    top2[p] = top1[p];
                    top1[p] = branch;
                } else if (branch > top2[p]) {
                    top2[p] = branch;
                }
            }
        }

        return chain;
    }
}