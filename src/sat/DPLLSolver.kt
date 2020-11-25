package sat

import java.util.*

class DPLLSolver(private val numberOfLiterals: Int) {
    private var modelSize = 0
    private val assignCount: IntArray = IntArray(numberOfLiterals)
    private val lastGuess: ArrayList<Literal> = ArrayList()
    private val model: ArrayList<Literal>
    private val workingSet: ArrayList<Literal>
    private var conjuncts: ArrayList<Clause>
    private var lastDeductionClause: Clause?

    /* Sorted insert for adding literals to working set */
    private fun addToWorkingSet(guess: Literal?) {
        for (i in workingSet.indices) {
            if (guess!!.get() < workingSet[i].get()) {
                workingSet.add(i, Literal(guess.get(), true))
                return
            }
        }
        workingSet.add(Literal(guess!!.get(), true))
    }

    /* Removal of literal 'unit' from working set */
    private fun removeFromWorkingSet(unit: Literal?) {
        for (i in workingSet.indices) {
            if (workingSet[i].get() == unit!!.get()) {
                workingSet.removeAt(i)
                return
            }
        }
    }

    /* Checks if the current assignment to the disjuncts yields 'true' */
    private fun checkFormula(disjuncts: ArrayList<Literal>): Boolean {
        var clauseTruth = false

        /* Compute the disjunction of all disjuncts */
        for (lit in disjuncts) {
            if (assignCount[lit.get() - 1] > 0) {
                val modelVal = truthValInModel(model, lit.get())
                val clauseVal = lit.truth
                clauseTruth = if (modelVal == clauseVal) {
                    true
                } else {
                    clauseTruth
                }
            }
        }
        return clauseTruth
    }

    /* Finds the truth value of Literal with value 'lit' in the model */
    private fun truthValInModel(model: ArrayList<Literal>, lit: Int): Boolean {
        for (l in model) {
            if (l.get() == lit) {
                return l.truth
            }
        }
        assert(false)
        return false
    }

    /* Unit Propagation. Returns:
            1: Unit clause exists/ Unit propagation performed
            0: No unit clause exists.
           -1: Conflict 		*/
    private fun deduce(): Int {

        /* Find a unit clause */
        for ((clauseIndex, c) in conjuncts.withIndex()) {
            val disjuncts = c.get()

            /* Single literal unit clause */
            if (disjuncts.size == 1) {
                val unit = disjuncts[0]
                if (assignCount[unit.get() - 1] == 0) {
                    /* Add the literal assignment to the model */
                    model.add(Literal(unit.get(), unit.truth))
                    modelSize++

                    /* Remove literal from working set */
                    removeFromWorkingSet(unit)

                    /* Increment assign count for this literal */
                    assignCount[unit.get() - 1]++

                    /* CDCL: Set the last deduction clause */
                    lastDeductionClause = c
                    return 1
                } else {
                    /* Check for contradiction */
                    if (unit.truth != truthValInModel(model, unit.get())) {
                        /* CDCL: Add new clause for performance */
                        cdcl(clauseIndex)
                        return -1
                    }
                }
            } else {
                /* Find how many literals are unassigned */
                var unassignedCount = 0
                var unit: Literal? = null
                for (lit in disjuncts) {
                    if (assignCount[lit.get() - 1] == 0) {
                        unassignedCount++
                        unit = lit
                    }
                }

                /* Check if it is unit clause */
                if (unassignedCount == 1) {
                    if (!checkFormula(disjuncts)) {
                        /* Add the literal assignment to the model */
                        model.add(Literal(unit!!.get(), unit.truth))
                        modelSize++

                        /* Remove literal from working set */
                        removeFromWorkingSet(unit)

                        /* Increment assign count for this literal */
                        assignCount[unit.get() - 1]++

                        /* CDCL: Set the last deduction clause */
                        lastDeductionClause = c
                        return 1
                    }
                }
            }
        }

        /* No unit clause found */return 0
    }

    /* Branching: choose the next value from the working set */
    private fun guess() {
        /* Check unsatisfiablity */
        if (workingSet.size == 0) {
            backTrack()
            return
        }

        /* Get next guess value from the working set */
        val pop = workingSet.removeAt(0)

        /* Add to the model */
        model.add(Literal(pop.get(), pop.truth))
        modelSize++

        /* Increment assign count for this literal */
        assignCount[pop.get() - 1]++

        /* Mark this guess as the most recent one */
        lastGuess.add(pop)
    }

    /* Add new clause constructed from the conflicting condition */
    private fun cdcl(clauseIndex: Int) {
        /* Initialize new clause */
        val learn = Clause()
        val conflictClause = conjuncts[clauseIndex].get()

        /* Construct the new clause from the conflicting condition */
        val conflictingLiteral = model[modelSize - 1]
        for (lit in conflictClause) {
            if (lit != conflictingLiteral && !learn.get().contains(lit)) {
                learn.addDisjunct(lit)
            }
        }
        for (lit in lastDeductionClause!!.get()) {
            if (lit != conflictingLiteral && !learn.get().contains(lit)) {
                learn.addDisjunct(lit)
            }
        }

        /* Add the new clause to the set of conjuncts */conjuncts.add(learn)
    }

    /* Reconstruct the model and workingSet to the last checkpoint at the most recent guess */
    private fun backTrack() {
        /* Restore the model and workingSet to the state at the most recent guess */
        var guess: Literal? = null
        while (guess == null && modelSize != 0) {
            guess = model.removeAt(modelSize - 1)

            /* The most recent guess */if (guess.get() == lastGuess[lastGuess.size - 1].get()) {
                lastGuess.removeAt(lastGuess.size - 1)
                assignCount[guess.get() - 1] = 0

                /* If 'true' has already been selected we now choose 'false'*/if (guess.truth) {
                    /* Guess the negated literal now */
                    model.add(Literal(guess.get(), false))
                    modelSize++

                    /* Increment assign count for this literal */assignCount[guess.get() - 1]++

                    /* Mark this guess as the most recent one */lastGuess.add(
                            Literal(guess.get(), false))
                } else {
                    addToWorkingSet(guess)
                    assignCount[guess.get() - 1] = 0
                    guess = null
                }
            } else {
                addToWorkingSet(guess)
                assignCount[guess.get() - 1] = 0
                guess = null
            }
            modelSize--
        }
    }

    /* Computes the disjunction of all clauses that can be resolved to a boolean value */
    private fun conflict(): Boolean {
        for ((clauseIndex, c) in conjuncts.withIndex()) {
            /* Find how many literals are unassigned */
            var unassignedCount = 0
            for (lit in c.get()) {
                if (assignCount[lit.get() - 1] == 0) {
                    unassignedCount++
                }
            }

            /* Check if it is unit clause */if (unassignedCount == 0) {
                if (!checkFormula(c.get())) {
                    /* CDCL: Add new clause for performance */
                    cdcl(clauseIndex)
                    return true
                }
            }
        }
        return false
    }

    /* This method performs the DPLL algorithm on the conjuncts in order to find a model */
    fun findModel(_conjuncts: ArrayList<Clause>): ArrayList<Literal>? {
        /* Store the clauses/conjuncts into the object */
        conjuncts = _conjuncts

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
            val deduction = deduce()

            /* Unit propagation not possible. Must guess a literal value */
            if (deduction == 0) {
                /* Perform Pure Literal Assignment */
                guess()
            } else if (deduction == 1) {
                /* Conflict caught*/
                if (conflict()) {
                    /* If no guess was made */
                    if (lastGuess.size == 0) {
                        System.err.println("NO GUESSES")
                        return null
                    }

                    /* Restore state to most recent guess */backTrack()

                    /* Exhausted the model */if (modelSize == 0) {
                        System.err.println("MODEL EXHAUSTED")
                        return null
                    }
                }
            } else if (deduction == -1) {
                /* If no guess was made */
                if (lastGuess.size == 0) {
                    System.err.println("NO GUESSES")
                    return null
                }

                /* Restore state to most recent guess */
                backTrack()
            }
        }
        return model
    }

    init {
        conjuncts = ArrayList()

        /* Initialize the model */
        model = ArrayList()

        /* Construct a working set of possible literals for the model */
        workingSet = ArrayList()
        for (i in 1..numberOfLiterals) {
            addToWorkingSet(Literal(i, true))
        }

        /* No deductions yet */
        lastDeductionClause = null
    }
}