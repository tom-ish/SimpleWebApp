package controllers

import javax.inject.{Inject, Singleton}
import models.{Global, LoginData, RegisterData}
import play.api.Logging
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.filters.csrf.CSRF
import utils.Const

@Singleton
class AuthenticationController @Inject()(cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) with Logging with I18nSupport
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
    def failure = { formWithErrors : Form[LoginData] =>
        logger.warn(s"form error: ${formWithErrors}")
        BadRequest(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString))
    }
    def success = { loginForm : LoginData =>
      Redirect(routes.AuthenticatedUserController.load())
        .flashing("info" -> "You are logged in.")
        .withSession(
          Global.SESSION_USERNAME_KEY -> loginForm.email,
          Global.SESSION_EXPIRATION_DATE -> (System.currentTimeMillis() + Const.SESSION_DURATION.toMillis).toString,
          "csrfToken" -> CSRF.getToken.get.value)
    }
    LoginData.loginForm.bindFromRequest.fold(failure, success)
  }

  def validateRegisterForm = Action { implicit request: MessagesRequest[AnyContent] =>
    def failure = { formWithErrors : Form[RegisterData] =>
      logger.warn(s"register form error: $formWithErrors")
      BadRequest(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString))
    }
    def success = { registerData : RegisterData =>
      Redirect(routes.AuthenticatedUserController.load)
        .flashing("info" -> "You are registered in.")
        .withSession(
          Global.SESSION_USERNAME_KEY -> registerData.email,
          Global.SESSION_EXPIRATION_DATE -> (System.currentTimeMillis() + Const.SESSION_DURATION.toMillis).toString,
          "csrfToken" -> CSRF.getToken.get.value)
    }
    RegisterData.registerForm.bindFromRequest.fold(failure,success)
  }



}