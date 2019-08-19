package models.account

import org.joda.time.DateTime

case class Account(_id : Option[String], username: String, email: String, creationDate: Option[String])

object Account {
  import play.api.libs.json.Json
  implicit val accountFormat = Json.format[Account]
}
