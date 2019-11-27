package com.isograd.exercise.n4;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Exercice4 {
    public static void main(String[] arv) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int C = sc.nextInt();

        List<Pierre> pierres = new ArrayList<>();
        List<Poudre> poudres = new ArrayList<>();
        // Pierres
        for (int i = 0; i < N; i++) {
            int or = sc.nextInt();
            int poids = sc.nextInt();
            pierres.add(new Pierre(or, poids));
        }
        // Poudres
        for (int i = 0; i < M; i++) {
            int orParGramme = sc.nextInt();
            int qteDispo = sc.nextInt();
            poudres.add(new Poudre(orParGramme, qteDispo));
        }
        int sumPoids = 0;
        int sumOr = 0;
        Take take;
        while ((take = sumGetBest(pierres, poudres, C, sumPoids)) != null && sumPoids < C) {
            sumPoids += take.take;
            sumOr += take.or;
        }
        System.out.println(sumOr);
    }

    private static Take moreThanPoudre(List<Pierre> pierres, float prixPoudre, int currentPoids, int maxPoids) {
        List<Pierre> pierreMoreStream = pierres.stream().filter(pierre -> pierre.getPrixAuGramme() > prixPoudre).collect(Collectors.toCollection(ArrayList::new));
        if (pierreMoreStream.size() == 0) {
            return null;
        }
        Pierre pierreMax = pierreMoreStream.stream().max(Comparator.comparing(Pierre::getPrixAuGramme)).get();
        pierres.remove(pierreMax);
        if (currentPoids + pierreMax.poids <= maxPoids) {
            return new Take(pierreMax.poids, pierreMax.or);
        } else {
            return moreThanPoudre(pierres, prixPoudre, currentPoids, maxPoids);
        }
    }

    private static Take sumGetBest(List<Pierre> pierres, List<Poudre> poudres, int maxPoids, int currentPoids) {

        Optional<Poudre> poudreOptional = poudres.stream().max(Comparator.comparing(poudre -> poudre.orParGramme));
        Poudre poudreBest = null;
        float prixPoudre = 0;
        if (poudreOptional.isPresent()) {
            poudreBest = poudreOptional.get();
            prixPoudre = poudreBest.orParGramme;
        }
        Take takePierre = moreThanPoudre(pierres, prixPoudre, currentPoids, maxPoids);

        if (takePierre != null) return takePierre;

        if (poudreBest == null) return null;

        if (currentPoids + poudreBest.qteDispo <= maxPoids) {
            poudres.remove(poudreBest);
            return new Take(poudreBest.qteDispo, poudreBest.qteDispo * poudreBest.orParGramme);
        } else {
            int take = maxPoids - currentPoids;
            return new Take(take, poudreBest.orParGramme * take);
        }

    }


}

class Take {
    public Take(int take, int or) {
        this.take = take;
        this.or = or;
    }

    public int take, or;
}

class Pierre {
    Pierre(int or, int poids) {
        this.or = or;
        this.poids = poids;
    }

    public int or, poids;

    public int getPrixAuGramme() {
        return or / poids;
    }

    @Override
    public String toString() {
        return "" + or + "," + poids + "g => " + getPrixAuGramme() + " or/g";
    }
}

class Poudre {
    int orParGramme, qteDispo;

    public Poudre(int orParGramme, int qteDispo) {
        this.orParGramme = orParGramme;
        this.qteDispo = qteDispo;
    }

    @Override
    public String toString() {
        return "" + qteDispo + "g, " + orParGramme + " Or/g";
    }
}