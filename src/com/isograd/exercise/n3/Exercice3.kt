package com.isograd.exercise.n3

/***************************
 ***                     ***
 *** Solution by OroshiX ***
 ***                     ***
 ***************************/
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayDeque


fun main() {
    with(Scanner(FileInputStream(
            "D:\\Documents\\Dev\\projects\\battledev\\Inputs\\ex3\\input2.txt"))) {
//    with(Scanner(System.`in`)) {
        val n = nextLine()!!.toInt()
        val setAgentNames = mutableMapOf<Int, Agent>()
        for (i in 1 until n) {
            val (a, b) = nextLine().split(" ").map { it.toInt() }
            if (!setAgentNames.containsKey(a)) {
                setAgentNames[a] = Agent(a, mutableListOf())
            }
            if (!setAgentNames.containsKey(b)) {
                setAgentNames[b] = Agent(b, mutableListOf())
            }
            val bb = setAgentNames[b]!!
            bb.children.add(setAgentNames[a]!!)
        }
        print(calculateNumberAgents(setAgentNames[0]!!).joinToString(
                " ") { i -> i.toString() })
    }
}

fun calculateNumberAgents(agent: Agent): List<Int> {
    val res = List(10) { 0 }.toMutableList()
    val queue = ArrayDeque<Agent>()
    queue.add(agent)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if(current.niveau + 1 < 10) {
            res[current.niveau + 1] += current.children.size
        }
        if (current.children.isNotEmpty()) {
            current.children.forEach { it.niveau = current.niveau + 1 }
            queue.addAll(current.children)
            current.children.clear()
        }
    }
    return res.apply { this[0] = 1 }
}

data class Agent(val name: Int, val children: MutableList<Agent>,
                 var niveau: Int = 0)