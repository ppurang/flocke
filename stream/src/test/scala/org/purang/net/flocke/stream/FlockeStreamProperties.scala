package org.purang.net.flocke.stream

import org.scalacheck.Properties
import org.scalacheck.Prop._
import java.lang.{Process => _}
import org.purang.net.flocke.Flocke

object FlockeStreamProperties extends Properties("Flocke"){

  val flocke = new Flocke(149890838367555l)

  import org.scalacheck._

  property("end of the world decides order") = forAll(for(n <- Gen.choose(1,100)) yield n) { (n: Int) =>
    val a = FlockeStream.next(n)(flocke)
    val b = FlockeStream.next(n)(flocke)
    val bs = b.runLog.run
    val as = a.runLog.run
    as.forall(a => bs.forall(b => b < a)) //the same as (bs ++ as).sortWith(_ < _) == bs ++ as
  }

}
