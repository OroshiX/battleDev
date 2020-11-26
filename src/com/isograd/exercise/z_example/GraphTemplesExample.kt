/****************************************************************
 ***
 ***
 *** SOLUTION BY Neumann
 ***
 ***
 ********************************************************************/
/**
 * Example input:
 *
F
Lamebring Sanakil
Lamithralas Clingoniel
Earylas Clingoniel
Tunhaad Uriolad
Maethoraran Iseldur
Rithralas Maethoraran
Iseldur Mebrimir
Maethoraran Horil
Uriolad Fioldor
Lamebring Tunhaad
Earylas Earaindir
Earylas Fioldor
Sanakil Lamithralas
Tunhaad Fioldor
Horil Mebrimir
Sanakil Horil
Uriolad Mebrimir
Earaindir Clingoniel
Rithralas Lamithralas
Lamebring Iseldur
Rithralas Earaindir
 *
 * Should output : Maethoraran
 */
package com.isograd.exercise.z_example

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


fun computeHashes(newMap: Map<String, Node>) {
    for ((_, value) in newMap) {
        val hash = getNodeHash(value)
        value.firstHash = hash
    }
    for ((_, value) in newMap) {
        var hash = value.firstHash * 1000
        for (nei in value.next) {
            hash += nei!!.firstHash
        }
        value.secondHash = hash
        //            System.out.println("Node " + e.getKey() + " hash = " + hash);
    }
}

fun getNodeHash(n: Node): Int {
    val done: MutableSet<String> = HashSet()
    var hash = 0
    val queue = LinkedList<Bfs>()
    queue.addLast(Bfs(n, 0))
    done.add(n.name)
    while (!queue.isEmpty()) {
        val c = queue.pollFirst()
        hash += c.dist * c.dist
        for (nn in c.node!!.next) {
            if (!done.contains(nn!!.name)) {
                queue.add(Bfs(nn, c.dist + 1))
                done.add(nn.name)
            }
        }
    }
    return hash
}

class Node(var name: String) {
    var next: MutableList<Node?> = ArrayList()
    var firstHash = 0
    var secondHash = 0
}

class Bfs(var node: Node?, var dist: Int)

fun main() {
    val `in` = Scanner(System.`in`)
    //        Scanner in = new Scanner(new FileInputStream("H:\\Workspace\\untitled\\BattleDev\\kek\\input1.txt"));
    val oldMap: MutableMap<String, Node> = HashMap()
    for (i in 0..13) {
        val name = (65 + i).toChar().toString()
        oldMap[name] = Node(name)
    }
    oldMap["A"]!!.next.add(oldMap["F"])
    oldMap["A"]!!.next.add(oldMap["M"])
    oldMap["A"]!!.next.add(oldMap["J"])
    oldMap["B"]!!.next.add(oldMap["C"])
    oldMap["B"]!!.next.add(oldMap["K"])
    oldMap["B"]!!.next.add(oldMap["H"])
    oldMap["C"]!!.next.add(oldMap["B"])
    oldMap["C"]!!.next.add(oldMap["D"])
    oldMap["C"]!!.next.add(oldMap["G"])
    oldMap["D"]!!.next.add(oldMap["C"])
    oldMap["D"]!!.next.add(oldMap["G"])
    oldMap["D"]!!.next.add(oldMap["L"])
    oldMap["E"]!!.next.add(oldMap["G"])
    oldMap["E"]!!.next.add(oldMap["N"])
    oldMap["E"]!!.next.add(oldMap["M"])
    oldMap["F"]!!.next.add(oldMap["K"])
    oldMap["F"]!!.next.add(oldMap["A"])
    oldMap["F"]!!.next.add(oldMap["I"])
    oldMap["G"]!!.next.add(oldMap["D"])
    oldMap["G"]!!.next.add(oldMap["C"])
    oldMap["G"]!!.next.add(oldMap["E"])
    oldMap["H"]!!.next.add(oldMap["I"])
    oldMap["H"]!!.next.add(oldMap["B"])
    oldMap["H"]!!.next.add(oldMap["J"])
    oldMap["I"]!!.next.add(oldMap["F"])
    oldMap["I"]!!.next.add(oldMap["H"])
    oldMap["I"]!!.next.add(oldMap["L"])
    oldMap["J"]!!.next.add(oldMap["H"])
    oldMap["J"]!!.next.add(oldMap["A"])
    oldMap["J"]!!.next.add(oldMap["N"])
    oldMap["K"]!!.next.add(oldMap["B"])
    oldMap["K"]!!.next.add(oldMap["F"])
    oldMap["K"]!!.next.add(oldMap["L"])
    oldMap["L"]!!.next.add(oldMap["K"])
    oldMap["L"]!!.next.add(oldMap["I"])
    oldMap["L"]!!.next.add(oldMap["D"])
    oldMap["M"]!!.next.add(oldMap["E"])
    oldMap["M"]!!.next.add(oldMap["N"])
    oldMap["M"]!!.next.add(oldMap["A"])
    oldMap["N"]!!.next.add(oldMap["M"])
    oldMap["N"]!!.next.add(oldMap["E"])
    oldMap["N"]!!.next.add(oldMap["J"])
    val newMap: MutableMap<String, Node> = HashMap()
    val lostT = `in`.nextLine()
    for (i in 0..20) {
        val temples = `in`.nextLine().split(" ".toRegex()).toTypedArray()
        var n1 = newMap[temples[1]]
        if (n1 == null) {
            n1 = Node(temples[1])
            newMap[n1.name] = n1
        }
        var n0 = newMap[temples[0]]
        if (n0 == null) {
            n0 = Node(temples[0])
            newMap[n0.name] = n0
        }
        n0.next.add(n1)
        n1.next.add(n0)
    }
    computeHashes(oldMap)
    computeHashes(newMap)
    val targetH = oldMap[lostT]!!.secondHash
    for ((key, value) in newMap) {
        if (value.secondHash == targetH) {
            println(key)
            break
        }
    }
}