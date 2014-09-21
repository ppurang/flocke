package org.purang.net.flocke.server

import util.Try
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.http4s._
import org.http4s.dsl._
import Header.`Content-Type`
import Header.`Content-Type`.`text/plain`
import org.http4s.server.HttpService

import org.purang.net.flocke.stream.FlockeStream
import java.lang.{Process => _}
import org.purang.net.flocke.{Base64Url, Flocke}
import scodec.bits.ByteVector

class FlockeTextBase64Route(implicit val flocke: Flocke) extends LazyLogging {

  implicit def TWritable(implicit charset: Charset = Charset.`UTF-8`) = {
    def go(t: IndexedSeq[BigInt]) = {
      val base64EncodedBigInts: IndexedSeq[String] = t.map((x:BigInt) => Base64Url.encode(x.toString().getBytes(charset.nioCharset)))
      ByteVector.view(base64EncodedBigInts.mkString(",").getBytes(charset.nioCharset))
    }
    Writable.simple(go, Headers(`Content-Type`(MediaType.fromKey("text", "base64")).withCharset(charset).withCharset(charset)))
  }

  def isTextBase64(r: Request) : Boolean = {
    r.headers.get(Header.Accept).map((h: Header.Accept.HeaderT) => h.values.list.count((v: MediaRange) => v.toString() contains "text/base64") > 0).fold(false)(x => x)
  }

  val service: HttpService = {

    case r @ GET -> Root / "flocke" if isTextBase64(r)  => Ok(FlockeStream.next(1).runLog)

    case r @ GET -> Root / "flocke" / n if isTextBase64(r)  => Try(n.toInt).toOption.fold(BadRequest(s"SomethingWrong with $n"))(x => Ok(FlockeStream.next(x).runLog))

  }

}

class FlockeTextPlainRoute(implicit val flocke: Flocke) extends LazyLogging {

  implicit def TWritable(implicit charset: Charset = Charset.`UTF-8`) = {
    def go(t: IndexedSeq[BigInt]) = ByteVector.view(t.mkString(",").getBytes(charset.nioCharset))
    Writable.simple(go, Headers(`text/plain`.withCharset(charset)))
  }

  val service: HttpService = {

    case GET -> Root / "flocke" => Ok(FlockeStream.next(1).runLog)

    case GET -> Root / "flocke" / n => Try(n.toInt).toOption.fold(BadRequest(s"SomethingWrong with $n"))(x => Ok(FlockeStream.next(x).runLog))

  }

}
