package models.account

import java.util.UUID

import javax.inject.Inject
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.commands.WriteResult

//Import BSON-JSON conversions/collection
import reactivemongo.play.json._
import reactivemongo.play.json.collection._
import utils.Const
import utils.Const.{BY_EMAIL, BY_ID, BY_USERNAME}

import scala.concurrent.Future

class Accounts @Inject() (
                           components: ControllerComponents,
                           val reactiveMongoApi: ReactiveMongoApi,
                           implicit val materializer: akka.stream.Materializer
                         ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents with AccountDAO {

  implicit def ec = components.executionContext

  def accountsCollectionFuture: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection]("accounts"))


  override def createAccount(account: Account) =
    accountsCollectionFuture flatMap(_.insert.one(
      account.copy(
        _id = account._id.orElse(Some(UUID.randomUUID().toString)),
        username = account.username,
        email = account.email,
        creationDate = account.creationDate)
    ))
      //.map(lastError => Ok("Mongo LastError: %s".format(lastError))))
      //.map(_.code ))

  override def findAccount(mode: Const.MODE, value: String) =
    accountsCollectionFuture flatMap {
      mode match {
        case BY_ID =>
          _.find(Json.obj("_id" -> value), Option.empty[JsObject]).one[Account]
        case BY_USERNAME =>
          _.find(Json.obj("username" -> value), Option.empty[JsObject]).one[Account]
        case BY_EMAIL =>
          _.find(Json.obj("email" -> value), Option.empty[JsObject]).one[Account]

      }
    }
}
