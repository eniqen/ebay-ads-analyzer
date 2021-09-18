package de.mobile.task

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
class DatasourceSpec extends AnyFlatSpec with Matchers {

  it should "read ads and fraudulent from file" in {
    val adsResourceName        = "ads_test.csv"
    val fraudulentResourceName = "fraudulent_test.csv"

    val ds         = DataSource.file(adsResourceName, fraudulentResourceName)
    val ads        = ds.getAds.toList
    val fraudulent = ds.getFrauds.toList

    ads        should have size 10
    fraudulent should have size 10
  }

  it should "skip invalid ads and fraudulent records" in {
    val adsResourceName        = "ads_test_incorrect.csv"
    val fraudulentResourceName = "fraudulent_test_incorrect.csv"

    val ds         = DataSource.file(adsResourceName, fraudulentResourceName)
    val ads        = ds.getAds.toList
    val fraudulent = ds.getFrauds.toList

    ads         should have size 7
    fraudulent should have size 4
  }
}
