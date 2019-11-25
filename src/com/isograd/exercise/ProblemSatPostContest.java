package com.isograd.exercise;

import sat.Clause;
import sat.DPLLSolver;
import sat.DimacsParser;
import sat.Literal;

import java.util.*;

public class ProblemSatPostContest {
//    public static int N;
//
//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        N = in.nextInt();
//        List<Integer> horairesA = new ArrayList<>();
//        List<Integer> horairesB = new ArrayList<>();
//        for (int i = 0; i < N; i++) {
//            horairesA.add(in.nextInt());
//            horairesB.add(in.nextInt());
//        }
//
//        List<CreneauxIncompatibles> creneauxIncompatibles = calculateIncompatibleCreneaux(horairesA, horairesB);
//        List<Integer[]> listOfOrs = transformToListOfOrs(creneauxIncompatibles);
//        String cnf = transformToCnf(listOfOrs);
//
//        DimacsParser parser = new DimacsParser(false, cnf);
//        ArrayList<Clause> conjuncts = parser.parseDimacs();
//        DPLLSolver solver = new DPLLSolver(parser.getNumLiterals());
//        ArrayList<Literal> model = solver.findModel(conjuncts);
//
//        if (model == null) {
//            System.out.println("KO");
//        } else {
//            model.sort(Comparator.comparingInt(Literal::get));
//            for (int i = 0; i < N; i++) {
//                Literal literal = model.get(i);
//                System.out.println(literal.getTruth() ? 1 : 2);
//            }
//        }
//    }
//
//    private static boolean isIncompatible(int x, int y) {
//        return Math.abs(x - y) < 60;
//    }
//
//    private static List<CreneauxIncompatibles> calculateIncompatibleCreneaux(List<Integer> horairesA, List<Integer> horairesB) {
//        List<CreneauxIncompatibles> res = new ArrayList<>();
//        for (int i = 0; i < N; i++) {
//            for (int j = i; j < N; j++) {
//                if (i == j) continue;
//                // Compare A with A
//                if (isIncompatible(horairesA.get(i), horairesA.get(j))) {
//                    res.add(new CreneauxIncompatibles(Choice.A, i + 1, Choice.A, j + 1));
//                }
//                // Compare B with B
//                if (isIncompatible(horairesB.get(i), horairesB.get(j))) {
//                    res.add(new CreneauxIncompatibles(Choice.B, i + 1, Choice.B, j + 1));
//                }
//            }
//        }
//
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                if (i == j) continue;
//                // Compare A with B
//                if (isIncompatible(horairesA.get(i), horairesB.get(j))) {
//                    res.add(new CreneauxIncompatibles(Choice.A, i + 1, Choice.B, j + 1));
//                }
//            }
//        }
//        return res;
//    }
//
//    private static List<Integer[]> transformToListOfOrs(List<CreneauxIncompatibles> creneauxIncompatibles) {
//        List<Integer[]> orList = new ArrayList<>();
//        // Regle generale
//        for (int i = 1; i <= N; i++) {
//            orList.add(new Integer[]{i, i + N});
//            orList.add(new Integer[]{-i, -(i + N)});
//        }
//        // Avec creneaux
//        for (CreneauxIncompatibles incompatibles : creneauxIncompatibles) {
//            orList.add(new Integer[]{-(incompatibles.getIndex1() + (incompatibles.getChoice1() == Choice.A ? 0 : N)),
//                    -(incompatibles.getIndex2() + (incompatibles.getChoice2() == Choice.A ? 0 : N))});
//        }
//
//        return orList;
//    }
//
//    private static String transformToCnf(List<Integer[]> listOfOrs) {
//        StringBuilder builder = new StringBuilder();
//        // problem
//        builder.append("p cnf ").append(2 * N).append(' ').append(listOfOrs.size()).append("\n");
//        // clauses
//        for (Integer[] ors : listOfOrs) {
//            StringJoiner sj = new StringJoiner(" ", "", " 0\n");
//            for (Integer member : ors) {
//                sj.add(member.toString());
//            }
//            builder.append(sj.toString());
//        }
//
//        return builder.toString();
//    }
//}
//
//enum Choice {
//    A, B
//}
//
//class CreneauxIncompatibles {
//    public CreneauxIncompatibles(Choice choice1, int index1, Choice choice2, int index2) {
//        this.choice1 = choice1;
//        this.choice2 = choice2;
//        this.index1 = index1;
//        this.index2 = index2;
//    }
//
//    private final Choice choice1, choice2;
//    private final int index1, index2;
//
//    public Choice getChoice1() {
//        return choice1;
//    }
//
//    public int getIndex1() {
//        return index1;
//    }
//
//    public Choice getChoice2() {
//        return choice2;
//    }
//
//    public int getIndex2() {
//        return index2;
//    }
//
//    @Override
//    public String toString() {
//        return "CreneauxIncompatibles{" + choice1 + index1 + ", " + choice2 + index2 + '}';
//    }
//}
//
//class Creneau {
//    public Choice choice;
//    public int index;
//
//    public Creneau(Choice choice, int index) {
//        this.choice = choice;
//        this.index = index;
//    }
//
//    public int toMember() {
//        return index + (choice == Choice.A ? 0 : ProblemSatPostContest.N);
//    }
//
//    public static Creneau toIndex(int member) {
//        Creneau creneau = new Creneau(Choice.A, member);
//        if (member > ProblemSatPostContest.N) {
//            creneau.index -= ProblemSatPostContest.N;
//            creneau.choice = Choice.B;
//        }
//        return creneau;
//    }
}