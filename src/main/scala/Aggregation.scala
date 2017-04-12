package example

import java.time.Instant
import scala.math.Ordering.Implicits._

case class Aggregation(
  userId: String,
  pageviewCount: Int,
  userLoginCount: Int,
  earliestPageview: Option[Instant],
  mostRecentPageview: Option[Instant],
  earliestUserLogin: Option[Instant],
  mostRecentUserLogin: Option[Instant]) {
  def ++(other: Aggregation) = copy(pageviewCount = pageviewCount + other.pageviewCount,
                                    userLoginCount  = userLoginCount + other.userLoginCount,
                                    earliestPageview = (earliestPageview ++ other.earliestPageview).reduceOption(Ordering[Instant].min),
                                    mostRecentPageview = (mostRecentPageview ++ other.mostRecentPageview).reduceOption(Ordering[Instant].max),
                                    earliestUserLogin = (earliestUserLogin ++ other.earliestUserLogin).reduceOption(Ordering[Instant].min),
                                    mostRecentUserLogin = (mostRecentUserLogin ++ other.mostRecentUserLogin).reduceOption(Ordering[Instant].max))
}

object Aggregation {
  def apply(event: Event): Aggregation = event match {
    case e: Pageview => Aggregation(event.userId, 1, 0, Some(e.eventTime), Some(e.eventTime), None, None)
    case e: UserLogin => Aggregation(event.userId, 0, 1, None, None, Some(e.eventTime), Some(e.eventTime))
  }
}
