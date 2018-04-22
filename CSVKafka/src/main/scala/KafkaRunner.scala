import org.apache.kafka.clients.producer.{ProducerRecord}



object KafkaRunner extends App {

    val bufferedSource = io.Source.fromFile("data/data.csv")
    for (line <- bufferedSource.getLines) {

      println("--------------------------")
      println(line);

      MyKafkaProducer.producer.send(new ProducerRecord[String,Any](MyKafkaProducer.TOPIC,line))
      Thread.sleep(2000)

    }
    bufferedSource.close

  MyKafkaProducer.producer.close()
}
