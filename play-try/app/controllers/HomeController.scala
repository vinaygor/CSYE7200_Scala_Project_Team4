  package controllers
  import javax.inject._
  import akka.actor.{ActorSystem}
  import play.api.mvc._
  import play.twirl.api.Html
  import akka.stream.Materializer
  import play.api.libs.streams._
  import Utils.ApplicationUtils._
  import Utils._
  import Actors.StreamDataActor


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




    def index = Action {

      val topicStream = createKafkaStream(ssc, TOPIC, "localhost:9092").map(_._2)

      val eachRow = topicStream.foreachRDD {
        rdd =>
          rdd.foreach {
            row => {

              val cleanedRow = cleanDataFunction(row)

              val input = cleanedRow.split(",")

              implicit def stringtoInt(s:String): Int = augmentString(s).toInt

              val inputInCaseClass = SalesInputData(input(0), input(1), input(2), input(3), input(4), input(5), input(6), input(7), input(8), input(9), input(10), input(11))

              val ProductCategoryCitySalesInputDataCaseClass = ProductCategoryCitySalesInputData(input(8),input(5))

              val AgeGroupPurchasesSalesInputDataCaseClass = AgeGroupPurchasesSalesInputData(input(3),input(11))

              val productId = inputInCaseClass match {
                case SalesInputData(_, product_id: String, _,_,_,_,_,_,_,_,_,_) =>
                  product_id
              }

              val category = ProductCategoryCitySalesInputDataCaseClass match {
                case ProductCategoryCitySalesInputData(product_category1,_) =>
                  product_category1
              }

              val city = ProductCategoryCitySalesInputDataCaseClass match {
                case ProductCategoryCitySalesInputData(_,city) =>
                  city
              }

              val ageGroup = AgeGroupPurchasesSalesInputDataCaseClass match {
                case AgeGroupPurchasesSalesInputData(age,_) => age
              }

              val purchaseAmount = AgeGroupPurchasesSalesInputDataCaseClass match {
                case AgeGroupPurchasesSalesInputData(_,purchaseAmount) => purchaseAmount
              }

              val userId = inputInCaseClass match {
                case SalesInputData(user_id: Int, _,_,_,_,_,_,_,_,_,_,_) =>
                  user_id
              }

              productBufferList += ((productId,"ProductID"))

              productCategoryBufferList +=((category,city))

              ageBufferList += ((ageGroup,"AgeGroup"))

              userIDBufferList += ((userId,"UserID"))
              sendUsersDataToActor(userIDBufferList)

              //Sending age data to actor
              sendProductsDataToActor(ageBufferList);

              if(productBufferList.length%2==0) {
                sendProductsDataToActor(productBufferList)
              }

              if(productCategoryBufferList.length%5==0) {
                sendProductsDataToActor(productCategoryBufferList)
              }

            //  actor ! inputInCaseClass

            }
          }
      }


      val dataInRDD = topicStream
      Ok(Html("<h1>Welcome to your application.</h1><a href='/startStreaming'>Start Spark Streaming</a>"))

    }



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