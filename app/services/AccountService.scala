package services

import java.util.UUID

import entities.LoginData
import javax.inject.Inject
import models.account.{Account, Accounts}
import models.password.{Password, Passwords}
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import reactivemongo.api.commands.{WriteConcernError, WriteError, WriteResult}
import utils.{Const, StatusCodes}

import scala.concurrent.impl.Promise
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class AccountService @Inject()(accounts: Accounts, passwords: Passwords)
                              (implicit executionContext: ExecutionContext) {

  def addAccount(username: String, email: String, password: String) = {
    val accountId = UUID.randomUUID().toString
    accounts.createAccount(
      Account(Some(accountId), username, email, Some(new DateTime().toString))
    ) flatMap {
      case wr_account: WriteResult if wr_account.ok && wr_account.n == 1 =>
        passwords.createPassword(
          Password(Some(UUID.randomUUID().toString), accountId, password)
        ) map {
          case wr_password: WriteResult if wr_password.ok && wr_password.n == 1 =>
            StatusCodes.OK
          case _ =>
            StatusCodes.ERROR_CREATE_PASSWORD
        }
      case _ =>
        Future.successful(StatusCodes.ERROR_CREATE_ACCOUNT)
    }
  }

  def getAccount(by: Const.MODE, value: String) : Future[Option[Account]] =
    accounts.findAccount(by, value)

  def attemptLogin(formData: LoginData) = {
    val foundAccountFuture = getAccount(Const.BY_EMAIL, formData.email)
    foundAccountFuture flatMap {
      case Some(foundAccount: Account) => {
        val passwordCorrect = passwords.isPasswordCorrect(foundAccount._id.get, formData.password)
        passwordCorrect map {
          case true =>
            StatusCodes.OK
          case _ =>
            StatusCodes.ERROR_PASSWORD_MISMATCH
        }
      }
      case _ =>
        Future.successful(StatusCodes.ERROR_UNKNOWN_ACCOUNT)
    }
  }

  def deleteAccount(id: UUID) : Future[Int] = ???
  def listAllAccounts : Future[Seq[Account]] = ???

}

object AccountService {
  private def generateHash(password: String) = BCrypt.hashpw(password, BCrypt.gensalt)
  private def checkPassword(password: String, pwdHash: String) = BCrypt.checkpw(password, pwdHash)

}
