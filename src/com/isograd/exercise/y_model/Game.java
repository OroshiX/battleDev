package com.isograd.exercise.y_model;

import java.util.*;

public class Game {
    public List<PogeType> adversaryFull, mineFull;
    public List<PogeType> adversary;
    public List<PogeType> mine;

    public Map<Integer, PogeType> association;
    public List<Integer> order, orderMax;

    public Game(List<PogeType> adversary, List<PogeType> mine) {
        this.adversary = adversary;
        adversaryFull = new ArrayList<>(adversary);
        this.mine = mine;
        mineFull = new ArrayList<>(mine);
        association = new HashMap<>();
        order = new ArrayList<>(mine.size());
        for (int i = 0; i < mine.size(); i++) {
            association.put(i, mine.get(i));
            order.add(i);
        }
        orderMax = new ArrayList<>(order);
        orderMax.sort(Comparator.reverseOrder());
    }

    private void reset() {
        mine.clear();
        order.forEach(integer -> mine.add(association.get(integer)));
        adversary = new ArrayList<>(adversaryFull);
    }

    public boolean nextSetup() {
        boolean maxReached;
        do {
            nextOrder();
            maxReached = maxOrderReached();
        } while (!isValidOrder() && !maxReached);
        reset();
        return !maxReached;
    }

    private void nextOrder() {
        int i = order.size();
        boolean success = false;
        do {
            i--;
            success = incrementAtI(i);
        } while (!success && i >= 0);
        if (i >= 0) {
            for (int j = i + 1; j < order.size(); j++) {
                order.set(j, 0);
            }
        }
    }

    private boolean incrementAtI(int i) {
        if (order.get(i) < order.size() - 1) {
            order.set(i, order.get(i) + 1);
            return true;
        }
        return false;
    }

    private boolean isValidOrder() {
        // are all numbers different ?
        for (int i = 0; i < order.size(); i++) {
            for (int j = 0; j < order.size(); j++) {
                if (i != j && order.get(i).equals(order.get(j))) return false;
            }
        }
        return true;
    }

    private boolean maxOrderReached() {
        return orderMax.equals(order);
    }

    private boolean isFinished() {
        return adversary.isEmpty() || mine.isEmpty();
    }

    private boolean isWon() {
        return adversary.isEmpty() && !mine.isEmpty();
    }

    public boolean isLost() {
        return !adversary.isEmpty() && mine.isEmpty();
    }

    private boolean nextTurn() {
        if (isFinished()) throw new IllegalStateException();
        int gagne = mine.get(0).compareTo(adversary.get(0));
        if (gagne > 0) {
            adversary.remove(0);
        } else if (gagne < 0) {
            mine.remove(0);
        } else {
            adversary.remove(0);
            mine.remove(0);
        }
        return !isFinished();
    }

    public boolean isGameWonEntirely() {
        while (true) {
            if (!nextTurn()) break;
        }
        return isWon();
    }

    public String mineToString() {
        reset();
        StringJoiner joiner = new StringJoiner(" ");
        mine.forEach(pogeType -> joiner.add(pogeType.type));
        return joiner.toString();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" "), joiner1 = new StringJoiner(" ");
        mine.forEach(pogeType -> joiner.add(pogeType.type));
        adversary.forEach(pogeType -> joiner1.add(pogeType.type));
        return "[me] " + joiner.toString() + "    VS    [adv] " + joiner1.toString();
    }
}
