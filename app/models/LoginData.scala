package models

import play.api.data.Form
import play.api.data.Forms._

case class LoginData(email: String, password: String)
object LoginData {
  val loginForm = Form(mapping(
    "login_email" -> email,
    "login_password" -> text(4, 32)
  )(LoginData.apply)(LoginData.unapply))
}
