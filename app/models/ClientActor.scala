package models

import akka.actor.{Actor, ActorLogging, ActorRef}

class ClientActor(out: ActorRef, chat: ActorRef) extends Actor with ActorLogging {
  chat ! Join

  override def postStop(): Unit = chat ! Leave

  override def receive: Receive = {
    case text: String =>
      println("text => " + text)
      chat ! ClientSentMessage(text)
    case ClientSentMessage(text) =>
      println("msg => "+text)
      out ! text
  }
}
