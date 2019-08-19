package utils

object StatusCodes {
  sealed trait STATUS

  case object OK extends STATUS
  case object ERROR_UNKNOWN_ACCOUNT extends STATUS
  case object ERROR_CREATE_ACCOUNT extends STATUS
  case object ERROR_PASSWORD_MISMATCH extends STATUS
  case object ERROR_CREATE_PASSWORD extends STATUS
}
