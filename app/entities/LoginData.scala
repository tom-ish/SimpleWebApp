package entities

import play.api.data.Form
import play.api.data.Forms._

case class LoginData(email: String, password: String)
object LoginData {
  val loginForm = Form(mapping(
    "login_email" -> nonEmptyText(minLength = 6, maxLength = 60),
    "login_password" -> text
  )(LoginData.apply)(LoginData.unapply))
}
