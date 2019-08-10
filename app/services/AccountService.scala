package services

import java.util.UUID

import javax.inject.Inject
import models.{Account, Accounts, Password, Passwords}

import scala.concurrent.{ExecutionContext, Future}

class AccountService @Inject()(accounts: Accounts, passwords: Passwords)(implicit executionContext: ExecutionContext) {

  def addAccount(username: String, email: String, password: String) : Future[Boolean] = {
    accounts.add(username, email) match {
      case account: Account => {
        passwords.addPassword(account.id, password) match {
          case password: Password => Future(true)
          case _ => Future(false)
        }
      }
      case _ => Future(false)
    }
  }
  def deleteAccount(id: UUID) : Future[Int] = accounts.delete(id)
  def getAccount(id: UUID) : Future[Option[Account]] = accounts.findById(id)
  def getAccountByUsername(username: String) : Future[Option[Account]] = accounts.findByUsername(username)
  def getAccountByEmail(email: String) : Future[Option[Account]] = accounts.findByEmail(email)
  def listAllAccounts : Future[Seq[Account]] = accounts.listAll

}
