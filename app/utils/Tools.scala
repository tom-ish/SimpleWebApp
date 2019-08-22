package utils

import java.text.SimpleDateFormat
import java.util.Date

object Tools {

  val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

  def getDateAsString(d: Date): String = {
    val dateFormat = new SimpleDateFormat(DATE_FORMAT)
    dateFormat.format(d)
  }

  def convertStringToDate(s: String): Date = {
    val dateFormat = new SimpleDateFormat(DATE_FORMAT)
    dateFormat.parse(s)
  }

  def convertDateStringToLong(dateAsString: String): Long = {
    convertStringToDate(dateAsString).getTime
  }

  def convertLongToDate(l: Long): Date = new Date(l)

  def prettyPrintDate(dateAsString: String) = getDateAsString(convertLongToDate(dateAsString.toLong))
}
