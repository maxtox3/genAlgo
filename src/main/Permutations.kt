package main

import java.util.*

class Permutations {
  var cost = 999999
  var best: List<Server> = listOf()


  fun permutations(list: List<Server>) {
    permutations(null, list, null)
  }

  private fun permutations(
    prefix: List<Server>?,
    suffix: List<Server>,
    output: List<ArrayList<Server>>?
  ): List<Server>? {
    var prefix = prefix
    var output = output
    if (prefix == null)
      prefix = ArrayList()
    if (output == null)
      output = ArrayList()
    if (suffix.size == 1) {
      val newElement = ArrayList(prefix)
      newElement.addAll(suffix)
      val costNow = cost(newElement)
      if (costNow < this.cost) {
        best = newElement
        this.cost = costNow
      }
      return best
    }
    for (i in suffix.indices) {
      val newPrefix = ArrayList(prefix)
      newPrefix.add(suffix[i])
      val newSuffix = ArrayList(suffix)
      newSuffix.removeAt(i)
      permutations(newPrefix, newSuffix, output)
    }


    return best
  }

  private fun cost(path: ArrayList<Server>): Int {
    var cost = 0
    var i = 0
    while (i < path.size - 1) {
      cost += path[i].distance(path[i + 1].x, path[i + 1].y)
      i++
    }
    cost += path[path.size - 1].distance(path[0].x, path[0].y)
    return cost
  }

}
