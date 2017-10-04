package au.edu.utscic.tap.services.analytics.analysers

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 9/1/17.
  */
trait Analyser extends Actor with ActorLogging {

  import context._

  def analyserName = this.getClass.getName

  override def preStart() = {
    log.debug("Starting {}",analyserName)
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting {} due to [{}] when processing [{}]", analyserName,
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
    case text:String => sender ! analyse(text)
    case _ => sender ! "Invalid input"
  }

  def analyse(text:String):Future[Any] = Future("nothing")
}
