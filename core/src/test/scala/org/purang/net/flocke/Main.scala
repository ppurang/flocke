package org.purang.net.flocke

object Main extends App {

  //provide a network interface display name for the hardware address
  val eth0 : Either[String, Flocke] = Flocke("wlan0")

  //provide a network interface hex code for the hardware address
  val hex: Either[String, Flocke]= Flocke.hex("54:42:49:97:a7:56")

  //the following chooses a mac address or if none is found then a random 15 digit long
  val chooseOne: Flocke = Flocke()

  //Or if you are feeling lucky
  val provideALong: Flocke = Flocke(123789345876230l)

  chooseOne.next

}
