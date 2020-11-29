package com.isograd.exercise.n4

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.FileInputStream
import java.util.*


fun main() {
    val debug = true
    val numExercise = 4
    val totalInputs = if (debug) 4 else 1
    for (numInput in 1..totalInputs) {
        val startTime = System.currentTimeMillis()
        val outputExpected: String = if (debug) Scanner(FileInputStream(
                "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\output$numInput.txt")).nextLine()
        else ""

        with(Scanner(if (debug) FileInputStream(
                "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\input$numInput.txt")
                     else System.`in`)) {

            val (n, m) = nextLine()!!.split(" ").map { it.toInt() }
            val octets = nextLine().split(" ").map { it.toInt() }
            val resList = constructResList(octets)
            val decyphered = mutableListOf<Int>()// size n
            for (i in 0 until m) {
                val (l, r) = nextLine().split(" ").map { it.toInt() }
                val decypherI = decypher(resList, l, r, octets)
                decyphered.add(decypherI)
            }
            val numOfI = toRes(decyphered)
            val res = numOfI.joinToString(" ") { it.toString() }
            if (debug) {
                if (res != outputExpected) System.err.println(
                        "error: expected $outputExpected but found $res")
                System.err.println("Took ${System.currentTimeMillis() - startTime} ms to execute")
                println(res)
            } else {
                print(res)
            }
        }
    }
}

fun toRes(decyphered: List<Int>): List<Int> {
    val res = List(256) { 0 }.toMutableList()
    decyphered.forEach {
        res[it]++
    }
    return res
}

/**
 * Pre-requisite: l<r
 */
fun decypher(resList: List<Int>, l: Int, r: Int, octets: List<Int>): Int {
    return when {
        l == r -> octets[l]
        l > 0 -> resList[r].xor(resList[l - 1])
        else -> resList[r]
    }
}

fun constructResList(octets: List<Int>): List<Int> {
    val resList = mutableListOf<Int>()
    resList.add(octets.first())
    for (i in 1 until octets.size) {
        resList.add(resList[i - 1].xor(octets[i]))
    }
    return resList
}