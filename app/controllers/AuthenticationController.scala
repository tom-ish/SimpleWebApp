package controllers

import javax.inject.{Inject, Singleton}
import models.{Global, LoginData, RegisterData}
import play.api.Logging
import play.api.data.Form
import play.api.mvc.{AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}

import utils.Const

@Singleton
class AuthenticationController @Inject()(cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) with Logging
{

  def load = Action { implicit request: MessagesRequest[AnyContent] =>
    val usernameOption = request.session.get(Const.USERNAME)
    usernameOption.map { username =>
      Ok(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString))
    }.getOrElse(Ok(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString)))
  }

  def loadAuthenticationForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.authentication(LoginData.loginForm, RegisterData.registerForm))
  }

  def validateLoginForm = Action { implicit request: MessagesRequest[AnyContent] =>
    logger.info("validateLoginForm click")

    def failure = { formWithErrors : Form[LoginData] =>
        logger.warn(s"form error: $formWithErrors")
        BadRequest(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString))
    }

    // form validation/binding failed...
    def success = { loginForm : LoginData =>
    //    request.session + (models.Global.SESSION_USERNAME_KEY, "dummy")
//        Redirect(routes.AuthenticatedController.load())
//          .flashing("info" -> "You are logged in.")
    //      .withSession(Global.SESSION_USERNAME_KEY -> "dummy")
      Redirect(routes.AuthenticatedController.load())
    }

    LoginData.loginForm.bindFromRequest.fold( failure, success)
  }

  def validateRegisterForm = Action { implicit request: MessagesRequest[AnyContent] =>
    logger.info("validateRegisterForm click")
//    request.session + (models.Global.SESSION_USERNAME_KEY, "dummy")
    Redirect(routes.AuthenticationController.load)
      .flashing("info" -> "You are registered in.")
//      .withSession(Global.SESSION_USERNAME_KEY -> "dummy")
  }



}
