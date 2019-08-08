package models

import play.api.data.Form
import play.api.data.Forms._

case class RegisterData(username: String, email: String, password: String)
object RegisterData {
  val registerForm = Form(mapping(
    "username" -> text(4, 32),
    "email" -> email,
    "password" -> text(4, 32)
  )(RegisterData.apply)(RegisterData.unapply))
}