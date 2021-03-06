package sat;

import java.util.ArrayList;

public class DPLLSolver {
    private int modelSize;
    private int numberOfLiterals;
    private int[] assignCount;

    private ArrayList<Literal> lastGuess;
    private ArrayList<Literal> model;
    private ArrayList<Literal> workingSet;
    private ArrayList<Clause> conjuncts;

    private Clause lastDeductionClause;

    public DPLLSolver(int _numberOfLiterals) {
        modelSize = 0;
        numberOfLiterals = _numberOfLiterals;
        assignCount = new int[numberOfLiterals];
        lastGuess = new ArrayList<Literal>();
        conjuncts = new ArrayList<Clause>();

        /* Initialize the model */
        model = new ArrayList<Literal>();

        /* Construct a working set of possible literals for the model */
        workingSet = new ArrayList<Literal>();

        for (int i = 1; i <= numberOfLiterals; i++) {
            addToWorkingSet(new Literal(i, true));
        }

        /* No deductions yet */
        lastDeductionClause = null;
    }

    /* Sorted insert for adding literals to working set */
    private void addToWorkingSet(Literal guess) {
        for (int i = 0; i < workingSet.size(); i++) {
            if (guess.get() < workingSet.get(i).get()) {
                workingSet.add(i, new Literal(guess.get(), true));
                return;
            }
        }
        workingSet.add(new Literal(guess.get(), true));
    }

    /* Removal of literal 'unit' from working set */
    private void removeFromWorkingSet(Literal unit) {
        for (int i = 0; i < workingSet.size(); i++) {
            if (workingSet.get(i).get() == unit.get()) {
                workingSet.remove(i);
                return;
            }
        }
    }

    /* Checks if the current assignment to the disjuncts yields 'true' */
    private boolean checkFormula(ArrayList<Literal> disjuncts) {
        boolean clauseTruth = false;

        /* Compute the disjunction of all disjuncts */
        for (Literal lit : disjuncts) {
            if (assignCount[lit.get() - 1] > 0) {
                boolean modelVal = truthValInModel(model, lit.get());
                boolean clauseVal = lit.getTruth();

                if (modelVal == clauseVal) {
                    clauseTruth = clauseTruth || true;
                } else {
                    clauseTruth = clauseTruth || false;
                }
            }
        }

        return clauseTruth;
    }

    /* Finds the truth value of Literal with value 'lit' in the model */
    private boolean truthValInModel(ArrayList<Literal> model, int lit) {
        for (Literal l : model) {
            if (l.get() == lit) {
                return l.getTruth();
            }
        }

        /* CODE SHOULD NOT REACH THIS POINT */
        assert (false);
        return false;
    }

    /* Unit Propagation. Returns:
            1: Unit clause exists/ Unit propagation performed
            0: No unit clause exists.
           -1: Conflict 		*/
    private int deduce() {
        int clauseIndex = 0;

        /* Find a unit clause */
        for (Clause c : conjuncts) {
            ArrayList<Literal> disjuncts = c.get();

            /* Single literal unit clause */
            if (disjuncts.size() == 1) {
                Literal unit = disjuncts.get(0);
                if (assignCount[unit.get() - 1] == 0) {
                    /* Add the literal assignment to the model */
                    model.add(new Literal(unit.get(), unit.getTruth()));
                    modelSize++;

                    /* Remove literal from working set */
                    removeFromWorkingSet(unit);

                    /* Increment assign count for this literal */
                    assignCount[unit.get() - 1]++;

                    /* CDCL: Set the last deduction clause */
                    lastDeductionClause = c;

                    return 1;
                }
                /* Literal already assigned a value */
                else {
                    /* Check for contradiction */
                    if (unit.getTruth() != truthValInModel(model, unit.get())) {
                        /* CDCL: Add new clause for performance */
                        cdcl(clauseIndex);

                        return -1;
                    }
                }
            }
            /* Check for only one unassigned literal */
            else {
                /* Find how many literals are unassigned */
                int unassignedCount = 0;
                Literal unit = null;

                for (Literal lit : disjuncts) {
                    if (assignCount[lit.get() - 1] == 0) {
                        unassignedCount++;
                        unit = lit;
                    }
                }

                /* Check if it is unit clause */
                if (unassignedCount == 1) {
                    if (!checkFormula(disjuncts)) {
                        /* Add the literal assignment to the model */
                        model.add(new Literal(unit.get(), unit.getTruth()));
                        modelSize++;

                        /* Remove literal from working set */
                        removeFromWorkingSet(unit);

                        /* Increment assign count for this literal */
                        assignCount[unit.get() - 1]++;

                        /* CDCL: Set the last deduction clause */
                        lastDeductionClause = c;

                        return 1;
                    }
                }
            }

            clauseIndex++;
        }

        /* No unit clause found */
        return 0;
    }

    /* Branching: choose the next value from the working set */
    private void guess() {
        /* Check unsatisfiablity */
        if (workingSet.size() == 0) {
            backTrack();
            return;
        }

        /* Get next guess value from the working set */
        Literal pop = workingSet.remove(0);

        /* Add to the model */
        model.add(new Literal(pop.get(), pop.getTruth()));
        modelSize++;

        /* Increment assign count for this literal */
        assignCount[pop.get() - 1]++;

        /* Mark this guess as the most recent one */
        lastGuess.add(pop);
    }

    /* Add new clause constructed from the conflicting condition */
    private void cdcl(int clauseIndex) {
        /* Initialize new clause */
        Clause learn = new Clause();
        ArrayList<Literal> conflictClause = conjuncts.get(clauseIndex).get();

        /* Construct the new clause from the conflicting condition */
        Literal conflictingLiteral = model.get(modelSize - 1);

        for (Literal lit : conflictClause) {
            if (!lit.equals(conflictingLiteral) && !learn.get().contains(lit)) {
                learn.addDisjunct(lit);
            }
        }

        for (Literal lit : lastDeductionClause.get()) {
            if (!lit.equals(conflictingLiteral) && !learn.get().contains(lit)) {
                learn.addDisjunct(lit);
            }
        }

        /* Add the new clause to the set of conjuncts */
        conjuncts.add(learn);
    }

    /* Reconstruct the model and workingSet to the last checkpoint at the most recent guess */
    private void backTrack() {
        /* Restore the model and workingSet to the state at the most recent guess */
        Literal guess = null;

        while (guess == null && modelSize != 0) {
            guess = model.remove(modelSize - 1);

            /* The most recent guess */
            if (guess.get() == lastGuess.get(lastGuess.size() - 1).get()) {
                lastGuess.remove(lastGuess.size() - 1);
                assignCount[guess.get() - 1] = 0;

                /* If 'true' has already been selected we now choose 'false'*/
                if (guess.getTruth()) {
                    /* Guess the negated literal now */
                    model.add(new Literal(guess.get(), false));
                    modelSize++;

                    /* Increment assign count for this literal */
                    assignCount[guess.get() - 1]++;

                    /* Mark this guess as the most recent one */
                    lastGuess.add(new Literal(guess.get(), false));
                }
                /* Both values chosen. Restore working set for backtrack */
                else {
                    addToWorkingSet(guess);
                    assignCount[guess.get() - 1] = 0;
                    guess = null;
                }
            }
            /* Literals deduced using unit propagation */
            else {
                addToWorkingSet(guess);
                assignCount[guess.get() - 1] = 0;
                guess = null;
            }

            modelSize--;
        }
    }

    /* Computes the disjunction of all clauses that can be resolved to a boolean value */
    private boolean conflict() {
        int clauseIndex = 0;

        for (Clause c : conjuncts) {
            /* Find how many literals are unassigned */
            int unassignedCount = 0;

            for (Literal lit : c.get()) {
                if (assignCount[lit.get() - 1] == 0) {
                    unassignedCount++;
                }
            }

            /* Check if it is unit clause */
            if (unassignedCount == 0) {
                if (!checkFormula(c.get())) {
                    /* CDCL: Add new clause for performance */
                    cdcl(clauseIndex);

                    return true;
                }
            }

            clauseIndex++;
        }

        return false;
    }

    /* This method performs the DPLL algorithm on the conjuncts in order to find a model */
    public ArrayList<Literal> findModel(ArrayList<Clause> _conjuncts) {
        /* Store the clauses/conjuncts into the object */
        conjuncts = _conjuncts;

        /* Work on the model recursively until solution found */
        while (modelSize != numberOfLiterals) {
            /* DEBUGGING */

            // for(Literal c : model)
            // 	System.err.print(c.toString() + " ");
            // System.err.println();

            // for(Literal c : lastGuess)
            // 	System.err.print(c.toString() + " ");
            // System.err.println();

            // for(Literal c : workingSet)
            // 	System.err.print(c.toString() + " ");
            // System.err.println();

            /* Attempt deduction */
            int deduction = deduce();

            /* Unit propagation not possible. Must guess a literal value */
            if (deduction == 0) {
                /* Perform Pure Literal Assignment */
                guess();
            }
            /* Checking conflicts for other conjuncts */
            else if (deduction == 1) {
                /* Conflict caught*/
                if (conflict()) {
                    /* If no guess was made */
                    if (lastGuess.size() == 0) {
                        System.err.println("NO GUESSES");
                        return null;
                    }

                    /* Restore state to most recent guess */
                    backTrack();

                    /* Exhausted the model */
                    if (modelSize == 0) {
                        System.err.println("MODEL EXHAUSTED");
                        return null;
                    }
                }
            }
            /* Conflict found. Must backtrack to last guess */
            else if (deduction == -1) {
                /* If no guess was made */
                if (lastGuess.size() == 0) {
                    System.err.println("NO GUESSES");
                    return null;
                }

                /* Restore state to most recent guess */
                backTrack();
            }
        }

        return model;
    }
}