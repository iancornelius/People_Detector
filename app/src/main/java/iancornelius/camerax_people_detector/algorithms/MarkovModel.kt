package iancornelius.camerax_people_detector.algorithms

import android.graphics.Point
import kotlin.random.Random

class MarkovModel {

    val STATES = arrayOf(
        Point(-1, -1), Point(-1, 0), Point(-1, 1),
        Point(0, -1), Point(0, 0), Point(0, 1),
        Point(1, -1), Point(1, 0), Point(1, 1)
    )

    var F: Array<IntArray> = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )
    var P: Array<DoubleArray> = arrayOf(
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    )
//    lateinit var P: Array<FloatArray>
//    var P: Array<FloatArray> = arrayOf(doubleArrayOf(), doubleArrayOf())

    init {
        for (i in STATES.indices) {
            for (j in STATES.indices) {
                P[i][j] = 1.toDouble() / STATES.size
                F[i][j] = 0
            }
        }
    }

    fun update_frequencies(previous: Int, current: Int): Unit {
        F[previous][current] += 1
    }

    fun update_probabilities(): Unit {
        for (i in 0..P.size) {
            for (j in 0..P[i].size) {
                var total = 0.0
                for (k in 0..F[i].size) {
                    total += F[i][k]
                    if (total != 0.0) {
                        P[i][j] = F[i][k] / total
                    } else {
                        P[i][j] = 0.0
                    }
                }
            }
        }
    }

    fun generate_prediction(curr: Point): Point {
        val r = Random.nextFloat()
        val i = STATES.indexOf(curr)
        var total = 0.0
        for (j in 0..STATES.size) {
            total += P[i][j]
            if (r <= total) { return STATES[j] }
        }
        return Point(-9, -9)
    }

}