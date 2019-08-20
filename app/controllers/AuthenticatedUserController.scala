package controllers

import javax.inject.Inject
import models.Global
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest, Request}

import scala.concurrent.ExecutionContext

class AuthenticatedUserController @Inject ()
  (cc: MessagesControllerComponents, authenticatedUserAction: AuthenticatedUserAction)
  (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) with I18nSupport with Logging
{

  def load = authenticatedUserAction { implicit request : Request[AnyContent] =>
    Ok(views.html.landing(s"[ ${request.session.get(Global.SESSION_USERNAME_KEY).get} ] : expired at " +
      s"${request.session.get(Global.SESSION_EXPIRATION_DATE).get}"))
  }

  def logout = authenticatedUserAction {
    Redirect(routes.AuthenticationController.load())
      .flashing("info" -> "You are logged out.")
      .withNewSession
  }

}
