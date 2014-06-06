package org.purang.net.flocke

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import Base64Url._

object Base64UrlProperties  extends Properties("Base64Url"){

  property("url safe alphabet") = forAll { (a: String) =>
    encode(a.getBytes("utf8")).forall(encodeTable.contains(_))
  }

}

import org.scalatest.FunSuite
import org.scalatest.Matchers

class Base64UrlTest extends FunSuite with Matchers {
  val data = Map("1" -> "MQ", "11" -> "MTE", "2" -> "Mg",
    "some random text Really?!" -> "c29tZSByYW5kb20gdGV4dCBSZWFsbHk_IQ",
    "data to be encoded" -> "ZGF0YSB0byBiZSBlbmNvZGVk",
    "jdsjhdsjhd jhjah991829182 19289182 9182 19289182 19 jjsdjshdksjd k jskjkjdkjddksh skdjkajd" -> "amRzamhkc2poZCBqaGphaDk5MTgyOTE4MiAxOTI4OTE4MiA5MTgyIDE5Mjg5MTgyIDE5IGpqc2Rqc2hka3NqZCBrIGpza2pramRramRka3NoIHNrZGprYWpk")
  test("encode correctly") {
    for((k,v) <- data) {
      encode(k.getBytes("utf8")) should be(v)
    }
  }
}
