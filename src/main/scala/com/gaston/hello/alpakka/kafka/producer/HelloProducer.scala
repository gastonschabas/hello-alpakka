package com.gaston.hello.alpakka.kafka.producer

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import java.time.Instant
import java.util.UUID
import scala.concurrent.Future

class HelloProducer(config: Config)(implicit val actorSystem: ActorSystem) {

  implicit val executionContext = actorSystem.dispatcher

  val logger = Logger(this.getClass.getName)

  val kafkaBoostrapServers = config.getString("kafka.bootstrap-servers")
  val topicEventHello = config.getString("topic.event.hello")

  val producerSettings: ProducerSettings[String, String] =
    ProducerSettings(actorSystem, new StringSerializer, new StringSerializer)
      .withBootstrapServers(kafkaBoostrapServers)

  def sendHello: Future[Done] =
    Source
      .single(
        new ProducerRecord[String, String](
          topicEventHello,
          UUID.randomUUID().toString,
          s"hello at ${Instant.now()}"
        )
      )
      .runWith(Producer.plainSink(producerSettings))

}
