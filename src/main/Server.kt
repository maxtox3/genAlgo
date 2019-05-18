package main

data class Server(val id: Int, val x: Int, val y: Int) {

  fun distance(xOther: Int, yOther: Int): Int {
    //Расстояние между двумя точками находится по формуле AB = √(xb - xa)^2 + (yb - ya)^2
    return Math.sqrt(((this.x - xOther) * (this.x - xOther) + (this.y - yOther) * (this.y - yOther)).toDouble()).toInt()
  }
}
