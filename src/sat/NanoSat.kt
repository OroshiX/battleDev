package sat

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

    /* DEBUGGING */
    // for(Clause c : conjuncts)
    // 	System.err.println(c);

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
