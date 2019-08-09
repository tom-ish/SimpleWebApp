package controllers

import javax.inject.Inject
import models.Global
import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedUserAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    request.session.get(Global.SESSION_USERNAME_KEY) match {
        case None => Future.successful(Forbidden("Dude, you're not logged in"))
        case Some(user) => {
          request.session.get(Global.SESSION_EXPIRATION_DATE) match {
            case Some(expirationDate : String) =>
              if(System.currentTimeMillis() < expirationDate.toLong)
                block(request)
              else
                Future.successful(Unauthorized("Dude, your session has expired"))
          }
        }
      }
    }
}
