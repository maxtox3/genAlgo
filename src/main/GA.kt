package main

import java.util.*

object GA {

  private var scanner = Scanner(System.`in`)

  @JvmStatic
  fun main(args: Array<String>) {

    //время выполнения
    val start = System.nanoTime()
    //размер популяции
    var populationSize = 30
    //кол-во серверов
    var numServers = 15
    //коэффициент мутации
    var mutationRate = 0.05
    //коэффициент кроссовера
    var crossoverRate = 0.8
    //кол-во поколениц
    val numberOfGenerations = 1000
    //стоп условие
    val stopAt = 20000
    //популяция
    val pop: Population

    val fileName = "Data2.txt"

    val useGenAlgo = readWorkType()
    if (useGenAlgo) {
      val metaData = readData(fileName) ?: return
      numServers = metaData.initialServers.size
      crossoverRate = metaData.crossoverRate
      mutationRate = metaData.mutationRate
      populationSize = numServers
      val p = Path(numServers)
      p.path = metaData.initialServers
      pop = Population(
        populationSize,
        numServers,
        p,
        crossoverRate,
        mutationRate
      )
      runGenAlgo(pop, numberOfGenerations, stopAt)
    } else {
      runBruteForce(fileName)
    }

    val elapsedTime = System.nanoTime() - start
    println("Алгоритм занял: $elapsedTime нано секунд, чтобы найти решение")
  }

  private fun runGenAlgo(
    pop: Population,
    numberOfGenerations: Int,
    stopAt: Int
  ) {
    //Сортируем популяцию Finess / Evaluating
    var numberOfGenerations1 = numberOfGenerations
    pop.fitnessOrder()

    //Выводим каждый путь ID/стоимость/фитнесс/номер сервера (с координатами)
    printFullPath(pop)

    //Начинаем поиск возможного решения
    while (numberOfGenerations1 != stopAt) {

      //Выборка / кроссовер
      while (pop.mate().not());
      //Mutate
      for (i in 0 until pop.nextGen.size) {
        pop.nextGen[i].path = pop.mutation(pop.nextGen[i].path)

      }

      //Устанавливаем новое поколение в текущее
      pop.population = pop.nextGen
      pop.done = 0
      //Сортируем новую популяцию по фитнес/ оценки
      pop.fitnessOrder()
      //инкрементируем количество генераций
      numberOfGenerations1++
    }


    //Выводим каждое значение фитнесс функции для каждого пути
    var valor = 0.0
    for (i in 0 until pop.population.size) {
      valor += pop.population[i].fitness.toDouble()
      println("Значение фитнесс функции: " + pop.population[i].fitness + "%")
    }
    println("")
    println("Полное значение фитнесс функции: $valor%")

    println("\n-----------------------------------------------")


    //Выводим каждый путь ID/стоимость/фитнесс/номер сервера (с координатами)
    printFullPath(pop)
  }

  private fun printFullPath(pop: Population) {
    for (i in 0 until pop.population.size) {
      println(
        "Путь ID: $i | стоимость: " + pop.population[i].cost + " | значение фитнесс функции: " + pop.population[i].fitness + "%"
      )
      print("Путь: ")
      for (j in 0 until pop.population[i].path.size) {
        print(
          pop.population[i].path[j].id.toString() + "(" + pop
            .population[i].path[j].x + "," + pop.population[i]
            .path[j].y + ")  "
        )

      }
      println("\n -----------------------------------------------------")
    }
  }

  private fun runBruteForce(fileName: String) {
    val cities = readCities(fileName)
    if (cities != null) {
      val permu = Permutations()
      permu.permutations(cities.toList())
      print("Самый короткий путь: ")
      for (i in 0 until permu.best.size) {
        print(
          permu.best[i].id.toString() + "(" + permu.best[i]
            .x + "," + permu.best[i].y + ")"
        )
      }
      println("")
      println("Стоимость: " + permu.cost)
    }
  }

  private fun readData(fileName: String): AppModel? {
    val serverCount = readInt("Введите кол-во городов: ")
    val mutationRate = readDouble("Введите процент мутации: ")
    val crossoverRate = readDouble("Введите процент кроссовера: ")
    val cities = readCities(fileName) ?: return null
    return AppModel(
      serverCount,
      mutationRate,
      crossoverRate,
      cities
    )
  }

  private fun readCities(fileName: String): Array<Server>? {
    val fileReader = FileReader(fileName)
    val lines = fileReader.read()
    if (lines.isNullOrEmpty()) {
      return null
    }
    val delimiter = "|"
    val equalDelimeter = "="
    return Array(lines.size) { arrayIndex ->
      val s: String? = lines[arrayIndex]
      val st = StringTokenizer(s, delimiter)
      st.nextToken()
      val serverName = st.nextToken()
      var stmp = StringTokenizer(serverName, equalDelimeter)
      stmp.nextToken()
      val x = Integer.parseInt(stmp.nextToken())
      val l = st.nextToken()
      stmp = StringTokenizer(l, equalDelimeter)
      stmp.nextToken()
      val y = Integer.parseInt(stmp.nextToken())
      Server(arrayIndex, x, y)
    }
  }

  private fun readInt(textToPresent: String): Int {
    print(textToPresent)
    val result: Int? = scanner.nextLine().toIntOrNull()
    if (result == null) {
      println("У тебя проблемы")
      readInt(textToPresent)
    } else {
      return result
    }
    return 0
  }

  private fun readDouble(textToPresent: String): Double {
    print(textToPresent)
    val result: Double? = scanner.nextLine().toDoubleOrNull()
    if (result == null) {
      println("У тебя проблемы")
      readDouble(textToPresent)
    } else {
      return result
    }
    return 0.0
  }

  private fun readWorkType(): Boolean {
    print("Использовать генетический алгоритм? Y/N: ")
    val result = scanner.nextLine()
    return when (result.toLowerCase()) {
      "n" -> false
      "y" -> true
      else -> false
    }
  }
}



