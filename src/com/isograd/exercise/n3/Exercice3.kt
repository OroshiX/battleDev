package com.isograd.exercise.n3

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
        val grid = mutableListOf<String>()
        for (i in 1..20) {
            val line = next()
            grid.add(line)
        }
        val t = hasTetris(grid) ?: return "NOPE"
        return "BOOM ${t + 1}"
    }
}

fun hasTetris(grid: List<String>): Int? {
    for (j in 0 until 10) {
        if (tetrisOnColumn(grid, j)) {
            return j
        }
    }
    return null
}

fun tetrisOnColumn(grid: List<String>, j: Int): Boolean {
    var lastEmptyRow = 0
    for (i in grid.indices) {
        if (grid[i][j] == '.') {
            lastEmptyRow = i
        } else {
            break
        }
    }
    if (lastEmptyRow < 3) return false // no tetris
    for (i in lastEmptyRow downTo lastEmptyRow - 3) {
        val isTetris = grid[i].foldIndexed(true) { index, acc, c ->
            acc && (index == j || c == '#')
        }
        if (!isTetris) return false
    }
    return true
}


// region for local testing
const val numExercise = 3

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