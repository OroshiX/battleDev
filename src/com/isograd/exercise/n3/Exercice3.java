package com.isograd.exercise.n3;

import java.util.*;
import java.io.PrintWriter;

public class Exercice3 {
    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int M = sc.nextInt();
        List<Integer> resCable = new ArrayList<>(M);
        List<StartEnd> startEnds = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            startEnds.add(new StartEnd(sc.nextInt(), sc.nextInt()));
        }
        int min = startEnds.stream().min(Comparator.comparing(o -> o.start)).get().start;
        int max = startEnds.stream().max(Comparator.comparing(o -> o.end)).get().end;
        int[] usageCables = new int[max - min];

        for (int i = 0; i < M; i++) {
            StartEnd startEnd = startEnds.get(i);
            int maxNumCable = 0;
            for (int j = startEnd.start - min; j < startEnd.end - min; j++) {
                usageCables[j]++;
                if (usageCables[j] > maxNumCable) {
                    maxNumCable = usageCables[j];
                }
                if (usageCables[j] > N) {
                    System.out.println("pas possible");
                    return;
                }
//                resCable.add(usageCables[j]);
            }
            resCable.add(maxNumCable);
        }
        StringJoiner sj = new StringJoiner(" ");
        resCable.forEach(integer -> sj.add(integer.toString()));
        System.out.println(sj.toString());
    }


}

class StartEnd {
    public StartEnd(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Integer start, end;

}