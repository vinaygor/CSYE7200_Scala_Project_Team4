package Actors

import play.api.Logger
import akka.actor.{Actor, Props, ActorRef}

import Actors.SimpleActorExample.wsOut


class StreamDataActor(out:ActorRef) extends Actor {
  wsOut = out
  def receive = {
    case "streaming" =>
      Logger.info("Received a message")
  }
}

object StreamDataActor {
  def props(out: ActorRef) = Props(new StreamDataActor(out))
}