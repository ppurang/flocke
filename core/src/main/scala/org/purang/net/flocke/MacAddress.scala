package org.purang.net.flocke

import java.net.{NetworkInterface => JNI, InterfaceAddress => JIA}
import java.util
import scala.util.Try

case class NetworkInterface(index: Int, dname: String, hwa: Array[Byte], ias: util.List[JIA]) {

  override def toString: String = s"""NIC[Index: $index, Display Name: $dname, Hardware Address: ${Try(hwa.mkString(",")).toOption}, Interface Addresses: $ias]"""

}

object NetworkInterface {
  def apply(n: JNI): NetworkInterface =
    NetworkInterface(n.getIndex, n.getDisplayName, n.getHardwareAddress, n.getInterfaceAddresses)
}

object MacAddress {

  def hex(address: String): Either[String, Long] = {
    val sep : Either[String, String]= if(address.contains(":")) Right(":") else if(address.contains("-")) Right("-") else Left("No separator found")
    sep.fold(Left(_), {
      x => {
        address.split(x) match {
          case r @ Array(a,b,c,d,e,f) => try {
            Right(macToLong(r.map(Integer.parseInt(_, 16).toByte)))
          } catch {
            case t: Throwable => Left(s"Failed parsing the hex strings after splitting with '$x' and parts '${r.mkString(",")}'. Root cause: ${t.getMessage}")
          }
          case l => Left(s"Expected 6 groups after splitting with '$x' but found ${l.length}: '${l.mkString(",")}'")
        }
      }
    })
  }
  
  def displayName(interface: String): Either[String, Long] = try {
    Right(macToLong(JNI.getByName(interface).getHardwareAddress))
  } catch {
    case e: Throwable =>
      import scala.collection.convert.wrapAsScala._
      Left( s"""Please check if the supplied interface: $interface is correct and available. Detected: ${(for (i <- JNI.getNetworkInterfaces) yield NetworkInterface(i)).toList.mkString(", ")}""")
  }

  private [flocke] def macToLong(mac : Array[Byte]) : Long = {
    val length: Int = mac.length
    (for (i <- 0 to length - 1) yield (mac(i).toLong & 0xff) << (length - i.toLong - 1L) * 8l).foldLeft(0l)(_ | _)
  }

  private [flocke] def optionalHardwareAddress : Option[Array[Byte]] = {
    import scala.collection.convert.wrapAsScala._
    JNI.getNetworkInterfaces.filter(x => {
      val y = x.getHardwareAddress; y != null && y.length > 0
    }).toList match {
      case x :: l => Some(x.getHardwareAddress)
      case Nil => None
    }
  }
}
