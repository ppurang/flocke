package org.purang.net.flocke.server

import org.purang.net.flocke.Flocke

import java.util.concurrent.Executors

import com.typesafe.scalalogging.slf4j.StrictLogging
import org.http4s.Request
import org.http4s.dsl._
import org.http4s.server.HttpService
import org.http4s.server.blaze.BlazeServer

class FlockeServer(host: String, port: Int) extends StrictLogging {

  private val pool = Executors.newCachedThreadPool()

  implicit val flocke  = Flocke()
  val base64 = new FlockeTextBase64Route().service
  val plain = new FlockeTextPlainRoute().service

  val service: HttpService =  { case req: Request =>
    val uri = req.uri.path
    if (uri.endsWith("html")) {
      logger.info(s"${req.remoteAddr.getOrElse("null")} -> ${req.method}: ${req.uri.path}")
    }

    base64 orElse plain applyOrElse (req, {_: Request => NotFound(req.uri.path)})
  }

  def run(): Unit = BlazeServer.newBuilder
      .withHost(host)
      .withPort(port)
      .mountService(service, "")
      .run()
}

object FlockeServer extends StrictLogging {
  val ip = Option(System.getenv("HOST")).getOrElse("0.0.0.0")
  val port = (Option(System.getenv("PORT")) orElse
              Option(System.getenv("HTTP_PORT")))
          .map(_.toInt)
          .getOrElse(8080)

  logger.info(s"Starting Http4s-blaze example on '$ip:$port'")
  println(s"Starting Http4s-blaze example on '$ip:$port'")

  def main(args: Array[String]): Unit = new FlockeServer(ip, port).run()
}

