package de.mobile

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneId}

import de.mobile.task.StarterApp.{Ads, Fraud}

import scala.util.Try

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
package object task {

  val dateTimePattern = "dd-MM-yyyy"
  val separator: String = ","

  def toDateTimeString(i: Instant)(pattern: String): String =
    asString(i.atZone(ZoneId.systemDefault()).toLocalDate, pattern)

  def asString(ld: LocalDate, pattern: String): String =
    ld.format(DateTimeFormatter.ofPattern(pattern))

  object syntax {
    implicit final class StringOps(val value: String) extends AnyVal {
      def toAds: Either[Throwable, Ads]     =
        value.split(separator) match {
          case Array(id, t, p) =>
            for {
              id    <- id.asNumber
              time  <- t.asInstant
              price <- p.asNumber
            } yield Ads(id, time, price)
          case _ => Left(new IllegalArgumentException(s"Incorrect row values=[$value]"))
        }

      def toFraud: Either[Throwable, Fraud] = value.split(separator) match {
        case Array(id, t) =>
          for {
            id   <- id.asNumber
            time <- t.asLocalDate(dateTimePattern)
          } yield Fraud(id, time)
        case _            => Left(new IllegalArgumentException(s"Incorrect row values=[$value]"))
      }

      def asInstant: Either[Throwable, Instant] =
        Try(Instant.ofEpochMilli(value.toLong)).toEither
      def asNumber: Either[Throwable, Long] = Try(value.toLong).toEither
      def asLocalDate(pattern: String): Either[Throwable, LocalDate] =
        Try(LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern))).toEither
    }
  }
}
