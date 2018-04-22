
import org.scalatest.{FlatSpec, Matchers}


class KafkaSpec extends FlatSpec with Matchers{


  it should """match localhost""" in{
    val propSet = MyKafkaProducer.props.getProperty("bootstrap.servers")
    val port = propSet.split(":")(0)
    port should matchPattern{
      case "localhost" =>
    }
  }

  it should """match 9092""" in{
    val propSet = MyKafkaProducer.props.getProperty("bootstrap.servers")
    val port = propSet.split(":")(1)
    port should matchPattern{
      case "9092" =>
    }
  }

  it should """match org.apache.kafka.common.serialization.StringSerializer for key""" in{
    val prop = MyKafkaProducer.props.getProperty("key.serializer")
    prop should matchPattern{
      case "org.apache.kafka.common.serialization.StringSerializer" =>
    }
  }

  it should """match org.apache.kafka.common.serialization.StringSerializer for value""" in{
    val prop = MyKafkaProducer.props.getProperty("value.serializer")
    prop should matchPattern{
      case "org.apache.kafka.common.serialization.StringSerializer" =>
    }
  }

  it should """match CSVKafka""" in{
    val topic = MyKafkaProducer.TOPIC
    topic should matchPattern{
      case "csvTopic" =>
    }
  }

}
