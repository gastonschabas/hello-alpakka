package com.gaston.hello.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.gaston.hello.alpakka.kafka.producer.HelloProducer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

import scala.io.StdIn

object AkkaHttpServerApp extends App {

  val logger = Logger(this.getClass.getName)

  implicit val actorSystem = ActorSystem("akka-http-server-actor-system")
  implicit val executionContext = actorSystem.dispatcher

  val config = ConfigFactory.load()

  val helloProducer = new HelloProducer(config)

  val route =
    path("hello") {
      get {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            "<h1>Say hello to akka-http</h1>"
          )
        )
      } ~ post {
        onSuccess(helloProducer.sendHello) { _ =>
          complete(
            StatusCodes.Accepted,
            HttpEntity(ContentTypes.`text/plain(UTF-8)`, "hello sent")
          )
        }
      }
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  logger.info(
    s"Server now online. Please navigate to http://localhost:8080/hello"
  )
  logger.info("Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => actorSystem.terminate()) // and shutdown when done
}
