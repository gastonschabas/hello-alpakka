# Hello Alpakka

A simple example with Scala, Akka and Alpakka

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
