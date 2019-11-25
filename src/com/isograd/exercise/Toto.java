/* *****
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package com.isograd.exercise;

import java.util.*;

public class Toto {

    public static void main(String[] args) throws Exception {
        // write your code here
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        List<Pair> etudiantsCreneaux = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            etudiantsCreneaux.add(new Pair(sc.nextInt(), sc.nextInt()));
        }
        System.err.println(Arrays.toString(etudiantsCreneaux.toArray()));

        List<List<Integer>> allCombinaisons = generateAllCreneaux(etudiantsCreneaux, N);
        int max = 1;
        for (List<Integer> aCombinaison : allCombinaisons) {
            if (aCombinaison.size() <= max) continue;
            if (isPossible(aCombinaison)) {
                max = aCombinaison.size();
            }
        }
        System.out.println(max);
    }

    private static List<List<Integer>> generateAllCreneaux(List<Pair> etudiantsCreneaux, int N) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1; i <= Math.pow(3, N); i++) {
            String taken = asBase3(i);
            System.err.println(taken);

            List<Integer> combinaison = new ArrayList<>();
            for (int j = 0; j < taken.length(); j++) {
                char index = taken.charAt(j);
                switch (index) {
                    case '0':
                        continue;
                    case '1':
                        combinaison.add(etudiantsCreneaux.get(j).premier);
                        break;
                    case '2':
                        combinaison.add(etudiantsCreneaux.get(j).deuxieme);
                        break;
                    default:
                        System.err.println("PROBLEME !!");
                }
            }
            res.add(combinaison);
        }
        return res;
    }

    private static String asBase3(int num) {
        long ret = 0, factor = 1;
        while (num > 0) {
            ret += num % 3 * factor;
            num /= 3;
            factor *= 10;
        }
        return String.valueOf(ret);
    }

    private static boolean isPossible(List<Integer> creneaux) {
        Collections.sort(creneaux);
        int last = -1;
        for (Integer aCreneaux : creneaux) {
            if (aCreneaux <= last) return false;
            last = aCreneaux + 60;
        }
        return true;
    }

    private static class Pair {
        public Pair(int premier, int deuxieme) {
            this.premier = premier;
            this.deuxieme = deuxieme;
        }

        int premier;
        int deuxieme;

        @Override
        public String toString() {
            return "{" + premier + ", " + deuxieme + '}';
        }
    }

}
