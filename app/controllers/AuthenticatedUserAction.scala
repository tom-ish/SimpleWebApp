package controllers

import java.util.Date

import javax.inject.Inject
import models.Global
import play.api.i18n.MessagesApi
import play.api.mvc.{BodyParsers, MessagesActionBuilderImpl, MessagesRequest, Request, Result}
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedUserAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext, messagesApi: MessagesApi)
  extends MessagesActionBuilderImpl(parser, messagesApi) {

  override def invokeBlock[A](request: Request[A], block: MessagesRequest[A] => Future[Result]): Future[Result] = {
    request.session.get(Global.SESSION_USERNAME_KEY) match {
        case None =>
          Future.successful(Redirect(routes.AuthenticationController.loadAuthenticationForm())
            .flashing("info" -> "You are logged out.")
            .withNewSession)
        case Some(user) => {
          request.session.get(Global.SESSION_EXPIRATION_DATE) match {
            case Some(expirationDate : String) =>
              if(System.currentTimeMillis() < new Date(expirationDate.toLong).getTime)
                block(new MessagesRequest[A](request, messagesApi))
              else
                Future.successful(Redirect(routes.AuthenticationController.loadAuthenticationForm())
                  .flashing("info" -> "You are logged out.")
                  .withNewSession)
            case None =>
              Future.successful(Redirect(routes.AuthenticationController.loadAuthenticationForm())
                .flashing("info" -> "You are logged out.")
                .withNewSession)
          }
        }
      }
    }
}
