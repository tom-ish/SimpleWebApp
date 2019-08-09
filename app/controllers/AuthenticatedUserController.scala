package controllers

import javax.inject.Inject
import play.api.Logging
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

class AuthenticatedUserController @Inject ()
  (cc: MessagesControllerComponents, authenticatedUserAction: AuthenticatedUserAction)
  extends MessagesAbstractController(cc) with Logging
{

  def load = authenticatedUserAction {
    Ok(views.html.landing())
  }

  def logout = authenticatedUserAction {
    Redirect(routes.AuthenticationController.load())
      .flashing("info" -> "You are logged out.")
      .withNewSession
  }

}
