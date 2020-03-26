package com.isograd.exercise.n2;

import java.io.PrintWriter;
import java.util.Scanner;

public class Exercice2 {
    public void solve(int testNumber, Scanner sc, PrintWriter out) {
        int N = sc.nextInt();
        int maxLength = 0;
        int lastValue = 0;int currentLength = 0;
        for (int i = 0; i < N; i++) {
            int current = sc.nextInt();
            if(current == lastValue){
                currentLength++;
                if(currentLength > maxLength) {
                    maxLength= currentLength;
                }
            } else {
                currentLength = 0;
            }
            lastValue=current;
        }
        out.print(maxLength);
    }
}
