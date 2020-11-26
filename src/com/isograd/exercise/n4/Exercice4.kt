package com.isograd.exercise.n4

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.*
import java.util.*
import kotlin.system.exitProcess


fun main() {
    val numInput = 1
    val numExercise = 4
    val outputExpected = Scanner(FileInputStream(
            "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\output$numInput.txt")).nextLine()
    with(Scanner(FileInputStream(
            "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\input$numInput.txt"))) {
//    with(Scanner(System.`in`)) {
        val (n, m) = nextLine()!!.split(" ").map { it.toInt() }
        val octets = nextLine().split(" ").map { it.toInt() }
        val decyphered = mutableListOf<Int>()// size n
        for (i in 0 until m) {
            val (l, r) = nextLine().split(" ").map { it.toInt() }
            decyphered.add(decypher(octets, l, r))
        }
        val numberOfI = toRes(decyphered)
        val res = numberOfI.joinToString(" ") { it.toString() }
        assert(res == outputExpected)
        if (res != outputExpected) System.err.println(
                "error: expected $outputExpected but found $res")
        print(res)
    }
}

fun toRes(decyphered: List<Int>): List<Int> {
    val res = List(256) { 0 }.toMutableList()
    decyphered.forEach {
        res[it]++
    }
    return res
}

fun decypher(octets: List<Int>, l: Int, r: Int): Int {
    val nb = r - l + 1
    return if (nb < 50) {
        var res = octets[l]
        for (i in l + 1..r) {
            res = res.xor(octets[i])
        }
        res
    } else {
        satSolve(nb, octets, l, r)
    }
}

fun satSolve(nb: Int, octets: List<Int>, l: Int, r: Int): Int {
    val sat = mutableListOf<Int>()
    sat.add(octets[l])
    for (i in l + 1..r) {
        // todo
    }
    return 255
}

// region SAT-solver

/** Defines a literal as an integer value with a boolean truth value */
data class Literal(private val lit: Int, var truth: Boolean) {

    fun get(): Int {
        return lit
    }

    fun set(_truthValue: Boolean) {
        truth = _truthValue
    }

    override fun equals(other: Any?): Boolean {
        return lit == (other as Literal?)!!.get()
    }

    override fun toString(): String {
        return if (truth) {
            lit.toString()
        } else {
            (lit * -1).toString()
        }
    }

    override fun hashCode(): Int {
        return lit
    }
}

/** Defines a clause as a list of disjuncts */
class Clause {
    private var disjuncts: ArrayList<Literal> = ArrayList()
    fun addDisjunct(lit: Literal) {
        disjuncts.add(lit)
    }

    fun get(): ArrayList<Literal> {
        return disjuncts
    }

    /** Pretty print for a clause */
    override fun toString(): String {
        /* Construct a single string for the entire clause */
        val clause: StringBuilder
        if (disjuncts.size == 1) {
            /* Pretty-print the one clause */
            clause = StringBuilder(disjuncts[0].toString())
        } else {
            clause = StringBuilder("(" + disjuncts[0].toString())

            /* Add the pretty-print version of each clause to the string */
            for (i in 1 until disjuncts.size) {
                clause.append(" V ").append(disjuncts[i].toString())
            }
            clause.append(")")
        }
        return clause.toString()
    }

}

class DPLLSolver(private val numberOfLiterals: Int) {
    private var modelSize = 0
    private val assignCount: IntArray = IntArray(numberOfLiterals)
    private val lastGuess: ArrayList<Literal> = ArrayList()
    private val model: ArrayList<Literal>
    private val workingSet: ArrayList<Literal>
    private var conjuncts: ArrayList<Clause>
    private var lastDeductionClause: Clause?

    /** Sorted insert for adding literals to working set */
    private fun addToWorkingSet(guess: Literal) {
        for (i in workingSet.indices) {
            if (guess.get() < workingSet[i].get()) {
                workingSet.add(i, Literal(guess.get(), true))
                return
            }
        }
        workingSet.add(Literal(guess.get(), true))
    }

    /** Removal of literal 'unit' from working set */
    private fun removeFromWorkingSet(unit: Literal) {
        for (i in workingSet.indices) {
            if (workingSet[i].get() == unit.get()) {
                workingSet.removeAt(i)
                return
            }
        }
    }

    /** Checks if the current assignment to the disjuncts yields 'true' */
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

    /** Finds the truth value of Literal with value 'lit' in the model */
    private fun truthValInModel(model: ArrayList<Literal>,
                                lit: Int): Boolean {
        for (l in model) {
            if (l.get() == lit) {
                return l.truth
            }
        }
        assert(false)
        return false
    }

    /** Unit Propagation. Returns:
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
        /* No unit clause found */
        return 0
    }

    /** Branching: choose the next value from the working set */
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

    /** Add new clause constructed from the conflicting condition */
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
        /* Add the new clause to the set of conjuncts */
        conjuncts.add(learn)
    }

    /** Reconstruct the model and workingSet to the last checkpoint at the most recent guess */
    private fun backTrack() {
        /* Restore the model and workingSet to the state at the most recent guess */
        var guess: Literal? = null
        while (guess == null && modelSize != 0) {
            guess = model.removeAt(modelSize - 1)

            /* The most recent guess */
            if (guess.get() == lastGuess[lastGuess.size - 1].get()) {
                lastGuess.removeAt(lastGuess.size - 1)
                assignCount[guess.get() - 1] = 0

                /* If 'true' has already been selected we now choose 'false'*/
                if (guess.truth) {
                    /* Guess the negated literal now */
                    model.add(Literal(guess.get(), false))
                    modelSize++

                    /* Increment assign count for this literal */
                    assignCount[guess.get() - 1]++

                    /* Mark this guess as the most recent one */
                    lastGuess.add(Literal(guess.get(), false))
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

    /** Computes the disjunction of all clauses that can be resolved to a boolean value */
    private fun conflict(): Boolean {
        for ((clauseIndex, c) in conjuncts.withIndex()) {
            /* Find how many literals are unassigned */
            var unassignedCount = 0
            for (lit in c.get()) {
                if (assignCount[lit.get() - 1] == 0) {
                    unassignedCount++
                }
            }
            /* Check if it is unit clause */
            if (unassignedCount == 0) {
                if (!checkFormula(c.get())) {
                    /* CDCL: Add new clause for performance */
                    cdcl(clauseIndex)
                    return true
                }
            }
        }
        return false
    }

    /** This method performs the DPLL algorithm on the conjuncts in order to find a model */
    fun findModel(_conjuncts: ArrayList<Clause>): ArrayList<Literal>? {
        /* Store the clauses/conjuncts into the object */
        conjuncts = _conjuncts
        /* Work on the model recursively until solution found */
        while (modelSize != numberOfLiterals) {
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
                    /* Restore state to most recent guess */
                    backTrack()
                    /* Exhausted the model */
                    if (modelSize == 0) {
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

/** A class responsible for parsing files in the DIMACS format.
The primary parseDimacs() method returns a list of clauses

TO DO: - General input parsing
- Better code refactoring		*/
class DimacsParser(private val file: Boolean,
                   private val filenameOrContent: String) {
    var numLiterals = 0
        private set
    private var numberOfClauses = 0

    /** Opens the file and returns a list of clauses parsed
    from the file */
    fun parseDimacs(): ArrayList<Clause> {
        var line = ""
        var split: Array<String>
        val clauses = ArrayList<Clause>()
        var problemLineReached = false
        val reader: BufferedReader

        /* Open the file */
        try {
            reader = if (file) {
                BufferedReader(FileReader(filenameOrContent))
            } else {
                BufferedReader(StringReader(filenameOrContent))
            }
        } catch (e: FileNotFoundException) {
            System.err.println("File not found! Exiting...")
            exitProcess(0)
        }

        /* Read the file and catch the clauses */
        try {
            /* Read until the "problem" line is reached */
            while (!problemLineReached && reader.readLine().also {
                        line = it
                    } != null) {
                /* Check if "problem" line has been reached */
                if (line.isNotEmpty()) {
                    for (i in line.indices) {
                        if (line[0] == 'p') {
                            problemLineReached = true
                            break
                        }
                    }
                }
            }

            /* Read in the "problem" line */
            split = line.split("\\s+".toRegex()).toTypedArray()
            var i = 0
            while (split[i] == "") {
                i++
            }
            i += 2
            numLiterals = split[i].toInt()
            i++
            while (split[i] == "") {
                i++
            }
            numberOfClauses = split[i].toInt()

            /* Parse all clauses */
            var clauseCounter = 0
            var endOfClause = false
            while (clauseCounter < numberOfClauses) {
                /* Initialize the clause */
                val clause = Clause()

                /* Construct the clause */
                while (!endOfClause) {
                    line = reader.readLine()
                    split = line.split("\\s+".toRegex()).toTypedArray()
                    for (s in split) {
                        if (s != "") {
                            if (s.toInt() == 0) {
                                endOfClause = true
                            } else {
                                val literal = s.toInt()
                                if (literal > 0) {
                                    clause.addDisjunct(
                                            Literal(literal, true))
                                } else {
                                    clause.addDisjunct(
                                            Literal(literal * -1, false))
                                }
                            }
                        }
                    }
                }
                clauses.add(clause)
                clauseCounter++
                endOfClause = false
            }
        } catch (e: IOException) {
            System.err.println("IOException! Exiting...")
            exitProcess(0)
        }

        /* Close the file */
        try {
            reader.close()
        } catch (e: IOException) {
            System.err.println("IOException! Exiting...")
            exitProcess(0)
        }
        return clauses
    }
}

fun transformToCnf(listOfOrs: List<Array<Int>>,
                   nbOfVariables: Int): String {
    // problem
    var sb = "p cnf $nbOfVariables ${listOfOrs.size}\n"
    // clauses
    for (ors in listOfOrs) {
        sb += ors.joinToString(separator = " ", prefix = "",
                               postfix = " 0\n") { i -> i.toString() }
    }
    return sb
}

/** Prints the returned model from the solver */
fun modelToString(model: ArrayList<Literal>): String {
    val sb = StringBuilder()
    for (lit in model) {
        if (lit.truth) {
            sb.append(lit.get())
        } else {
            sb.append(lit.get() * -1)
        }
        sb.append(" ")
    }
    return sb.toString()
}

/**
 * This is the complete (from List of ORs to solved model) workflow function
 * to solve SATs problems
 */
fun completeSolving(listOfOrs: List<Array<Int>>,
                    nbOfVariables: Int): ArrayList<Literal>? {
    val cnfFormat = transformToCnf(listOfOrs, nbOfVariables)
    val parser = DimacsParser(false, cnfFormat)
    val conjuncts = parser.parseDimacs()
    val solver = DPLLSolver(parser.numLiterals)
    return solver.findModel(conjuncts)
}
// endregion