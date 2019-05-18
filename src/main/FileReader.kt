package main

import java.io.File
import java.io.FileNotFoundException

class FileReader(private val fileName: String) {

  fun read(): List<String>? {
    return try {
      readFileAsLinesUsingUseLines("./$fileName")
    } catch (e: FileNotFoundException) {
      println("У тебя очень большие проблемы. Где файл?")
      null
    }
  }

  private fun readFileAsLinesUsingUseLines(fileName: String): List<String> =
    File(fileName).useLines { it.toList() }

}
