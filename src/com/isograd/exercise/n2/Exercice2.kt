package com.isograd.exercise.n2

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.FileInputStream
import java.util.*

fun main() {
    with(Scanner(System.`in`)) {
//        with(Scanner(FileInputStream("D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex2\\input1.txt"))) {
        val N = nextLine()!!.toInt()
        for (i in 0 until N) {
            val (n, P, m) = nextLine().split(" ").map { it.toInt() }
        }

        print(N)
    }
}