package Actors

import akka.actor.{Actor, ActorRef}
import Utils.SalesInputData
import play.api.libs.json._
object SimpleActorExample {

var wsOut: ActorRef = ActorRef.noSender

  class ProductActor extends Actor {
    implicit private val SalesWrites = Json.writes[SalesInputData]
    def receive = {
      case message: SalesInputData =>{
          val json = Json.toJson(message)
          if(!wsOut.equals(ActorRef.noSender))
          wsOut ! Json.stringify(json)


      }
      case message: String => {
       // println(" Pattern Matching "+message.toString())
        if(!wsOut.equals(ActorRef.noSender))
          {
                wsOut ! message
          }
      }
      case _ =>
    }

  }



}