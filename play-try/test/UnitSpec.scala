import akka.actor.ActorSystem
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import services.Counter
import Utils.ApplicationUtils._

import scala.collection.mutable.ListBuffer

/**
 * Unit tests can run without a full Play application.
 */
class UnitSpec extends PlaySpec {


  "SparkContext" should {

    "match sparkStreamingContext AppName" in {

      ssc.sparkContext.appName must equal("MySparkStreaming")

    }

    "match sparkStreamingContext current execution status" in {

      ssc.sparkContext.isStopped must equal(false)

    }

    "match sparkStreamingContext isLocal status" in {

      ssc.sparkContext.isLocal must equal(true)

    }

    "match sparkStreamingContext version" in {
      ssc.sparkContext.version must equal("2.3.0")
    }


  }

  "CleanDataFunction" should
    {
      "return a cleaned row" in {

        val answer = cleanDataFunction("1005972,P00214142,F,26-35,20,B,0,0,8,,5,5100");

        answer must equal("1005972,P00214142,F,26-35,20,B,0,0,8,EMPTY,5,5100")

      }
    }

  "Data buffers" should
    {
      "size of ProductbufferList must be 0" in {

        val bufferListSize = productBufferList.size

        bufferListSize must equal (0)

      }

      "size of productCategoryBufferList must be 0" in {

        val productCategorySize = productCategoryBufferList.size

        productCategorySize must equal (0)

      }
    }

  "Kafka Topic" should
    {
      "must match csvTopic" in {

        val answer = TOPIC

        answer must equal("csvTopic")

      }
    }

  "Kafka Parameters" should {
    "match bootstrap.servers" in {

      val bootstrapProps = props.get("bootstrap.servers")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("localhost:9092")

    }

    "match metadata.broker.list" in {

      val bootstrapProps = props.get("metadata.broker.list")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("localhost:9092")

    }

    "match serializer.class" in {

      val bootstrapProps = props.get("serializer.class")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("kafka.serializer.StringEncoder")

    }

    "match value.serializer" in {

      val bootstrapProps = props.get("value.serializer")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("org.apache.kafka.common.serialization.StringSerializer")

    }

    "match value.deserializer" in {

      val bootstrapProps = props.get("value.deserializer")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("org.apache.kafka.common.serialization.StringDeserializer")

    }

    "match key.serializer" in {

      val bootstrapProps = props.get("key.serializer")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("org.apache.kafka.common.serialization.StringSerializer")

    }

    "match key.deserializer" in {

      val bootstrapProps = props.get("key.deserializer")

      val answer = bootstrapProps match {
        case Some(bootstrapServer) => bootstrapServer
        case _ => ""
      }

      answer must equal ("org.apache.kafka.common.serialization.StringDeserializer")

    }




  }

  "Data Analytics" should {

    "Summation of userIDBufferList size 1" in {

      val answer = sendUsersDataToActor(ListBuffer((1000036,"UserID")))
        answer must equal("[[[1000036,\"UserID\"],1]]")
    }

    "Summation of userIDBufferList size greater than 1" in {

      val answer = sendUsersDataToActor(ListBuffer((1000036,"UserID"), (1000036,"UserID"), (1000036,"UserID"), (1000036,"UserID"), (1000036,"UserID"), (1000036,"UserID"), (1000036,"UserID"), (1000037,"UserID"), (1000039,"UserID"), (1000039,"UserID"), (1000041,"UserID"), (1000041,"UserID"), (1000042,"UserID"), (1000042,"UserID"), (1000042,"UserID")))
      println(answer)
      answer must equal("[[[1000042,\"UserID\"],3],[[1000041,\"UserID\"],2],[[1000036,\"UserID\"],7],[[1000039,\"UserID\"],2],[[1000037,\"UserID\"],1]]")
    }

    "Summation of productBufferList size 1" in {

      val answer = sendProductsDataToActor(ListBuffer(("P00331842","ProductID"), ("P00288642","ProductID")))
      answer must equal("[[[\"P00288642\",\"ProductID\"],1],[[\"P00331842\",\"ProductID\"],1]]")
    }

    "Summation of productBufferList size greater than 1" in {

      val answer = sendProductsDataToActor(ListBuffer(("P00331842","ProductID"),("P00331842","ProductID"),("P00331842","ProductID"),("P00331842","ProductID"),("P00288642","ProductID")))
      answer must equal("[[[\"P00288642\",\"ProductID\"],1],[[\"P00331842\",\"ProductID\"],4]]")
    }

    "Summation of productCategoryBufferList" in {

      val answer = sendProductsDataToActor(ListBuffer(("16","B"), ("16","B"), ("1","B"), ("1","B"), ("1","B")))
      answer must equal("[[[\"1\",\"B\"],3],[[\"16\",\"B\"],2]]")
    }



  }




}
