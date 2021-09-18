package de.mobile.task

import de.mobile.task.StarterApp.{Ads, Fraud}
import de.mobile.task.syntax.StringOps

import scala.io.Source
import scala.reflect.ClassTag

/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
trait DataSource {
  def getAds: Stream[Ads]
  def getFrauds: Stream[Fraud]
}

final class FileDataSource (adsResourceName: String, fraudResourceName: String) extends DataSource {
  override def getAds: Stream[Ads]      = fromResourceName(adsResourceName).map(_.toAds)

  override def getFrauds: Stream[Fraud] = fromResourceName(fraudResourceName).map(_.toFraud)

  private def fromResourceName(name: String): Stream[String] =
    Source.fromResource(name).getLines.toStream

  private implicit def collect[T](s: Stream[Either[Throwable, T]])(implicit C: ClassTag[T]): Stream[T] =
    s.map {
    case Left(err)     =>
      println(s"ERROR ${C.getClass.getName} [${err.getLocalizedMessage}]")
      None
    case Right(result) => Some(result)
  }.collect { case Some(r) => r }
}

object DataSource {
  def file(adsResourceName: String, fraudResourceName: String) = new FileDataSource(adsResourceName, fraudResourceName)
}
