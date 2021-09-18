package de.mobile.task

import java.util.UUID

import de.mobile.task.StarterApp.PerDay
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
class MetricsSpec extends AnyFlatSpec with Matchers {

  val dateTimePattern = "dd-MM-yyyy"

  it should "join ads with fraudulent records" in {
    val adsResourceName        = "ads_test.csv"
    val fraudulentResourceName = "fraudulent_test.csv"

    val ds          = DataSource.file(adsResourceName, fraudulentResourceName)
    val calculator  = Metrics.make(ds, dateTimePattern)
    val ads         = ds.getAds
    val fraudulent  = ds.getFrauds

    val result = calculator.getAggregate

    ads         should have size 10
    fraudulent  should have size 10
    result      should have size 4
  }

  it should "skip fraudulent records with duplicated ids" in {
    val adsResourceName        = "ads_test.csv"
    val fraudulentResourceName = "fraudulent_test_duplicates.csv"

    val ds          = DataSource.file(adsResourceName, fraudulentResourceName)
    val calculator  = Metrics.make(ds, dateTimePattern)
    val ads         = ds.getAds
    val fraudulent  = ds.getFrauds

    val result = calculator.getAggregate

    val duplicatesCount = fraudulent.groupBy(_.adsId).foldLeft(0){
      case (acc, (_, v)) => acc + v.size - 1
    }

    ads         should have size 10
    fraudulent  should have size 10

    (fraudulent.map(_.adsId).toSet diff ads.map(_.id).toSet) should have size (fraudulent.size - result.size - duplicatesCount)
    result          should have size 3
    result.forall(agg => agg.ads.id == agg.fraud.adsId) shouldBe true
    duplicatesCount shouldBe 5
  }

  it should "return fraudulent ads list" in {
    val adsResourceName        = "ads_test.csv"
    val fraudulentResourceName = "fraudulent_test.csv"

    val ds          = DataSource.file(adsResourceName, fraudulentResourceName)
    val calculator  = Metrics.make(ds, dateTimePattern)
    val ads         = ds.getAds
    val fraudulent  = ds.getFrauds

    val result        = calculator.getAggregate
    val fraudulentAds = calculator.fraudulentAdsList

    ads         should have size 10
    fraudulent  should have size 10
    result      should have size 4

    result.map(_.ads) should contain theSameElementsAs fraudulentAds
  }

  it should "calculate average price for all fraudulent ads" in {
    val adsResourceName        = "ads_test.csv"
    val fraudulentResourceName = "fraudulent_test.csv"

    val ds          = DataSource.file(adsResourceName, fraudulentResourceName)
    val calculator  = Metrics.make(ds, dateTimePattern)
    val ads         = ds.getAds
    val fraudulent  = ds.getFrauds

    val fraudulentAds = calculator.fraudulentAdsList
    val avgPrice      = calculator.avgPrice(fraudulentAds)

    ads           should have size 10
    fraudulent    should have size 10
    fraudulentAds should have size 4

    avgPrice shouldBe 7734.75
  }

  it should "calculate average price per day for all fraudulent ads" in {
    val adsResourceName        = "ads_test.csv"
    val fraudulentResourceName = "fraudulent_test.csv"

    val ds          = DataSource.file(adsResourceName, fraudulentResourceName)
    val calculator  = Metrics.make(ds, dateTimePattern)
    val ads         = ds.getAds
    val fraudulent  = ds.getFrauds
    val expected    = List(
      PerDay("02-07-2016",14490.0),
      PerDay("03-07-2016",4374.5),
      PerDay("04-07-2016",7700.0)
    )

    val fraudulentAds  = calculator.getAggregate
    val avgPricePerDay = calculator.avgPricePerDay(fraudulentAds)

    ads           should have size 10
    fraudulent    should have size 10
    fraudulentAds should have size 4

    avgPricePerDay should contain theSameElementsAs expected
  }
}
