package de.mobile.task

import java.io.PrintWriter
/**
 * @author Mikhail Nemenko {@literal <nemenkoma@gmail.com>}
 */
trait Sink {
  def write(value: String): Unit
}

object Sink {
  def file(filename: String): Sink = new FileSink(filename)
  def console: Sink = new StdoutSink()
}

final class FileSink(fileName: String) extends Sink {
  override def write(value: String): Unit = {
    val wr = new PrintWriter(fileName)
    try {
      wr.write(value)
      wr.flush()
    } finally wr.close()
  }
}

final class StdoutSink extends Sink {
  override def write(value:  String): Unit = println(value)
}