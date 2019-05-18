package main

import java.util.*

class Population(
  private val populationSize: Int,
  private val serversCount: Int,
  private val path: Path,
  private val crossoverRage: Double,
  private val mutationRate: Double
) {
  var population: Array<Path> = arrayOf()
  var crossoverRate: Double = crossoverRage
  private var child1: Array<Server> = arrayOf()
  private var child2: Array<Server> = arrayOf()
  var nextGen: Array<Path> = arrayOf()
  var done: Int = 0

  init {
    population = Array(populationSize) {
      val tmpServer = mutableListOf<Server>()
      for (j in 0 until serversCount) {
        tmpServer.add(path.path[j])
      }
      tmpServer.shuffle()
      val tmpPath = Path(serversCount)
      tmpPath.path = tmpServer.toTypedArray()
      tmpPath
    }
    this.nextGen = Array(populationSize) { index ->
      Path(
        serversCount
      )
    }
    done = 0
  }

  private fun selectParent(): Int {
    //Выбираем предков
    val totalCost = calculateTotalFitness()

    val fit = randomNum(0, totalCost)
    var value = 0
    for (i in population.indices) {
      value += population[i].fitness
      if (fit <= value) {
        return i
      }
    }
    return -1

  }

  fun mate(): Boolean {
    //Генерация случайного числа, чтобы проверить, пересекаются ли родители
    val check = randomNum(0, 100)
    val parent1 = selectParent()
    var parent2 = selectParent()
    while (parent1 == parent2) {
      parent2 = selectParent()
    }

    //проверяем, будет ли кроссовер
    if (check <= crossoverRate * 100) {

      val crossoverPoint = randomNum(0, population[parent1].path.size - 1)
      val init: (Int) -> Server = {
        Server(it, 0, 0)
      }
      child1 = Array(serversCount, init)
      child2 = Array(serversCount, init)

      //производим кроссовер
      for (i in 0 until crossoverPoint) {
        child1[i] = population[parent2].path[i]
        child2[i] = population[parent1].path[i]
      }
      for (i in crossoverPoint until serversCount) {
        child1[i] = population[parent1].path[i]
        child2[i] = population[parent2].path[i]
      }


      //Перестановка детей с учетом повторения сервера
      var serverChild1: Int
      var serverChild2: Int
      val list1 = ArrayList<Int>()
      val list2 = ArrayList<Int>()

      for (i in 0 until crossoverPoint) {
        serverChild1 = child1[i].id
        serverChild2 = child2[i].id

        //Получаем позиции повторяющихся серверов
        for (j in crossoverPoint until serversCount) {
          if (serverChild1 == child1[j].id) {
            list1.add(j)
          }
          if (serverChild2 == child2[j].id) {
            list2.add(j)
          }
        }
      }

      //Ищем пропущенные значения
      for (i in 0 until serversCount) {
        var found = false
        //Исправляем первого детеныша
        for (j in 0 until serversCount) {
          if (population[parent2].path[i] == child1[j]) {
            found = true
            break
          }
        }
        if (!found) {
          child1[list1.removeAt(list1.size - 1)] = population[parent2].path[i]
        }
        found = false
        //Исправляем второго детеныша
        for (j in 0 until serversCount) {
          if (population[parent1].path[i] == child2[j]) {
            found = true
            break
          }
        }
        if (!found) {
          child2[list2.removeAt(list2.size - 1)] = population[parent1].path[i]
        }
      }
      return addToGenerationCheckFull(child1, child2)

    }//кроссовера не будет, берем родичей
    else {
      return addToGenerationCheckFull(
        population[parent1].path,
        population[parent1].path
      )
    }
  }

  private fun addToGenerationCheckFull(child1: Array<Server>, child2: Array<Server>): Boolean {
    if (done == populationSize) {
      return true
    }
    val newGenChild1 = Path(serversCount)
    val newGenChild2 = Path(serversCount)
    newGenChild1.path = child1
    newGenChild2.path = child2
    return when {
      done < populationSize - 2 -> {
        this.nextGen[done] = newGenChild1
        this.nextGen[done + 1] = newGenChild2
        this.done += 2
        false
      }
      done == populationSize - 2 -> {
        this.nextGen[done] = newGenChild1
        this.nextGen[done + 1] = newGenChild2
        done += 2
        true
      }
      else -> {
        this.nextGen[done] = newGenChild1
        done += 1
        true
      }
    }

  }

  //Используем рулетку
  fun mutation(child: Array<Server>): Array<Server> {
    val check = randomNum(0, 100)

    //Проверяем, будет ли мутация
    if (check <= mutationRate * 100) {

      //ищем 2 сервера для мутации
      val point1 = randomNum(0, serversCount - 1)
      var point2 = randomNum(0, serversCount - 1)
      while (point2 == point1) {
        point2 = randomNum(0, serversCount - 1)
      }

      //Сервера меняются (switch) в результате мутации
      val server1 = child[point1]
      val server2 = child[point2]
      child[point1] = server2
      child[point2] = server1

    }
    return child
  }

  private fun randomNum(min: Int, max: Int): Int {
    return min + Random().nextInt(max - min)
  }

  fun fitnessOrder() {
    Arrays.sort(population)
    for (i in population.indices) {
      val lol = 100000 / (population[i].cost + 1)
      population[i].fitness = lol
    }
  }

  private fun calculateTotalFitness(): Int {
    var cost = 0
    for (i in population.indices) {
      cost += population[i].fitness
    }
    return cost
  }
}
