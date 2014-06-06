package org.purang.net.flocke.stream


object Main extends App {

  import org.purang.net.flocke.Flocke
  import org.purang.net.flocke.stream.FlockeStream._
  import scalaz._, Scalaz._

  Flocke.hex("54:42:49:97:a7:56").fold(
    s => println("can't do uch without shiny new ids"),
    implicit f => {
      //might need 100 sometime soon
      val n = next(100)

      //ok now's the right time
      val ids: IndexedSeq[BigInt] = n.runLog.run

      //do something useful with the shiny new ids
      println(ids) //ok kinda lame
    }
  )

}
