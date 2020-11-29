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
    val maxInputs = 4
    val totalInputs = if (debug) maxInputs else 1
    for (numInput in 1..totalInputs) {
        val startTime = System.currentTimeMillis()
        val outputExpected: String = if (debug) Scanner(FileInputStream(
                "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\output$numInput.txt")).nextLine()
        else ""
        val res = solve(Scanner(if (debug) FileInputStream(
                "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\input$numInput.txt")
                                else System.`in`))
        if (debug) {
            if (res != outputExpected) System.err.println(
                    "error: expected $outputExpected but found $res")
            System.err.println(
                    "Took ${System.currentTimeMillis() - startTime} ms to execute")
            println(res)
        } else {
            print(res)
        }
    }
}

fun solve(scanner: Scanner): String {
    with(scanner) {
        val n = nextLine().toInt()
        TODO()
    }
}