package com.isograd.exercise.n1

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.FileInputStream
import java.util.*

fun main() {
    //        with(Scanner(FileInputStream("D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex1\\input1.txt"))) {
    with(Scanner(System.`in`)) {
        val n = nextLine().toInt()
        var s = 0
        for (i in 0 until n) {
            val line = nextLine()
            val matches = line.matches(Regex("\\w*[0-9]{5}"))
            if(matches) s++
        }
        print(s)
//        print(lines.maxByOrNull { pair -> pair.first }?.second)
    }
}
// maxByOrNull { pair -> pair.first}?.second

//fun readInts() = readLine()!!.split(' ').map { it.toInt() }