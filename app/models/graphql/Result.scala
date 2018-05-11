package models.graphql

import java.time.OffsetDateTime

trait Result {
  val analytics: Any
  val timestamp: String = OffsetDateTime.now().toString
  val querytime: Int
  val message: String
}
