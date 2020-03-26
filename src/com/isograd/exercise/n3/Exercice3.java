package com.isograd.exercise.n3;

import com.isograd.exercise.y_model.Creneau;
import com.isograd.exercise.y_model.TimeSchedule;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exercice3 {
    public void solve(int testNumber, Scanner in, PrintWriter out) {
        int N = in.nextInt();
        List<Creneau> dayList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int d = in.nextInt();
            String line= in.next();
            String[] tss = line.split("-");

            int h1 = Integer.parseInt(tss[0].split(":")[0]);
            int m1 = Integer.parseInt(tss[0].split(":")[1]);
            int h2 = Integer.parseInt(tss[1].split(":")[0]);
            int m2 = Integer.parseInt(tss[1].split(":")[1]);
            dayList.add(new Creneau(d, new TimeSchedule(h1,m1), new TimeSchedule(h2,m2)));
        }
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            if(dayList.stream().noneMatch(creneau -> creneau.day == finalI)) {
                out.print(Creneau.createFromStart(finalI,8,0));
                return;
            }
        }
        int startTime = TimeSchedule.first().toTime();
        int endTime = TimeSchedule.last().toTime();
        for (int i = 1; i <= 5 ; i++) {
            // iterate days
            for (int t = startTime; t <= endTime; t++) {
                int finalT = t;
                int finalI = i;
                if(dayList.stream().noneMatch(creneau -> creneau.isIncompatible(finalI,finalT))) {
                    out.print(Creneau.createFromStart(i,finalT));
                    return;
                }
            }
        }
        System.out.println("NO!");
    }

}