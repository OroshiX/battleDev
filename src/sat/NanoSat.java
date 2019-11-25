package sat;
/*
 * Date 05/01/2016
 *
 * @author Hassan Zaidi
 */

import java.util.ArrayList;

public class NanoSat {
    private static DimacsParser parser;
    private static DPLLSolver solver;
    private static ArrayList<Clause> conjuncts;

    /* Prints the returned model from the solver */
    private static void printModel(ArrayList<Literal> model) {
        for (Literal lit : model) {
            if (lit.getTruth()) {
                System.err.print(lit.get());
                System.err.print(" ");
            } else {
                System.err.print(lit.get() * -1);
                System.err.print(" ");
            }
        }

        System.err.println();
    }

    public static void main(String[] args) {
		/* Check correct argument usage:
		       args[0] = dimacs file */
        if (args.length != 1) {
            System.err.println("Usage: java NanoSat <file>");
            System.exit(0);
        }

        /* Initialize parser and parse input file */
        parser = new DimacsParser(true, args[0]);
        conjuncts = parser.parseDimacs();

        /* DEBUGGING */
        // for(Clause c : conjuncts)
        // 	System.err.println(c);

        /* Apply DPLL and return the model */
        solver = new DPLLSolver(parser.getNumLiterals());
        ArrayList<Literal> model = solver.findModel(conjuncts);

        /* Print output */
        if (model == null) {
            System.err.println("Unsat");
        } else {
            System.err.println("Sat\n");
            printModel(model);
        }
    }
}