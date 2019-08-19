package models.password

case class Password(_id : Option[String], accountId: String, password: String)

object Password {
  import play.api.libs.json.Json
  implicit val accountFormat = Json.format[Password]
}
