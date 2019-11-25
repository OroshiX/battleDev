package sat;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/* A class responsible for parsing files in the DIMACS format.
   The primary parseDimacs() method returns a list of clauses 

   TO DO: - General input parsing
   		  - Better code refactoring		*/
public class DimacsParser {
    private boolean file;
    private String filenameOrContent;
    private BufferedReader reader;
    private int numberOfLiterals;
    private int numberOfClauses;

    public DimacsParser(boolean file, String fileOrString) {
        this.file = file;
        filenameOrContent = fileOrString;
        numberOfLiterals = 0;
        numberOfClauses = 0;
    }

    public int getNumLiterals() {
        return numberOfLiterals;
    }

    /* Opens the file and returns a list of clauses parsed
       from the file */
    public ArrayList<Clause> parseDimacs() {
        String line = "";
        String[] split;
        ArrayList<Clause> clauses = new ArrayList<>();
        boolean problemLineReached = false;

        /* Open the file */
        try {
            if (file) {
                reader = new BufferedReader(new FileReader(filenameOrContent));
            } else {
                reader = new BufferedReader(new StringReader(filenameOrContent));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found! Exiting...");
            System.exit(0);
        }

        /* Read the file and catch the clauses */
        try {
            /* Read until the "problem" line is reached */
            while (!problemLineReached && (line = reader.readLine()) != null) {
                /* Check if "problem" line has been reached */
                if (line.length() > 0) {
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(0) == 'p') {
                            problemLineReached = true;
                            break;
                        }
                    }
                }
            }

            /* Read in the "problem" line */
            split = line.split("\\s+");

            int i = 0;
            while (Objects.equals(split[i], "")) {
                i++;
            }
            i = i + 2;

            numberOfLiterals = Integer.parseInt(split[i]);

            i++;
            while (Objects.equals(split[i], "")) {
                i++;
            }
            numberOfClauses = Integer.parseInt(split[i]);

            /* Parse all clauses */
            int clauseCounter = 0;
            boolean endOfClause = false;

            while (clauseCounter < numberOfClauses) {
                /* Initialize the clause */
                Clause clause = new Clause();

                /* Construct the clause */
                while (!endOfClause) {
                    line = reader.readLine();
                    split = line.split("\\s+");

                    for (String s : split) {
                        if (!s.equals("")) {
                            if (Integer.parseInt(s) == 0) {
                                endOfClause = true;
                            } else {
                                int literal = Integer.parseInt(s);

                                if (literal > 0) {
                                    clause.addDisjunct(new Literal(literal, true));
                                } else {
                                    clause.addDisjunct(new Literal(literal * -1, false));
                                }
                            }
                        }
                    }
                }

                clauses.add(clause);
                clauseCounter++;
                endOfClause = false;
            }
        } catch (IOException e) {
            System.err.println("IOException! Exiting...");
            System.exit(0);
        }

        /* Close the file */
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("IOException! Exiting...");
            System.exit(0);
        }

        return clauses;
    }
}