package example

import java.time.Instant

case class User(
  id: String
)

sealed trait Event {
  def id: String
  def userId: String
  def eventTime: Instant
}

object Event {

  def isPageview(s: String) = s.startsWith("pageview")
  def isUserLogin(s: String) = s.startsWith("userlogin")

  def parsePageview(s: String): Pageview = {
    val tokens = s.split('|')
    Pageview(tokens(1), tokens(2))
  }

  def parseUserLogin(s: String): UserLogin = {
    val tokens = s.split('|')
    UserLogin(tokens(1), tokens(2), tokens(3))
  }
}

case class Pageview(
  id: String,
  userId: String,
  eventTime: Instant = Instant.now()
) extends Event

case class UserLogin(
  id: String,
  userId: String,
  username: String,
  eventTime: Instant = Instant.now()
) extends Event
