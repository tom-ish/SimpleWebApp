package models

import akka.actor.{Actor, ActorLogging, ActorRef}
import play.api.Logging

case object Join
case object Leave
final case class ClientSentMessage(text: String)

class Chat extends Actor with Logging {
  override def receive: Receive = process(Set.empty)

  def process(subscribers: Set[ActorRef]): Receive = {
    case Join => context become process(subscribers + sender)
    case Leave => context become process(subscribers - sender)
    case msg: ClientSentMessage =>
      println("===> "+msg)
      (subscribers).foreach(_ ! msg)
  }

}