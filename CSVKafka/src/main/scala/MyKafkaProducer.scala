import java.util.{Properties}

import org.apache.kafka.clients.producer.{KafkaProducer}

object MyKafkaProducer {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("client.id", "ScalaProducerExample")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String,Any](props)

  val TOPIC = "csvTopic"


}
