package com.isograd.exercise.z_example;

import com.isograd.exercise.util.Util;
import sat.Clause;
import sat.DPLLSolver;
import sat.DimacsParser;
import sat.Literal;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class BadStudents {
    private static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        BadStudents solver = new BadStudents();
        solver.solve(1, in, out);
        out.close();
    }

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
        String cnf = Util.transformToCnf(listOfOrs, 2 * N);

        DimacsParser parser = new DimacsParser(false, cnf);
        ArrayList<Clause> conjuncts = parser.parseDimacs();
        DPLLSolver solver = new DPLLSolver(parser.getNumLiterals());
        ArrayList<Literal> model = solver.findModel(conjuncts);

        if (model == null) {
            out.println("KO");
        } else {
            model.sort(Comparator.comparingInt(Literal::get));
            for (int i = 0; i < N; i++) {
                Literal literal = model.get(i);
                out.println(literal.getTruth() ? 1 : 2);
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
}