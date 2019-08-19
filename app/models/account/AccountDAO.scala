package models.account

import reactivemongo.api.commands.WriteResult
import utils.Const.MODE

import scala.concurrent.Future

trait AccountDAO {

  /**
   * Create a new account.
   */
  def createAccount(account: Account) : Future[WriteResult]


  /**
   * Find an account by username, email or id.
   */
  def findAccount(mode: MODE, value: String) : Future[Option[Account]]

}
