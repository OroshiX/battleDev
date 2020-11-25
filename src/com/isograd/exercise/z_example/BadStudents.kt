package com.isograd.exercise.z_example

import com.isograd.exercise.util.transformToCnf
import sat.DPLLSolver
import sat.DimacsParser
import sat.Literal
import java.io.OutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.math.abs

private var N = 0

fun solve(testNumber: Int, `in`: Scanner, out: PrintWriter) {
    N = `in`.nextInt()
    val horairesA: MutableList<Int> = ArrayList()
    val horairesB: MutableList<Int> = ArrayList()
    for (i in 0 until N) {
        horairesA.add(`in`.nextInt())
        horairesB.add(`in`.nextInt())
    }
    val creneauxIncompatibles =
            calculateIncompatibleCreneaux(horairesA, horairesB)
    val listOfOrs = transformToListOfOrs(creneauxIncompatibles)
    val cnf = transformToCnf(listOfOrs, 2 * N)
    val parser = DimacsParser(false, cnf)
    val conjuncts = parser.parseDimacs()
    val solver = DPLLSolver(parser.numLiterals)
    val model = solver.findModel(conjuncts)
    if (model == null) {
        out.println("KO")
    } else {
        model.sortWith(
                Comparator.comparingInt { obj: Literal -> obj.get() })
        for (i in 0 until N) {
            val literal = model[i]
            out.println(if (literal.truth) 1 else 2)
        }
    }
}

private fun isIncompatible(x: Int, y: Int): Boolean {
    return abs(x - y) < 60
}

private fun calculateIncompatibleCreneaux(horairesA: List<Int>,
                                          horairesB: List<Int>): List<CreneauxIncompatibles> {
    val res: MutableList<CreneauxIncompatibles> = ArrayList()
    for (i in 0 until N) {
        for (j in i until N) {
            if (i == j) continue
            // Compare A with A
            if (isIncompatible(horairesA[i], horairesA[j])) {
                res.add(CreneauxIncompatibles(Choice.A, i + 1, Choice.A,
                                              j + 1))
            }
            // Compare B with B
            if (isIncompatible(horairesB[i], horairesB[j])) {
                res.add(CreneauxIncompatibles(Choice.B, i + 1, Choice.B,
                                              j + 1))
            }
        }
    }
    for (i in 0 until N) {
        for (j in 0 until N) {
            if (i == j) continue
            // Compare A with B
            if (isIncompatible(horairesA[i], horairesB[j])) {
                res.add(CreneauxIncompatibles(Choice.A, i + 1, Choice.B,
                                              j + 1))
            }
        }
    }
    return res
}

private fun transformToListOfOrs(
        creneauxIncompatibles: List<CreneauxIncompatibles>): List<Array<Int>> {
    val orList: MutableList<Array<Int>> = ArrayList()
    // Regle generale
    for (i in 1..N) {
        orList.add(arrayOf(i, i + N))
        orList.add(arrayOf(-i, -(i + N)))
    }
    // Avec creneaux
    for (incompatibles in creneauxIncompatibles) {
        orList.add(
                arrayOf(-(incompatibles.index1 + if (incompatibles.choice1 == Choice.A) 0 else N),
                        -(incompatibles.index2 + if (incompatibles.choice2 == Choice.A) 0 else N)))
    }
    return orList
}

fun main(args: Array<String>) {
    val inputStream = System.`in`
    val outputStream: OutputStream = System.out
    val `in` = Scanner(inputStream)
    val out = PrintWriter(outputStream)
    solve(1, `in`, out)
    out.close()
}


class CreneauxIncompatibles(
        val choice1: Choice, val index1: Int, val choice2: Choice,
        val index2: Int) {

    override fun toString(): String {
        return "CreneauxIncompatibles{$choice1$index1, $choice2$index2}"
    }
}

enum class Choice {
    A, B
}
