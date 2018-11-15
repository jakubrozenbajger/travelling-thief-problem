package cf.jrozen.mh.ttp.slover

import com.google.common.math.Stats

object CsvFormat {

  def format(map: Map[String, Stats]): List[String] = {
    "name,count,min,mean,max,popStdEev" :: map.toList.sortBy(_._1).map({
      case (name, s) => name + "," + asCsv(s.count(), s.min(), s.mean(), s.max(), s.populationStandardDeviation())
    })
  }

  def asCsv(obj: Number*) = {
    obj.seq.map(_.toString).mkString(",")
  }

}
