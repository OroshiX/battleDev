package com.isograd.exercise.util;

import java.util.List;
import java.util.StringJoiner;

public class Util {

    public static String transformToCnf(List<Integer[]> listOfOrs, int nbOfVariables) {
        StringBuilder sb = new StringBuilder();
        // problem
        sb.append("p cnf ").append(nbOfVariables).append(' ').append(listOfOrs.size()).append("\n");
        // clauses
        for (Integer[] ors : listOfOrs) {
            StringJoiner stringJoiner = new StringJoiner(" ", "", " 0\n");
            for (Integer member : ors) {
                stringJoiner.add(member.toString());
            }
            sb.append(stringJoiner.toString());
        }
        return sb.toString();
    }
}
