package controllers

import entities.{LoginData, RegisterData}
import javax.inject.{Inject, Singleton}
import models.Global
import play.api.{Logger, Logging}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.filters.csrf.CSRF
import services.AccountService
import utils.{Const, StatusCodes}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class AuthenticationController @Inject()(cc: MessagesControllerComponents, accountService: AccountService)
                                        (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) with I18nSupport
{
  val logger: Logger = Logger(this.getClass())

  def load = Action { implicit request: MessagesRequest[AnyContent] =>
    val usernameOption = request.session.get(Const.USERNAME)
    usernameOption.map { username =>
      Ok(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString))
    }.getOrElse(Ok(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString)))
  }

  def loadAuthenticationForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.authentication(LoginData.loginForm, RegisterData.registerForm))
  }

  def wrongAuthentication(msg: String) =
    Redirect(routes.AuthenticationController.loadAuthenticationForm().toString) flashing ("wrong authentication"-> msg)

  def validateLoginForm = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def failure = { formWithErrors : Form[LoginData] =>
        logger.warn(s"form error: ${formWithErrors}")
        Future.successful(wrongAuthentication("validateLoginForm"))
    }
    def success = { loginForm : LoginData =>
      val status = accountService.attemptLogin(loginForm)
      status map {
        case (StatusCodes.OK, msg) =>
          Redirect(routes.AuthenticatedUserController.load())
            .flashing("info" -> "You are logged in.")
            .withSession(
              Global.SESSION_USERNAME_KEY -> loginForm.email,
              Global.SESSION_EXPIRATION_DATE -> (System.currentTimeMillis() + Const.SESSION_DURATION.toMillis).toString,
              "csrfToken" -> CSRF.getToken.get.value)
        case (error, msg) =>
          wrongAuthentication(s"Login - $error\n - $msg")
      }
    }
    LoginData.loginForm.bindFromRequest.fold(failure, success)
  }

  def validateRegisterForm = Action.async { implicit request: MessagesRequest[AnyContent] =>
    def failure = { formWithErrors : Form[RegisterData] =>
      logger.warn(s"register form error: $formWithErrors")
      Future.successful(wrongAuthentication("validateRegisterForm"))
    }
    def success = { registerData : RegisterData =>
      logger.info("register form success entered")
      val status = accountService.addAccount(registerData.username, registerData.email, registerData.password)
      status map {
        case StatusCodes.OK =>
          Redirect(routes.AuthenticatedUserController.load)
            .flashing("info" -> "You are registered in.")
            .withSession(
              Global.SESSION_USERNAME_KEY -> registerData.email,
              Global.SESSION_EXPIRATION_DATE -> (System.currentTimeMillis() + Const.SESSION_DURATION.toMillis).toString,
              "csrfToken" -> CSRF.getToken.get.value)

        case error =>
          wrongAuthentication(s"Register - $error")
      }
    }
    RegisterData.registerForm.bindFromRequest.fold(failure,success)
  }



}
