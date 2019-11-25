package com.isograd.exercise.exo4;

import com.isograd.exercise.BadStudents;

public class CreneauxIncompatibles {
    public CreneauxIncompatibles(Choice choice1, int index1, Choice choice2, int index2) {
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.index1 = index1;
        this.index2 = index2;
    }

    private final Choice choice1, choice2;
    private final int index1, index2;

    public Choice getChoice1() {
        return choice1;
    }

    public int getIndex1() {
        return index1;
    }

    public Choice getChoice2() {
        return choice2;
    }

    public int getIndex2() {
        return index2;
    }

    @Override
    public String toString() {
        return "CreneauxIncompatibles{" + choice1 + index1 + ", " + choice2 + index2 + '}';
    }
}
