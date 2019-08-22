package controllers

import javax.inject.Inject
import models.Global
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest, Request}
import utils.Tools

import scala.concurrent.ExecutionContext

class AuthenticatedUserController @Inject ()
  (cc: MessagesControllerComponents, authenticatedUserAction: AuthenticatedUserAction)
  (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) with I18nSupport with Logging
{

  def load = authenticatedUserAction { implicit request : MessagesRequest[AnyContent] =>
    Ok(views.html.landing(
      routes.AuthenticatedUserController.dropZone().toString,
      routes.AuthenticatedUserController.chatroom().toString)
    )
  }

  def logout = authenticatedUserAction {
    Redirect(routes.AuthenticationController.load())
      .flashing("info" -> "You are logged out.")
      .withNewSession
  }

  def dropZone = authenticatedUserAction { implicit request =>
    val expiration = request.session.get(Global.SESSION_EXPIRATION_DATE).get
    Ok(views.html.dropZone(s"[ ${request.session.get(Global.SESSION_USERNAME_KEY).get} ] : expired at " +
      Tools.prettyPrintDate(expiration)))
  }

  def chatroom = authenticatedUserAction { implicit request =>
    Ok(views.html.chatroom())
//    Ok(views.html.chatroom())
  }

}
