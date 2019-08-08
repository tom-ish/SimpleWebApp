package controllers

import javax.inject.Inject
import play.api.Logging
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

class AuthenticatedController @Inject ()(cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) with Logging
{

  def load = Action {
    Ok(views.html.landing())
  }

  def logout = Action {
    Redirect(routes.AuthenticationController.load()).withNewSession
  }

}
