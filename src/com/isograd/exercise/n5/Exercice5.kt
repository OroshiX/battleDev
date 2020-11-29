package com.isograd.exercise.n5

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.FileInputStream
import java.util.*
import kotlin.math.pow


fun main() {
    //        with(Scanner(FileInputStream("D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex5\\input1.txt"))) {
    val numExercise = 5
    val debug = true
    val totalInputs = if (debug) 5 else 1
    for (numInput in 1..totalInputs) {
        val outputExpected: String = if (debug) Scanner(FileInputStream(
                "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\output$numInput.txt")).nextLine()
        else ""

        with(Scanner(if (debug) FileInputStream(
                "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex$numExercise\\input$numInput.txt")
                     else System.`in`)) {
            val username = nextLine()
            val hash = userHash(username)
            val collision = revHash(hash)
            if (debug) {
                if (outputExpected != collision) {
                    System.err.println(
                            "Expected $outputExpected but found $collision")
                }
                System.err.println(
                        "Input: $username, hash(username)=$hash, res=$collision, hashOfCollision=${
                            userHash(collision)
                        }")
                println(collision)
            } else {
                print(collision)
            }
        }
    }
}

const val capsA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
const val collisionBase = 5
fun userHash(s: String): Int {
    var hash = 0
    for (c in s) {
        hash *= 31
        hash += c.toInt()
    }
    return hash % 2.0.pow(32.0).toInt()
}

fun revHash(hash: Int): String {
    val h1Lookup = mutableMapOf<Int, String>()
    for (i in 0 until 100000) {
        val half1 =
                (1..collisionBase).fold("") { acc, _ -> acc + capsA.random() }
        val half1hash = userHash(half1)
        h1Lookup[half1hash] = half1
    }
    while (true) {
        val half2 =
                (1..collisionBase).fold("") { acc, _ -> acc + capsA.random() }
        val half2hash =
                (31.0.pow(collisionBase).toInt() * userHash(half2)) % 2.0.pow(
                        32).toInt()
        val rem = (hash - half2hash) % 2.0.pow(32).toInt()
        if (h1Lookup.containsKey(rem))
            return half2 + h1Lookup[rem]
    }
}