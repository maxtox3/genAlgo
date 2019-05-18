package main

import java.util.*

class Path(var serversCount: Int) : Comparable<Any> {
  var path: Array<Server> = arrayOf()
    set(value) {
      field = value
      calculateCost()
    }
  var cost: Int = 0
  var fitness: Int = 0

  init {
    createPath()
    cost = 0
    calculateCost()
    fitness = 0
  }

  private fun calculateCost() {
    cost = 0
    var i = 0
    while (i < serversCount - 1) {
      cost += path[i].distance(path[i + 1].x, path[i + 1].y)
      i++
    }
    cost += path[path.size - 1].distance(path[0].x, path[0].y)
  }

  private fun createPath() {
    path = Array(serversCount) { index ->
      Server(index, randomNum(1, 99), randomNum(1, 99))
    }
  }

  private fun randomNum(min: Int, max: Int): Int {
    return min + Random().nextInt(max - min)
  }

  override operator fun compareTo(other: Any): Int {
    val tmp = other as Path
    return when {
      this.cost < tmp.cost -> 1
      this.cost > tmp.cost -> -1
      else -> 0
    }
  }
}
