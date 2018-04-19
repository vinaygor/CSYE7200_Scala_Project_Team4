package controllers

import java.lang.ProcessBuilder.Redirect
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject._

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.protobuf.ByteString
import akka.stream.Materializer
import akka.stream.scaladsl.{Source, StreamConverters}
import play.api.mvc._
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import play.api.http.ContentTypes
import play.api.libs.{Comet, EventSource}
import play.twirl.api.Html
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import play.api.http.ContentTypes
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.Comet
import play.api.libs.json._
import play.api.libs.streams._
import Actors.SimpleActorExample.ProductActor

import scala.concurrent.duration._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */


@Singleton
class HomeController @Inject()(implicit system: ActorSystem, materializer: Materializer, cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */


  val ssc = new StreamingContext(new SparkConf().setAppName("MySparkStreaming").setMaster("local[2]"), Seconds(2))

  def index = Action {


    //Kafka Streaming

    def createKafkaStream(ssc: StreamingContext, kafkaTopics: String, brokers: String): DStream[(String, String)] = {
      val topicsSet = kafkaTopics.split(",").toSet
      val props = Map(
        "bootstrap.servers" -> "localhost:9092",
        "metadata.broker.list" -> "localhost:9092",
        "serializer.class" -> "kafka.serializer.StringEncoder",
        "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
        "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
        "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
        "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
      )
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, props, topicsSet)
    }

    val TOPIC = "csvTopic"

    val topicStream = createKafkaStream(ssc, TOPIC, "localhost:9092").map(_._2)

    val eachRow = topicStream.foreachRDD {
      rdd =>
        rdd.foreach {
          row => {
            val dirtyRow = row.replaceAll(",,", ",EMPTY,")

            val cleanedRow = if (dirtyRow.charAt(dirtyRow.length() - 1) == ',') dirtyRow + "EMPTY" else dirtyRow

            val input = cleanedRow.split(",")

            import Utils.SalesInputData
            //print(Some(SalesInputData(input(0).toInt,input(1),input(2),input(3),input(4),input(5),input(6),input(7),input(8),input(9),input(10))))
            val inputInCaseClass = SalesInputData(input(0).toInt, input(1), input(2), input(3), input(4), input(5), input(6), input(7), input(8), input(9), input(10))
            //println(inputInCaseClass)
            val actSystem = ActorSystem("SimpleSystem")

            val actor = actSystem.actorOf(Props[ProductActor], "ProductActor")

            actor ! inputInCaseClass

          }
        }
    }


    val dataInRDD = topicStream
    Ok(Html("<h1>Welcome to your application.</h1><a href='/startStreaming'>Start Spark Streaming</a>"))

  }

  import Actors.StreamDataActor

  def streamData = WebSocket.accept[String, String] {
    request => ActorFlow.actorRef { out => StreamDataActor.props(out) }
  }

  def startStreaming = Action {
    implicit request => {
      ssc.start()
      println("Streaming Data Started!")
      Ok(views.html.index("StreamData"))
    }
  }

  def stopStreaming = Action {
    Redirect("/")
  }


}