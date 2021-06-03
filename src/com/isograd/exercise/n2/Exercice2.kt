package com.isograd.exercise.n2

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.File
import java.io.FileInputStream
import java.util.*

// for export to site
fun main() {
    val res = solve(Scanner(System.`in`))
    print(res)
}

fun solve(scanner: Scanner): String {
    with(scanner) {
        val map = mutableMapOf<String, Int>()
        val n = nextLine().toInt()
        for (i in 0 until n) {
            val button = next()
            map[button] = map.getOrDefault(button, 0) + 1
        }
        return map.filter { entry -> entry.value == 2 }.keys.first()
    }
}

// region for local testing
const val numExercise = 2
@Suppress("DuplicatedCode")
fun main(args: Array<String>) {
    val output = ".\\Inputs\\ex%d\\output%d.txt"
    val input = ".\\Inputs\\ex%d\\input%d.txt"
    var numInput = 1
    var fileInput = File(input.format(numExercise, numInput))

    while (fileInput.exists()) {
        System.err.println("With input $numInput")
        val outputExpected =
            Scanner(FileInputStream(output.format(numExercise, numInput))).nextLine()
        val startTime = System.currentTimeMillis()
        val res = solve(Scanner(FileInputStream(fileInput)))
        System.err.println(
            "[Ex $numExercise; input $numInput]: ${System.currentTimeMillis() - startTime} ms"
        )
        if(res.trim() != outputExpected.trim()) {
            System.err.println("Error: Expected $outputExpected but found $res")
        }
        println(res)
        numInput++
        fileInput = File(input.format(numExercise, numInput))
    }
}
// endregion