package Actors

import play.api.Logger
import akka.actor.{Actor, ActorSystem, Props, ActorRef}
import play.api.libs.json.Json
import Actors.SimpleActorExample.wsOut


class StreamDataActor(out:ActorRef) extends Actor {

  def receive = {
    case "streaming" =>
      wsOut = out
      Logger.info("Received a message")
  }
}

object StreamDataActor {
  def props(out: ActorRef) = Props(new StreamDataActor(out))
}