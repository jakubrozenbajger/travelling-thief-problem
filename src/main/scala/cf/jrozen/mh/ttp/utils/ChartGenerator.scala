package cf.jrozen.mh.ttp.utils

import com.google.common.math.Stats
import org.knowm.xchart.style.Styler
import org.knowm.xchart.{SwingWrapper, XYChartBuilder}

class ChartGenerator(
                      chartName: String = "chart" + hashCode(),
                      xTitle: String = "x",
                      yTitle: String = "y"
                    ) {


  def show(stats: java.lang.Iterable[Stats]) = {
    val chart = new XYChartBuilder()
      .width(800).height(600)
      .title(chartName)
      .xAxisTitle(xTitle)
      .yAxisTitle(yTitle)
      .build

    chart.getStyler.setLegendPosition(Styler.LegendPosition.InsideNE)
    chart.getStyler.setAxisTitlesVisible(true)

    import scala.collection.JavaConversions._
    val statsArray = stats.toArray
    val xValues = Array.range(0, statsArray.length).map(_.doubleValue)

    val transposed: Array[Array[Double]] = statsArray
      .map(s => Array(s.min(), s.mean(), s.max()))
      .transpose

    val minSeries = transposed(0)
    val avgSeries = transposed(1)
    val maxSeries = transposed(2)

    chart.addSeries("min", minSeries)
    chart.addSeries("avg", avgSeries)
    chart.addSeries("max", maxSeries)

    new SwingWrapper(chart).displayChart()
  }

}
