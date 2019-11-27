package com.isograd.exercise.n1;

import java.util.Scanner;
import java.io.PrintWriter;

public class Exercice1 {
    public void solve(int testNumber, Scanner in, PrintWriter out) {
        int N = in.nextInt();
        String minSt = null;
        int minI = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            String curr = in.next();
            int currI = in.nextInt();
            if (currI < minI) {
                minI = currI;
                minSt = curr;
            }
        }
        out.println(minSt);
    }
}
