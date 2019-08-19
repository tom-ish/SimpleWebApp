package entities

import play.api.data.Form
import play.api.data.Forms._

case class RegisterData(username: String, email: String, password: String)
object RegisterData {
  val registerForm = Form(mapping(
    "register_username" -> nonEmptyText(minLength = 6, maxLength = 32),
    "register_email" -> email,
    "register_password" -> nonEmptyText(minLength = 6, maxLength = 32)
  )(RegisterData.apply)(RegisterData.unapply)
  )
}