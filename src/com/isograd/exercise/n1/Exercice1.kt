package com.isograd.exercise.n1

import java.util.*

fun main(args: Array<String>) {
    with(Scanner(System.`in`)) {
        val n = nextLine().toInt()
        val lines = mutableListOf<Pair<Int, String>>()
        for (i in 0 until n) {
            val line = nextLine().split(" ")
            lines.add(Pair(line[0].toInt(), line[1]))
        }
        print(lines.maxByOrNull { pair -> pair.first }?.second)
    }
}

fun readInts() = readLine()!!.split(' ').map { it.toInt() }