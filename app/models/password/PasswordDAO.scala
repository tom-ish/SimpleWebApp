package models.password

import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait PasswordDAO {

  /**
   * Create a new mapping between an accountId and a password string.
   *
   * @param password
   * @return
   */
  def createPassword(password: Password): Future[WriteResult]

  /**
   *
   * @param id
   * @return
   */
  def findPassword(id: String): Future[Option[Password]]

  def isPasswordCorrect(accountId: String, passwordStr: String) : Future[(Boolean, String)]
}
