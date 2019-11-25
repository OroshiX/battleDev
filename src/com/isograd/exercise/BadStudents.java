package com.isograd.exercise;

import com.isograd.exercise.exo4.Choice;
import com.isograd.exercise.exo4.CreneauxIncompatibles;
import sat.Clause;
import sat.DPLLSolver;
import sat.DimacsParser;
import sat.Literal;

import java.util.*;
import java.io.PrintWriter;

public class BadStudents {
    public static int N;

    public void solve(int testNumber, Scanner in, PrintWriter out) {
        N = in.nextInt();
        List<Integer> horairesA = new ArrayList<>();
        List<Integer> horairesB = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            horairesA.add(in.nextInt());
            horairesB.add(in.nextInt());
        }

        List<CreneauxIncompatibles> creneauxIncompatibles = calculateIncompatibleCreneaux(horairesA, horairesB);
        List<Integer[]> listOfOrs = transformToListOfOrs(creneauxIncompatibles);
        String cnf = transformToCnf(listOfOrs);

        DimacsParser parser = new DimacsParser(false, cnf);
        ArrayList<Clause> conjuncts = parser.parseDimacs();
        DPLLSolver solver = new DPLLSolver(parser.getNumLiterals());
        ArrayList<Literal> model = solver.findModel(conjuncts);

        if (model == null) {
            System.out.println("KO");
        } else {
            model.sort(Comparator.comparingInt(Literal::get));
            for (int i = 0; i < N; i++) {
                Literal literal = model.get(i);
                System.out.println(literal.getTruth() ? 1 : 2);
            }
        }
    }

    private static boolean isIncompatible(int x, int y) {
        return Math.abs(x - y) < 60;
    }

    private static List<CreneauxIncompatibles> calculateIncompatibleCreneaux(List<Integer> horairesA, List<Integer> horairesB) {
        List<CreneauxIncompatibles> res = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                if (i == j) continue;
                // Compare A with A
                if (isIncompatible(horairesA.get(i), horairesA.get(j))) {
                    res.add(new CreneauxIncompatibles(Choice.A, i + 1, Choice.A, j + 1));
                }
                // Compare B with B
                if (isIncompatible(horairesB.get(i), horairesB.get(j))) {
                    res.add(new CreneauxIncompatibles(Choice.B, i + 1, Choice.B, j + 1));
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) continue;
                // Compare A with B
                if (isIncompatible(horairesA.get(i), horairesB.get(j))) {
                    res.add(new CreneauxIncompatibles(Choice.A, i + 1, Choice.B, j + 1));
                }
            }
        }
        return res;
    }

    private static List<Integer[]> transformToListOfOrs(List<CreneauxIncompatibles> creneauxIncompatibles) {
        List<Integer[]> orList = new ArrayList<>();
        // Regle generale
        for (int i = 1; i <= N; i++) {
            orList.add(new Integer[]{i, i + N});
            orList.add(new Integer[]{-i, -(i + N)});
        }
        // Avec creneaux
        for (CreneauxIncompatibles incompatibles : creneauxIncompatibles) {
            orList.add(new Integer[]{-(incompatibles.getIndex1() + (incompatibles.getChoice1() == Choice.A ? 0 : N)),
                    -(incompatibles.getIndex2() + (incompatibles.getChoice2() == Choice.A ? 0 : N))});
        }

        return orList;
    }

    private static String transformToCnf(List<Integer[]> listOfOrs) {
        StringBuilder builder = new StringBuilder();
        // problem
        builder.append("p cnf ").append(2 * N).append(' ').append(listOfOrs.size()).append("\n");
        // clauses
        for (Integer[] ors : listOfOrs) {
            StringJoiner sj = new StringJoiner(" ", "", " 0\n");
            for (Integer member : ors) {
                sj.add(member.toString());
            }
            builder.append(sj.toString());
        }

        return builder.toString();
    }

//
//    static class Creneau {
//        public Choice choice;
//        public int index;
//
//        public Creneau(Choice choice, int index) {
//            this.choice = choice;
//            this.index = index;
//        }
//
//        public int toMember() {
//            return index + (choice == Choice.A ? 0 : BadStudents.N);
//        }
//
//
//    }
//
//    public static Creneau toIndex(int member) {
//        Creneau creneau = new Creneau(Choice.A, member);
//        if (member > BadStudents.N) {
//            creneau.index -= BadStudents.N;
//            creneau.choice = Choice.B;
//        }
//        return creneau;
//    }
}