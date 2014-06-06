package org.purang.net.flocke

//this should make a great State Monad?
// can we use nanoTime
case class Flocke(mac: Long) {

  private val macShift = 16
  private val sequenceBits = 16
  private val timestampShift = 64
  private val sequenceMask = -1L ^ (-1L << sequenceBits)

  var sequence: Long = 0
  var lastTimestamp = -1L

  def tilNextMillis(lastTimestamp: Long): Long = {
    //todo monitor
    var timestamp = timeGen
    while (timestamp <= lastTimestamp) {
      timestamp = timeGen
    }
    timestamp
  }

  def timeGen = System.currentTimeMillis()

  def next: BigInt = {
    var timestamp = timeGen

    if (timestamp < lastTimestamp) {
      //throw new Exception("Clock is moving backwards!")
      //todo monitor
      tilNextMillis(timestamp)
    }

    if (lastTimestamp == timestamp) {
      //todo monitor
      sequence = (sequence + 1) & sequenceMask
      if (sequence == 0) {
        timestamp = tilNextMillis(lastTimestamp)
      }
    } else {
      sequence = 0
    }

    lastTimestamp = timestamp

    (BigInt(timestamp) << timestampShift) + ((mac << macShift) | sequence)
  }

}


object Flocke {

  import MacAddress._

  def apply(str: String) : Either[String, Flocke] = displayName(str).right.map(Flocke(_))

  def hex(str: String) : Either[String, Flocke] = MacAddress.hex(str).right.map(Flocke(_))

  //todo monitor which one is actually chosen?
  def apply() : Flocke = optionalHardwareAddress.fold(Flocke(randSeq(15).mkString.toLong))(x => Flocke(macToLong(x)))

  private def randSeq(n: Int): Stream[Int] = Stream.continually{ scala.util.Random.nextInt(10) }.take(n)

}



