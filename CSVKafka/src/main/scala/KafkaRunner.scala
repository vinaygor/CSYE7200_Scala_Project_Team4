import org.apache.kafka.clients.producer.{ProducerRecord}

import java.util.{Random}

object KafkaRunner extends App {

    val bufferedSource = io.Source.fromFile("D:\\Semester-Spring-18\\Scala\\Project Stuff\\Data\\train.csv")
    for (line <- bufferedSource.getLines) {

      println("--------------------------")
      println(line);

      MyKafkaProducer.producer.send(new ProducerRecord[String,Any](MyKafkaProducer.TOPIC,line))
      Thread.sleep(new Random().nextInt(2000)+1000)

    }
    bufferedSource.close

  MyKafkaProducer.producer.close()
}
