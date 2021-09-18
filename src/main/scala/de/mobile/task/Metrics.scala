package de.mobile.task

import de.mobile.task.StarterApp.{Ads, Aggregate, Fraud, PerDay}
import scala.annotation.tailrec

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
trait Metrics {
  def getAggregate: Stream[Aggregate]
  def fraudulentAdsList: Stream[Ads]
  def avgPrice(ads: Stream[Ads]): Double
  def avgPricePerDay(ads: Stream[Aggregate]): Stream[PerDay]
}

class MetricsCalculator (source: DataSource)(dateTimePattern: String) extends Metrics {
  override def fraudulentAdsList: Stream[StarterApp.Ads] = getAggregate.map(_.ads)

  override def getAggregate: Stream[Aggregate] = join(source.getAds, source.getFrauds)

  override def avgPrice(ads: Stream[Ads]): Double = {
    val (s, c) = ads.foldLeft((0.0, 0L)) {
      case ((sum, count), next) => (sum + next.price, count + 1)
    }

    if(c > 0) s / c
    else 0d
  }

  override def avgPricePerDay(ads: Stream[Aggregate]): Stream[PerDay] =
    ads.groupBy(agg => asString(agg.fraud.detectedTime, dateTimePattern)).map {
      case (date, agg) => PerDay(date, avgPrice(agg.map(_.ads)))
    }.toStream

  private def join(ads: Stream[Ads], frauds: Stream[Fraud]): Stream[Aggregate] = {
    @tailrec
    def loop(ads: Stream[Ads], frauds: Stream[Fraud])(acc: Stream[Aggregate]): Stream[Aggregate] = (ads, frauds) match {
      case (aStream @ aHead #:: aTail, fStream @ fHead #:: fTail) =>
        Ordering[Long].compare(aHead.id, fHead.adsId) match {
          case v if v < 0 => loop(aTail, fStream)(acc)
          case v if v > 0 => loop(aStream, fTail)(acc)
          case _          => loop(aTail, fTail)(Aggregate(aHead, fHead) #:: acc)
        }
      case _              => acc.reverse
    }
    loop(ads, frauds)(Stream.Empty)
  }
}

object Metrics {
  def make(s: DataSource, dtPattern: String) = new MetricsCalculator(s)(dtPattern)
}