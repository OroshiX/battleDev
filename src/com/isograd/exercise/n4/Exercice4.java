package com.isograd.exercise.n4;

import com.isograd.exercise.y_model.Game;
import com.isograd.exercise.y_model.PogeType;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exercice4 {
    public void solve(int testNumber, Scanner sc, PrintWriter out) {
        int N = sc.nextInt();
        List<PogeType> adversary = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            adversary.add(new PogeType(sc.next()));
        }
        List<PogeType> mine = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            mine.add(new PogeType(sc.next()));
        }
        Game game= new Game(adversary,mine);

        do {
            if (game.isGameWonEntirely()) {
                out.print(game.mineToString());
                return;
            }
        } while (game.nextSetup());
        out.println(-1);
    }
}