package models

import java.util.UUID

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}


case class Account(username: String, email: String, id: UUID = UUID.randomUUID())

class AccountTableDef(tag: Tag) extends Table[Account](tag, "accounts") {

  def id = column[UUID]("id", O.PrimaryKey, O.AutoInc, O.Unique)
  def username = column[String]("username", O.Unique)
  def email = column[String]("email")

  override def * =
    (username, email, id) <> ((Account.apply _).tupled, Account.unapply)
}

class Accounts @Inject()(protected val dbConfigProvider : DatabaseConfigProvider, cc : ControllerComponents)
                        (implicit executionContext : ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile]
{
  val accounts = TableQuery[AccountTableDef]



  /**
   * Create a new account with the given username and email.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def add(username: String, email: String): Future[Account] = dbConfig.db.run {
    // We create a projection of just the username and email columns, since we're not inserting a value for the id column
    (accounts.map(a => (a.username, a.email))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning accounts.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((accountTuple, id) => Account(accountTuple._1, accountTuple._2, id))
      // And finally, insert the person into the database
    ) += (username, email)
  }

  def delete(id: UUID): Future[Int] = dbConfig.db.run(
    accounts.filter(_.id === id).delete
  )

  def findById(id: UUID): Future[Option[Account]] = dbConfig.db.run(
    accounts.filter(_.id === id).result.headOption
  )

  def findByUsername(username: String) : Future[Option[Account]] = dbConfig.db.run(
    accounts.filter(_.username === username).result.headOption
  )

  def findByEmail(email: String) : Future[Option[Account]] = dbConfig.db.run(
    accounts.filter(_.email === email).result.headOption
  )

  def listAll(): Future[Seq[Account]] = dbConfig.db.run(
    accounts.result
  )
}
