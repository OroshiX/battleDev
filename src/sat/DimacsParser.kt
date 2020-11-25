package sat

import java.io.*
import java.util.*
import kotlin.system.exitProcess

/* A class responsible for parsing files in the DIMACS format.
  The primary parseDimacs() method returns a list of clauses

  TO DO: - General input parsing
            - Better code refactoring		*/
class DimacsParser(private val file: Boolean,
                   private val filenameOrContent: String) {
    var numLiterals = 0
        private set
    private var numberOfClauses = 0

    /* Opens the file and returns a list of clauses parsed
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

        /* Read the file and catch the clauses */try {
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

            /* Read in the "problem" line */split =
                    line.split("\\s+".toRegex()).toTypedArray()
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

                /* Construct the clause */while (!endOfClause) {
                    line = reader.readLine()
                    split = line.split("\\s+".toRegex()).toTypedArray()
                    for (s in split) {
                        if (s != "") {
                            if (s.toInt() == 0) {
                                endOfClause = true
                            } else {
                                val literal = s.toInt()
                                if (literal > 0) {
                                    clause.addDisjunct(Literal(literal, true))
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