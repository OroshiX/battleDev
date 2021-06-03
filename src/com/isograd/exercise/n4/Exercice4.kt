package com.isograd.exercise.n4

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
        val n = nextLine().toInt()
        val debris = next().toCharArray()
        if (n % 2 != 0) return "0"
        val nbWaysCut = nbWaysCut(debris.toTypedArray().toMutableList())
        return "$nbWaysCut"
    }
}

fun nbWaysCut(debris: List<Char>): Int {
    val half = debris.size / 2
    var nbWaysCut = 0
    for (i in 0 until half) {
        val secondCut = i + half
        if (isEquilibrium(debris, i, secondCut)) {
            nbWaysCut += 2
        }
    }
    return nbWaysCut
}

fun nbWaysCut2(debris: List<Char>): Int {
    val charToIndices = charToIndices(debris)
    val half = debris.size / 2
    var possibleCuts: Set<Int> = debris.indices.toSet()
    for (charToIndex in charToIndices) {
        val indices = charToIndex.value
        if (indices.size % 2 != 0) return 0
        val currentPossibleCuts = possibleCuts(indices, half)
        possibleCuts = possibleCuts.intersect(currentPossibleCuts)
        if (possibleCuts.isEmpty()) return 0
    }
    var nbWaysCut = 0
    for (i in possibleCuts) {
        val secondCut = i + half
        if (isEquilibrium(debris, i, secondCut)) {
            nbWaysCut += 2
        }
    }
    return nbWaysCut
}

fun minus(i: Int, modulo: Int): Int {
    return if (i > 0) i - 1 else modulo
}

fun plus(i: Int, modulo: Int): Int {
    return (i + 1) % modulo
}

fun distance(indices: List<Int>, i1: Int, i2: Int, size: Int): Int {
    if (i2 >= indices.size) return size + indices[i2 % indices.size] - indices[i1]
    if (i1 < 0) return indices[i2] - indices[i1] - size
    return indices[i2] - indices[i1]
}

fun possibleCuts(indices: List<Int>, half: Int): Set<Int> {
    val cuts = mutableSetOf<Int>()
    val halfNumberOfChar = indices.size / 2
    val size = indices.size
    for (i in 0 until halfNumberOfChar) {
        val firstCut = indices[i]
        val secondCut = indices[i + halfNumberOfChar]
        if (secondCut + 1 - firstCut > half) continue

        // not a cut possible
        val distance1 =distance(indices, i, i + halfNumberOfChar, size)
        if (distance1> half) continue
        val distance2 = distance(indices, i-1,i+halfNumberOfChar, size)
        if(distance2 > half) continue


        // range for first cut
        if (i > 0) {
            cuts.addAll(indices[i - 1] + 1 until firstCut)
        } else {
            cuts.add(firstCut)
        }
    }
    return cuts
}

fun charToIndices(debris: List<Char>): Map<Char, List<Int>> {
    val map = mutableMapOf<Char, MutableList<Int>>()
    for (e in debris.withIndex()) {
        map[e.value] = map.getOrDefault(e.value, mutableListOf()).apply { add(e.index) }
    }
    return map
}

fun isEquilibrium(debris: List<Char>, firstCut: Int, secondCut: Int): Boolean {
    val map1 = mutableMapOf<Char, Int>()
    val map2 = mutableMapOf<Char, Int>()
//    val intRange1 = (0 until firstCut).union(secondCut until debris.size)
    val intRange2 = firstCut until secondCut
    for (i in 0 until firstCut) {
        val char = debris[i]
        map1[char] = map1.getOrDefault(char, 0) + 1
    }
    for (i in secondCut until debris.size) {
        val char = debris[i]
        map1[char] = map1.getOrDefault(char, 0) + 1
    }
    for (i in intRange2) {
        val char = debris[i]
        map2[char] = map2.getOrDefault(char, 0) + 1
    }
    return map1 == map2
}


// region for local testing
const val numExercise = 4

@Suppress("DuplicatedCode")
fun main(args: Array<String>) {
    val output = ".\\Inputs\\ex%d\\output%d.txt"
    val input = ".\\Inputs\\ex%d\\input%d.txt"
    var numInput = 0
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