# Hello Alpakka <!-- omit in TOC -->
A simple example with Scala, Akka and Alpakka

# Table Of Content <!-- omit in TOC -->

- [Requirements](#requirements)
- [Description](#description)
- [How the project is built](#how-the-project-is-built)
  - [Stack](#stack)
  - [Akka HTTP Server With Producer](#akka-http-server-with-producer)
  - [Consumer](#consumer)
- [Running locally](#running-locally)
  - [Start a kafka cluster using `docker-compose`](#start-a-kafka-cluster-using-docker-compose)
  - [Start the consumer](#start-the-consumer)
  - [Start the akka http server](#start-the-akka-http-server)
  - [Send `POST` request to `http://localhost:8080/hello`](#send-post-request-to-httplocalhost8080hello)
  - [Check the logs of the consumer app](#check-the-logs-of-the-consumer-app)

# Requirements

- [docker](https://www.docker.com/) 20.10.12
- [docker-compose](https://docs.docker.com/compose/) 1.25.5
- [jdk](https://adoptopenjdk.net/) 11
- [scala](https://www.scala-lang.org/) 2.13.8
- [sbt](https://www.scala-sbt.org/) 1.6.2

# Description

There are two apps.
- **Akka HTTP Server**: it starts a http server exposing and endpoint `/hello`. Sending a GET it will return a hello message. Sending a POST with an empty body, a kafka message will be published in the `event.hello` topic.
- **Consumer**: it starts a kafka consumer that subscribes to the `event.hello` topic

# How the project is built

## Stack

- [scala](https://www.scala-lang.org/) 2.13.8
- [Alpakka](https://doc.akka.io/docs/alpakka/current/index.html) 3.0.0

## Akka HTTP Server With Producer

Main class for starting the http server is located in `com.gaston.hello.akka.http.AkkaHttpServerApp`. It doesn't
contain any complex logic. It just starts the `HTTP Server` exposing the `/hello` endpoint for `GET` and `POST`
methods. The logic for `POST` method calls to the producer and this one publish a message in a kafka topic. The
Producer has the logic to send the message.

## Consumer

Main class for starting the consumer is located in `com.gaston.hello.alpakka.kafka.consumer.ConsumerApp`. It creates a
kafka consumer that subscribes to the `event.hello` topic, which is where the producer is going to be sending messages.
Once the message is received, this one is logged and that's all.

# Running locally

## Start a kafka cluster using `docker-compose`

```shell
docker-compose up -d
```
This command will start two containers
- **zookeper**: port `22181` is exposed
- **kafka**: port `29092` is exposed

## Start the consumer

```shell
sbt "runMain com.gaston.hello.alpakka.kafka.consumer.ConsumerApp"
```
Once the consumer starts it will try to subscribe to `event.hello` topic

## Start the akka http server

```shell
sbt "runMain com.gaston.hello.akka.http.AkkaHttpServerApp"
```
Once the http server is started a http `GET` or `POST` request can be sent to `http://localhost:8080/hello`. Sending a `GET` will
return a hello message. Sending a `POST` will publish a message in `event.hello` topic and returns a hello message.

## Send `POST` request to `http://localhost:8080/hello`

```shell
curl -d "" http://localhost:8080/hello
```
With this command a `http POST request` is sent to the `akka http server`. A message is published in `event.hello`
topic and a hello message is returned.

## Check the logs of the consumer app

Once the `POST request` is sent, a log message should be shown like the following one
```log
[INFO ] 2022-03-06 02:22:10,399 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [6f6f930e-dd3a-416b-b506-c3ac0cf8f569], value: [hello at 2022-03-06T05:22:09.872594Z] 
[INFO ] 2022-03-06 02:22:31,257 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [42bce1e8-1da6-4fa3-956d-d5c0669b3a91], value: [hello at 2022-03-06T05:22:31.103983Z] 
[INFO ] 2022-03-06 02:22:31,920 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [7c48bd51-1cee-46df-b5ab-9aeb55b58499], value: [hello at 2022-03-06T05:22:31.831066Z] 
[INFO ] 2022-03-06 02:22:32,317 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-4 - received - key: [489f8b91-2c3b-4880-bd4c-a8dc6eb37413], value: [hello at 2022-03-06T05:22:32.225990Z] 
[INFO ] 2022-03-06 02:22:32,697 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [7e743f01-71cf-4298-8099-e717c042b5e7], value: [hello at 2022-03-06T05:22:32.539574Z] 
[INFO ] 2022-03-06 02:22:32,957 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [15d2ecfa-bbd4-41ee-a7b9-30ad7413a590], value: [hello at 2022-03-06T05:22:32.817235Z] 
[INFO ] 2022-03-06 02:22:33,217 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [54e16bb3-07a7-4935-952c-c61e2030b813], value: [hello at 2022-03-06T05:22:33.059850Z] 
[INFO ] 2022-03-06 02:22:33,357 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [1628b127-fc37-4c22-97bc-29953151deb2], value: [hello at 2022-03-06T05:22:33.273488Z] 
[INFO ] 2022-03-06 02:22:33,617 - com.gaston.hello.alpakka.kafka.consumer.ConsumerApp$ - default-akka.actor.default-dispatcher-5 - received - key: [6342f7d2-9040-4c67-b30e-9940a4e92132], value: [hello at 2022-03-06T05:22:33.520176Z]  
```
