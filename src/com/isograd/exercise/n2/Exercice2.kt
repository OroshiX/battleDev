package com.isograd.exercise.n2

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.util.*

fun main() {
    with(Scanner(System.`in`)) {
//        with(Scanner(FileInputStream("D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex2\\input1.txt"))) {
        val N = nextLine()!!.toInt()
        var day = 0
        var night = 0
        for (i in 0 until N) {
            val (h, m) = nextLine().split(":").map { it.toInt() }
            if (h >= 20 || h <= 7) {
                night ++
            } else {
                day++
            }
        }
        print(if (night > day) "SUSPICIOUS" else "OK")
    }
}