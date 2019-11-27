package com.isograd.exercise.n2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;

public class Exercice2 {
    public void solve(int testNumber, Scanner sc, PrintWriter out) {

        List<Integer> planches = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            planches.add(sc.nextInt());
        }
        Integer min = planches.stream().min(Integer::compareTo).get();
        final int[] sum = {0};
        planches.forEach(integer -> {
            sum[0] += integer -min;
        });

        System.out.println(sum[0]);
    }
}
