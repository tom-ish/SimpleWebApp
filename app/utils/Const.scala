package utils

import scala.language.postfixOps
import scala.concurrent.duration._

object Const {
  final val USERNAME = "username"
  final val PASSWORD = "password"
  final val CSRF_TOKEN = "csrfToken"
  val SESSION_DURATION = 120 minutes

  sealed trait MODE
  case object BY_USERNAME extends MODE
  case object BY_EMAIL extends MODE
  case object BY_ID extends MODE
}
