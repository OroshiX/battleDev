package com.isograd.exercise.util


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