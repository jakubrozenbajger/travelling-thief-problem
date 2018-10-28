package cf.jrozen.mh.ttp.utils

import com.google.common.math.Stats
import org.knowm.xchart.style.Styler
import org.knowm.xchart.{SwingWrapper, XYChartBuilder}

class ChartGenerator(
                      chartName: String = "chart" + hashCode(),
                      xTitle: String = "x",
                      yTitle: String = "y",
                      params: Any
                    ) {


  def show(stats: java.lang.Iterable[Stats]) = {
    val chart = new XYChartBuilder()
      .width(800).height(800)
      .title(s"$chartName (${params.toString})")
      .xAxisTitle(xTitle)
      .yAxisTitle(yTitle)
      .build

    chart.getStyler.setLegendPosition(Styler.LegendPosition.InsideSE)
    chart.getStyler.setAxisTitlesVisible(true)

    import scala.collection.JavaConverters._
    val statsArray = stats.asScala.toArray
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

  def show(stats: (Array[Double], Array[Double])) = {
    val chart = new XYChartBuilder()
      .width(800).height(800)
      .title(s"$chartName (${params.toString})")
      .xAxisTitle(xTitle)
      .yAxisTitle(yTitle)
      .build

    chart.getStyler.setLegendPosition(Styler.LegendPosition.InsideSE)
    chart.getStyler.setAxisTitlesVisible(true)

    chart.addSeries("value best", stats._1)
    chart.addSeries("valueSnd", stats._2)

    new SwingWrapper(chart).displayChart()
  }

  def show(stats: Array[Double]) = {
    val chart = new XYChartBuilder()
      .width(800).height(800)
      .title(s"$chartName (${params.toString})")
      .xAxisTitle(xTitle)
      .yAxisTitle(yTitle)
      .build

    chart.getStyler.setLegendPosition(Styler.LegendPosition.InsideSE)
    chart.getStyler.setAxisTitlesVisible(true)

    chart.addSeries("value best", stats)

    new SwingWrapper(chart).displayChart()
  }

}
