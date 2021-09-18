package de.mobile.task

import java.time.{Instant, LocalDate}

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
object StarterApp {
  final case class Ads(id: Long, createdAt: Instant, price: Long)
  final case class Fraud(adsId: Long, detectedTime: LocalDate)
  final case class Aggregate(ads: Ads, fraud: Fraud)
  final case class PerDay(dateTime: String, avgPrice: Double)

  val adsPath   = "ads.csv"
  val fraudPath = "fraudulent.csv"

  def main(args: Array[String]): Unit = {

    import Show.syntax._

    val source             = DataSource.file(adsPath, fraudPath)
    val calculator         = Metrics.make(source, dateTimePattern)
    val adsSink            = Sink.file("fraudulent_ads.csv")
    val avgPricePerDaySync = Sink.file("avg_price_daily.csv")
    val consoleSink        = Sink.console

    val aggregate = calculator.getAggregate
    val frList    = calculator.fraudulentAdsList

    println("###1 Fraudulent Ads List")
    adsSink.write(frList.toList.show)
    println("###2 Avg price for fraudulent ads list")
    consoleSink.write(List(calculator.avgPrice(frList)).show)
    println("###3 Avg price per day for fraudulent ads list")
    avgPricePerDaySync.write(calculator.avgPricePerDay(aggregate).toList.sortBy(_.dateTime).show)

    /*
          Task-4
          To sort this problem on single machine with exceeding RAM size we have to prepare our files to be sorted
          For example we can use merge sort algorithm on unix machine
          sort --parallel=3 -uo ads.csv sorted_ads.csv
          sort --parallel=3 -uo fraudulent.csv sorted_fraudulent.csv

          after that it is enough just to have two pointers anc compare ads_id and fraudulent_ads_id using merge sort strategy
          to understand where we are and what should we do next
          in case we would like to skip duplicates on fraudulent file with the same id
          we compare ads_id and fraudulent_ids_id

          if ads_id < fraudulent_ads_id then we have to skip current ads and take next row in ads file
          if ads_id > fraudulent_ads_id then we did not find any matches with fraudulent_ads_id and
                      we have to skip current fraudulent and take next row fraudulent in file
          if ads_id = fraudulent_ids_id we found matches and we have to add this info into result and switch both pointers next

     */
  }
}
