package controllers

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import javax.inject.Inject
import models.{Chat, ClientActor}
import play.api.Logging
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, RequestHeader, WebSocket}

import scala.concurrent.ExecutionContext

class ChatroomController @Inject()
(controllerComponents: ControllerComponents, authenticatedUserAction: AuthenticatedUserAction)
(implicit actorSystem: ActorSystem, materializer: Materializer, executionContext: ExecutionContext)
extends AbstractController(controllerComponents) with Logging
{
  val chat = actorSystem.actorOf(Props[Chat], "ChatActor")

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(ref => Props(new ClientActor(ref, chat)))
  }


  def chatroom = authenticatedUserAction { implicit request =>
    Ok(views.html.chatroom())
    //    Ok(views.html.chatroom())
  }

}
