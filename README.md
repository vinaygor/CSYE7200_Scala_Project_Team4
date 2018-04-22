[![Build Status](https://travis-ci.org/akshaysjk/CSYE7200_Scala_Project_Team4.svg?branch=master)](https://travis-ci.org/akshaysjk/CSYE7200_Scala_Project_Team4)  [![Codacy Badge](https://api.codacy.com/project/badge/Grade/9f6ffa2abde74ab38a5c4ed58e3fc205)](https://www.codacy.com/app/akshaysjk/CSYE7200_Scala_Project_Team4?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=akshaysjk/CSYE7200_Scala_Project_Team4&amp;utm_campaign=Badge_Grade)

# CSYE7200_Scala_Project_Team4
This is the CSYE7200 Big Data Systems Engineering Using Scala Final Project for Team 4 Spring 2018

Team Members:

Akshay Jain [jain.aksha@husky.neu.edu](jain.aksha@husky.neu.edu)

Vinay Gor [gor.v@husky.neu.edu](gor.v@husky.neu.edu)

# Building Real-Time Analytics Dashboard Using Apache Spark

# Abstract
Generally in an ecommerce platform, the analysis of the sales of products or service happens with the help of a job which is scheduled to execute or run after a given interval of time. In situations which require immediate/real-time actions such as credit card fraud, this model won’t be suitable and will not provide accurate solution.To overcome the drawbacks of this model, we have proposed a real-time analytics model using Stream Analytics.In this project we will create a Real-time dashboard for an ecommerce platform. Dashboard will help to see hoe the sales go on a particular day across different locations. Warehouse and in ventory management at peak locations can be handled gracefully based on real-time analysis.

# Methodology
1. Data is read from a csv file in batches to simulate real-time scenario.
2. Apache Kafka is used to read the data from csv in batches. Apache Kafka provides fast data streaming, scalability and durability.
3. Kafka Producer creates data stream in batches which are consumed by the Spark Streaming context.
4. Spark Streaming is responsible for data cleaning for each of the RDD rows comsumed from the Kafka Dstream.
5. The data is cleaned and filtered according to the features required for analysis.
6. Play Framework and Highcharts are used to display the Analytics data.
7. Plays's Web socket is used which allows two way full duplex communication. Web socket requests for the streamed data.
8. The streamed data from Spark Streaming is filtered, collected and passed to the web socket using the Akka Actor System continuously.
9. Web socket ccollects the data at the front end and passes the data to the Highcharts to display analytics graphs.
10. We can enter a specific ProdcutId and see how the products sales go on every second. 

# Steps to run the project on the local machine
1. Install ```java 1.8 version``` on your machine if not installed.
2. Instal ```sbt 1.1.1 version``` on your machine.
3. Installing Kafka and Zookeeper and runnig on 
## Installing and running steps on mac
   -  Download kafka by downloading the confluent package through this link [Download confluent](https://www.confluent.io/download/) 
   -  Start Zookeeper. Since this is a long-running service, you should run it in its own terminal.
   -  ``` $ ./bin/zookeeper-server-start ./etc/kafka/zookeeper.properties ```
   -  Start Kafka, also in its own terminal.
   -  ``` $ ./bin/kafka-server-start ./etc/kafka/server.properties ```

## Installing and running steps on WINDOWS
   - Install the Apache Kafka ```kafka_2.12-1.0.1 version ``` server using this link [Download kafka](https://medium.com/@shaaslam/installing-apache-kafka-on-windows-495f6f2fd3c8)  
   - Install Zookeeper ``` zookeeper-3.4.10 ``` server using this link [Download zookeeper](https://medium.com/@shaaslam/installing-apache-zookeeper-on-windows-45eda303e835) 
4. Download the project repository on your local machine.
5. Start the Zookeeper followed by Apache Kafka server.
5. Run the CSVKafka project from your local machine using command ```sbt run```
6. Next run the second project play-try using the command ```sbt run```
7. Open the browser and browse ```http://localhost:9000``` and start the stream data, Real-time Analytics of sales can be seen.

# Continuous Integration
This project is using Travis CI as the continuous integration tool
[![Build Status](https://travis-ci.org/akshaysjk/CSYE7200_Scala_Project_Team4.svg?branch=master)](https://travis-ci.org/akshaysjk/CSYE7200_Scala_Project_Team4)
 

