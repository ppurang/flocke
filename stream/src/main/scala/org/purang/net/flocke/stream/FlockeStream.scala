package org.purang.net.flocke.stream

import org.purang.net.flocke.Flocke
import scalaz._, \/._
import scalaz.concurrent.Task
import scalaz.stream.{Process => P}, P._

object FlockeStream {

  private def asyncFlockeNext(flocke: Flocke)(callback: Throwable \/ BigInt => Unit): Unit = {
    try {
      callback(right(flocke.next))
    } catch {
      case t: Throwable => callback(left(t))
    }
  }

  private def flockeProcess(implicit flocke: Flocke) = repeatEval(Task.async(asyncFlockeNext(flocke)))

  def next(n: Int)(implicit flocke: Flocke) = flockeProcess.take(n)
}
