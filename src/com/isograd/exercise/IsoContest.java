/* *****
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package com.isograd.exercise;

import java.util.*;

public class IsoContest {

    public static void main(String[] args) throws Exception {
        // write your code here
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();

        List<Etudiant> etudiantsCreneaux = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Etudiant nouveau = new Etudiant(i, in.nextInt(), in.nextInt());
            if (!tryToFitEtudiant(nouveau, etudiantsCreneaux)) {
                System.out.println("KO");
                return;
            }
            etudiantsCreneaux.add(nouveau);
        }
        if (etudiantsCreneaux.stream().anyMatch(IsoContest::decideImpossible)) {
            System.out.println("KO");
            return;
        }
        StringJoiner joiner = new StringJoiner("\n");
        etudiantsCreneaux.forEach(etudiant -> joiner.add(etudiant.decide == 2 ? "2" : "1"));
        System.out.println(joiner.toString());
    }

    private static boolean deciderEtudiant(Etudiant un, Etudiant deux) {
        if (un.equals(deux)) return false;
        if (decideImpossible(un) || decideImpossible(deux)) return false;
        if (un.decide == 1) {
            if (equals(un.premier, deux.premier) && equals(un.premier, deux.deuxieme)) {
                deux.decide = -1;
                return false;
            }
            if (equals(un.premier, deux.premier)) {
                deux.decide = 2;
            } else if (equals(un.premier, deux.deuxieme)) {
                deux.decide = 1;
            }
        } else if (un.decide == 2) {
            if (equals(un.deuxieme, deux.premier) && equals(un.deuxieme, deux.deuxieme)) {
                deux.decide = -1;
                return false;
            }
            if (equals(un.deuxieme, deux.premier)) {
                deux.decide = 2;
            } else if (equals(un.deuxieme, deux.deuxieme)) {
                deux.decide = 1;
            }
        } else {
            // Pas decide
            if (equals(un.premier, deux.premier) && equals(un.premier, deux.deuxieme)) {
                un.decide = 2;
            } else if (equals(un.deuxieme, deux.premier) && equals(un.deuxieme, deux.deuxieme)) {
                un.decide = 1;
            } else if (equals(deux.premier, un.premier) && equals(deux.premier, un.deuxieme)) {
                deux.decide = 2;
            } else if (equals(deux.deuxieme, un.premier) && equals(deux.deuxieme, un.deuxieme)) {
                deux.decide = 1;
            }
        }
        return true;
    }

    private static boolean tryToFitEtudiant(Etudiant nouveau, List<Etudiant> etudiantsCreneaux) {
        for (Etudiant etudiant : etudiantsCreneaux) {
            if (decideImpossible(etudiant)) return false;
            if (etudiant.equals(nouveau)) return false;
            if (!deciderEtudiant(etudiant, nouveau)) return false;
        }
        return true;
    }

    private static boolean equals(int i, int i2) {
        return Math.abs(i - i2) <= 60;
    }

    private static boolean decideImpossible(Etudiant etudiant) {
        return etudiant.decide == -1;
    }

    private static class Etudiant {
        int decide;

        public Etudiant(int numero, int premier, int deuxieme) {
            this.numero = numero;
            this.premier = premier;
            this.deuxieme = deuxieme;
        }

        int numero;
        int premier;
        int deuxieme;

        @Override
        public String toString() {
            return "{" + premier + ", " + deuxieme + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Etudiant etudiant = (Etudiant) o;

            if (Math.abs(premier - etudiant.premier) > 60) return false;
            if (Math.abs(deuxieme - etudiant.deuxieme) > 60) return false;
            if (Math.abs(deuxieme - etudiant.premier) > 60) return false;
            if (Math.abs(premier - etudiant.deuxieme) > 60) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

}
