package com.gaston.hello.alpakka.kafka.consumer

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.Future
import scala.io.StdIn

object ConsumerApp extends App {

  val logger = Logger(this.getClass.getName)

  implicit val actorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher

  val config = ConfigFactory.load()

  val kafkaBoostrapServers = config.getString("kafka.bootstrap-servers")
  val topicEventHello = config.getString("topic.event.hello")

  val consumerSettings =
    ConsumerSettings(
      actorSystem,
      new StringDeserializer,
      new StringDeserializer
    )
      .withBootstrapServers(kafkaBoostrapServers)
      .withGroupId("consumer-app")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val consumer = Consumer
    .atMostOnceSource(consumerSettings, Subscriptions.topics(topicEventHello))
    .mapAsync(1)(record =>
      Future(
        logger
          .info(s"received - key: [${record.key}], value: [${record.value()}]")
      )
        .map(_ => Done.getInstance())
    )
    .toMat(Sink.seq)(DrainingControl.apply)
    .run()

  logger.info("Consumer is started")
  logger.info("Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  consumer.drainAndShutdown()

}
