package org.purang.net.flocke.server

import org.http4s.dsl._
import org.http4s._
import org.http4s.Status._

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.http4s.dsl./
import org.http4s.Header.`Content-Type`
import org.http4s.util.jodaTime.UnixEpoch
import java.lang.{Process => _}
import scalaz.stream.Process
import concurrent.duration._
import scala.util.Try
import org.purang.net.flocke.stream.FlockeStream
import org.purang.net.flocke.{Base64Url, Flocke}
import argonaut.EncodeJson
import org.http4s.Header.`Content-Type`._
import org.http4s.dsl./
import scodec.bits.ByteVector
import scalaz.concurrent.Task
import scodec.bits.ByteVector


class FlockeTextBase64Route(implicit val flocke: Flocke) extends LazyLogging {

  private implicit def TWritable(implicit charset: CharacterSet = CharacterSet.`UTF-8`) =
    new SimpleWritable[IndexedSeq[BigInt]] {
      def contentType: `Content-Type` = `Content-Type`(MediaType.fromKey("text", "base64")).withCharset(charset)

      def asChunk(t: IndexedSeq[BigInt])  = {
        val base64EncodedbigInts: IndexedSeq[String] = t.map((x:BigInt) => Base64Url.encode(x.toString().getBytes(charset.charset)))
        ByteVector.view(base64EncodedbigInts.mkString(",").getBytes(charset.charset))
      }
    }


  def isTextBase64(r: Request) : Boolean = {
    r.headers.get(Header.Accept).map((h: Header.Accept.HeaderT) => h.values.list.count((v: MediaRange) => v.toString() contains "text/base64") > 0).fold(false)(x => x)
  }

  val service: HttpService = {

    case r @ Get -> Root / "flocke" if isTextBase64(r)  => Ok(FlockeStream.next(1).runLog)

    case r @ Get -> Root / "flocke" / n if isTextBase64(r)  => Try(n.toInt).toOption.fold(BadRequest(s"SomethingWrong with $n"))(x => Ok(FlockeStream.next(x).runLog))

  }

}




class FlockeTextPlainRoute(implicit val flocke: Flocke) extends LazyLogging {

  implicit def TWritable(implicit charset: CharacterSet = CharacterSet.`UTF-8`) =
    new SimpleWritable[IndexedSeq[BigInt]] {
      def contentType: `Content-Type` = `text/plain`.withCharset(charset)

      def asChunk(t: IndexedSeq[BigInt]) = ByteVector.view(t.mkString(",").getBytes(charset.charset))
    }

  val service: HttpService = {

    case Get -> Root / "flocke" => Ok(FlockeStream.next(1).runLog)

    case Get -> Root / "flocke" / n => Try(n.toInt).toOption.fold(BadRequest(s"SomethingWrong with $n"))(x => Ok(FlockeStream.next(x).runLog))

  }

}





