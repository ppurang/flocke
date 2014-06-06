package org.purang.net.flocke

import org.scalacheck.Properties
import org.scalacheck.Prop._

object FlockeProperties extends Properties("Flocke"){

  val flocke = new Flocke(149890838367555l)

  import org.scalacheck._

  implicit val bigints: Gen[List[BigInt]] = for {
    i <- Gen.choose(1, 100)
  } yield (for {
      j <- 0 to i
    } yield flocke.next).toList

  property("unique") = forAll(bigints) { (a: List[BigInt]) =>
    a.length == a.distinct.length
  }

  property("sorted 2") = forAll(bigints) { (ids: List[BigInt]) =>
    ids == ids.sortWith(_ < _)
  }

}
