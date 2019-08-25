package controllers

import java.util.Date

import entities.{LoginData, RegisterData}
import javax.inject.{Inject, Singleton}
import models.Global
import play.api.{Logger, Logging}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.Results.Redirect
import play.api.mvc.{AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest, Results}
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
    val resultOption = for {
      expirationDate <- request.session.get(Global.SESSION_EXPIRATION_DATE)
    } yield {
      if (System.currentTimeMillis() < new Date(expirationDate.toLong).getTime)
        Redirect(routes.AuthenticatedUserController.load)
      else
        Redirect(routes.AuthenticationController.start)
          .flashing("info" -> "You are logged out.")
          .withNewSession
    }
    resultOption.getOrElse(Ok(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString)))
  }

  def start = Action { implicit request =>
    Ok(views.html.start(routes.AuthenticationController.loadAuthenticationForm().toString))
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
    def success = { loginData : LoginData =>
      val res = for {
        status <- accountService.attemptLogin(loginData)
        accountOption <- accountService.getAccount(Const.BY_EMAIL, loginData.email)
      } yield (status, accountOption)

      res map {
        case ((StatusCodes.OK, _), accountOption) =>
          Redirect(routes.AuthenticatedUserController.load)
            //       Ok(views.html.start(routes.AuthenticatedUserController.load().toString))
            .flashing("info" -> "You are logged in.")
            .withSession(
              Global.SESSION_ID_KEY -> accountOption.get._id.get,
              Global.SESSION_USERNAME -> accountOption.get.username,
              Global.SESSION_EXPIRATION_DATE -> (System.currentTimeMillis() + Const.SESSION_DURATION.toMillis).toString,
              "csrfToken" -> CSRF.getToken.get.value)
        case ((error, msg), _) =>
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

      val res = for {
        status <- accountService.addAccount(registerData.username, registerData.email, registerData.password)
        account <- accountService.getAccount(Const.BY_EMAIL, registerData.email)
      } yield (status, account)

      res map {
        case (StatusCodes.OK, accountOption) =>
          Redirect(routes.AuthenticatedUserController.load)
            //  Ok(views.html.start(routes.AuthenticatedUserController.load().toString))
            .flashing("info" -> "You are registered in.")
            .withSession(
              Global.SESSION_ID_KEY ->  accountOption.get._id.get,
              Global.SESSION_USERNAME -> accountOption.get.username,
              Global.SESSION_EXPIRATION_DATE -> (System.currentTimeMillis() + Const.SESSION_DURATION.toMillis).toString,
              "csrfToken" -> CSRF.getToken.get.value)
        case (error, _) =>
          wrongAuthentication(s"Register - $error")
      }
    }
    RegisterData.registerForm.bindFromRequest.fold(failure,success)
  }



}
