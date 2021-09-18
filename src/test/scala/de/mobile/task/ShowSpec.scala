package de.mobile.task

import java.time.Instant

import de.mobile.task.Show.syntax.ShowOps
import de.mobile.task.StarterApp.{Ads, Fraud, PerDay}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
class ShowSpec extends AnyFlatSpec with Matchers {
  val dateTimePattern: String = "dd-MM-yyyy"
  val currentTime: Instant    = Instant.now

  it should "convert ads to string" in {
    Ads(1, currentTime, 1).show shouldBe s"1,${toDateTimeString(currentTime)(dateTimePattern)}"
    Ads(2, currentTime, 1).show shouldBe s"2,${toDateTimeString(currentTime)(dateTimePattern)}"
    Ads(3, currentTime, 1).show shouldBe s"3,${toDateTimeString(currentTime)(dateTimePattern)}"
  }

  it should "convert per day info to string" in {
    val dateTime = "01-01-2016"
    val dateTime2 = "02-02-2017"
    val dateTime3 = "03-03-2018"

    PerDay(dateTime, 1.0123).show shouldBe s"$dateTime,1.01"
    PerDay(dateTime2, 2.1123).show shouldBe s"$dateTime2,2.11"
    PerDay(dateTime3, 3.2123).show shouldBe s"$dateTime3,3.21"
  }
}
