package models.password

import javax.inject.Inject
import models.account.Account
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.impl.Promise

//Import BSON-JSON conversions/collection
import reactivemongo.play.json._
import reactivemongo.play.json.collection._

import scala.concurrent.Future

class Passwords @Inject() (
                            components: ControllerComponents,
                            val reactiveMongoApi: ReactiveMongoApi,
                            implicit val materializer: akka.stream.Materializer
                          ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents with PasswordDAO {

  implicit def ec = components.executionContext

  def passwordsCollectionFuture: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection]("passwords"))



  override def createPassword(password: Password): Future[WriteResult] =
    passwordsCollectionFuture flatMap(_.insert.one(
      password.copy(
        _id = password._id,
        accountId = password.accountId,
        password = password.password
      )
    ))

  override def findPassword(accountId: String) =
    passwordsCollectionFuture flatMap(
      _.find(Json.obj("accountId" -> accountId), Option.empty[JsObject]).one[Password]
      )


  def isPasswordCorrect(accountId: String, passwordStr: String) = {
    val password = findPassword(accountId)
    password flatMap {
      case Some(p : Password) =>
        if (p.password == passwordStr) Future.successful((true, ""))
        else Future.successful((false, "mismatch"))
      case error =>
        Future.successful((false, s"$error"))
    }
  }

}
