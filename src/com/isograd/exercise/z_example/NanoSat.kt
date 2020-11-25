package com.isograd.exercise.z_example

import com.isograd.exercise.util.DPLLSolver
import com.isograd.exercise.util.DimacsParser
import com.isograd.exercise.util.modelToString
import kotlin.system.exitProcess

/*
* Date 05/01/2016
*
* @author Hassan Zaidi
*/

fun main(args: Array<String>) {
    /* Check correct argument usage:
           args[0] = dimacs file */
    if (args.size != 1) {
        System.err.println("Usage: java NanoSat <file>")
        exitProcess(0)
    }

    /* Initialize parser and parse input file */
    val parser = DimacsParser(true, args[0])
    val conjuncts = parser.parseDimacs()

    /* Apply DPLL and return the model */
    val solver = DPLLSolver(parser.numLiterals)
    val model = solver.findModel(conjuncts)

    /* Print output */
    if (model == null) {
        System.err.println("Unsat")
    } else {
        System.err.println("Sat\n")
        modelToString(model)
    }
}
