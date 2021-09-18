package de.mobile.task

import de.mobile.task.Show.syntax.ShowOps
import de.mobile.task.StarterApp.{Ads, PerDay}

import scala.math.BigDecimal.RoundingMode.HALF_UP

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
trait Show[T] {
  def show(value: T): String
}

object Show {
  def apply[T](implicit ev: Show[T]): Show[T] = ev

  implicit val adsShow: Show[Ads] = Show.show[Ads] {
    ads => s"${ads.id}$separator${toDateTimeString(ads.createdAt)(dateTimePattern)}"
  }

  implicit val perDayShow: Show[PerDay] = Show.show[PerDay] {
    p => s"${p.dateTime}$separator${p.avgPrice.show}"
  }

  implicit def listShow[T: Show]: Show[List[T]] = Show.show(_.map(Show[T].show(_)).mkString("\n"))

  implicit val doubleShow: Show[Double] = Show.show(v => BigDecimal(v).setScale(2, HALF_UP).toDouble.toString)

  def show[T](fn: T => String): Show[T] = (value: T) => fn(value)

  object syntax {
    implicit final class ShowOps[T](private val value: T) extends AnyVal {
      def show(implicit T: Show[T]): String = T.show(value)
    }
  }
}