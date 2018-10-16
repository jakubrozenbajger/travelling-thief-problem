package cf.jrozen.mh.ttp.utils

import cf.jrozen.mh.ttp.model.{Item, Node, Problem}

import scala.io.Source

object Loader {

  private val DIR_NAME = "fixtures/"
  private val FILE_EXT = ".ttp"
  private val REGEX = ":"

  def load(filename: String): Problem = {

    val scanner: Iterator[String] = Source.fromInputStream(inputStream(filename)).getLines()

    val problemName = nextValue(scanner)
    val knapsackDataType = nextValue(scanner)
    val dimension = nextValue(scanner).toInt
    val numberOfItems = nextValue(scanner).toInt
    val capacityOfKnapsack = nextValue(scanner).toInt
    val minSpeed = nextValue(scanner).toDouble
    val maxSpeed = nextValue(scanner).toDouble
    val rentingRatio = nextValue(scanner).toDouble
    val edgeWeightType = nextValue(scanner)

    scanner.next()

    val nodes = scanner.takeWhile(notContainsItemsSection).map(nodeFromLine).toArray
    val sections = scanner.map(itemFromLine).toArray

    Problem(problemName, knapsackDataType, dimension, numberOfItems, capacityOfKnapsack, minSpeed, maxSpeed, rentingRatio, edgeWeightType, nodes, sections)
  }

  val notContainsItemsSection: String => Boolean = str => !(str contains "ITEMS SECTION")

  private def inputStream(filename: String) =
    classOf[Problem].getClassLoader.getResourceAsStream(DIR_NAME + filename + FILE_EXT)


  private def nextValue(scanner: Iterator[String]) = scanner.next().split(REGEX)(1).trim

  private def nodeFromLine(line: String) = {
    val values = line.split("\t").map(_.trim.toDouble)
    Node(values(0).toInt, values(1), values(2))
  }

  private def itemFromLine(line: String) = {
    val values = line.split("\t").map(_.trim.toDouble)
    Item(values(0).toInt, values(1), values(2), values(3).toInt)
  }

}
