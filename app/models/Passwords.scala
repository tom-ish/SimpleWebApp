package models

import java.util.UUID

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

case class Password(accountId: UUID, password: String, id: UUID = UUID.randomUUID())
class PasswordTableDef(tag: Tag) extends Table[Password](tag, "passwords") {

  def id = column[UUID]("id", O.PrimaryKey, O.AutoInc, O.Unique)
  def accountId = column[UUID]("accountId")
  def password = column[String]("password")

  override def * =
    (accountId, password, id) <> ((Password.apply _).tupled, Password.unapply)
}

class Passwords @Inject()(protected val dbConfigProvider : DatabaseConfigProvider, cc : ControllerComponents)
                        (implicit executionContext : ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile]
{
  val passwords = TableQuery[PasswordTableDef]

  /**
   * Create a new password association with the given account id and password.
   *
   * This is an asynchronous operation, it will return a future of the created Password mapping, which can be used to obtain the
   * id for that mapping.
   */
  def addPassword(accountId: UUID, password: String): Future[Password] = dbConfig.db.run {
    // We create a projection of just the username and email columns, since we're not inserting a value for the id column
    (passwords.map(p => (p.accountId, p.password))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning passwords.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((passwordTuple, id) => Password(passwordTuple._1, passwordTuple._2, id))
      // And finally, insert the person into the database
      ) += (accountId, password)
  }
}
