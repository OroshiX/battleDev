package sat

import java.util.*

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