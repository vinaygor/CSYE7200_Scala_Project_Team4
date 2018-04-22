package Utils

import Actors.SimpleActorExample.ProductActor
import akka.actor.{ActorSystem, Props}
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import play.api.libs.json.Json

import scala.collection.mutable.ListBuffer

object ApplicationUtils {


  /**
    * Creating a actorSystem
    */
  val actorSystem = ActorSystem("SimpleSystem")


  /**
    * Creating a ProductActor
    */
  val productActor = actorSystem.actorOf(Props[ProductActor], "ProductActor")



  /**
    * Defining Spark Streaming Context. 1 instance of streaming context per application.
    * Fetching the data every 2 seconds
    */
  val ssc = new StreamingContext(new SparkConf().setAppName("MySparkStreaming").setMaster("local[2]"), Seconds(2))

  /**
    * Setting the TOPIC name to which the Kakfa producer is publishing the contents
    */
  val TOPIC = "csvTopic"

  /**
    * Setting the properties for the map
    */
  val props = Map(
    "bootstrap.servers" -> "localhost:9092",
    "metadata.broker.list" -> "localhost:9092",
    "serializer.class" -> "kafka.serializer.StringEncoder",
    "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
  )


  /**
    * Method to create Kakfa Stream
    * @param ssc
    * @param kafkaTopics
    * @param brokers
    * @return DStream[(String,String)]
    */
  def createKafkaStream(ssc: StreamingContext, kafkaTopics: String, brokers: String): DStream[(String, String)] = {
    val topicsSet = kafkaTopics.split(",").toSet
    KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, props, topicsSet)
  }

  /**
    * Method to clean the data, remove the unwanted blank/white spaces and replace them with 'EMPTY'
    * @param inputRow: Raw input data
    * @return Cleaned data without any empty spaces
    */
  def cleanDataFunction(inputRow:String) = {

    val dirtyRow = inputRow.replaceAll(",,", ",EMPTY,")

    val cleanedRow = if (dirtyRow.charAt(dirtyRow.length() - 1) == ',') dirtyRow + "EMPTY" else dirtyRow

    cleanedRow
  }

  /**
    * Caching the userIDs which are received by streams
    */
  val userIDBufferList:  ListBuffer[(Int,String)]  = ListBuffer()

  /**
    * Caching the product which are received by streams
    */
  val productBufferList:  ListBuffer[(String,String)]  = ListBuffer()

  /**
    * Caching the details of Products sold per category per city
    */
  val productCategoryBufferList: ListBuffer[(String,String)] =  ListBuffer()


  /**
    * Caching the details of purchases made by each group sold per category per city
    */
  val ageGenderPurchasesBufferList: ListBuffer[(String,String)] =  ListBuffer()



  /**
    * Caching the details of purchases made by each group sold per category per city
    */
  val ageBufferList: ListBuffer[(String,String)] =  ListBuffer()


  /**
    * Method to send data to web socket Actor in JSON format
    * @param data
    */
  def sendProductsDataToActor(data:ListBuffer[(String,String)]) : String = {

    val dataInMap = data.groupBy(identity).mapValues(_.size)
    val json = Json.toJson(dataInMap)
    productActor ! Json.stringify(json)
    Json.stringify(json)
  }


  /**
    * Method to send data to web socket Actor in JSON format
    * @param data
    */
  def sendUsersDataToActor(data:ListBuffer[(Int,String)]) : String = {

    val dataInMap = data.groupBy(identity).mapValues(_.size)
    /**
      * Taking top 10 users from the map
      * 1. converting map to Seq
      * 2. sorting in reverse order by value using (-)
      * 3. Converting back to map
      * 4. taking top 10
      */
    val top10 = dataInMap.toSeq.sortBy(- _._2).toMap.take(10)
    val json = Json.toJson(top10)
    productActor ! Json.stringify(json)
    Json.stringify(json)
  }


}
