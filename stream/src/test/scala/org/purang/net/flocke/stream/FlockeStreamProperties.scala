package org.purang.net.flocke.stream

import org.scalacheck.Properties
import org.scalacheck.Prop._
import org.purang.net.flocke._
import scalaz.concurrent.Task
import java.lang.{Process => _}
import scalaz.stream.Process
import org.purang.net.flocke.Flocke

object FlockeStreamProperties extends Properties("Flocke"){

  val flocke = new Flocke(149890838367555l)

  import org.scalacheck._

  property("end of the world decides order") = forAll { (n: Int) =>
    val a = FlockeStream.next(n)(flocke)
    val b = FlockeStream.next(n)(flocke)
    val bs = b.runLog.run
    val as = a.runLog.run
    (bs ++ as).sortWith(_ < _) == bs ++ as

  }

}
