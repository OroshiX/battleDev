package com.isograd.exercise.util

import sat.DPLLSolver
import sat.DimacsParser
import sat.Literal
import java.util.*


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

/* Prints the returned model from the solver */
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

fun main() {
    val cnfFormat =
            transformToCnf(listOf(arrayOf(1, 2, -3), arrayOf(2, 3), arrayOf(-2),
                                  arrayOf(-1, 3)), 3)
    val parser = DimacsParser(false, cnfFormat)
    val conjuncts = parser.parseDimacs()
    val solver = DPLLSolver(parser.numLiterals)
    val model = solver.findModel(conjuncts)
    if (model == null) {
        print("NOT Possible")
    } else {
        println("SAT")
        println(modelToString(model))
    }

}