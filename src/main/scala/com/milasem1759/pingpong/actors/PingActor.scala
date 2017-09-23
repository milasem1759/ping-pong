package com.milasem1759.pingpong.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSelection, Cancellable}
import com.milasem1759.pingpong.message.{Ping, Pong, WaitTimeout}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

/**
  * Created by Semenova Liudmila
  *
  */
class PingActor() extends Actor {

  var waitEvent: Option[Cancellable] = None
  var pong: Option[ActorSelection] = None

  private def setTimeout(): Unit = {
    waitEvent = Some(context.system.scheduler.scheduleOnce(
      new FiniteDuration(1, TimeUnit.SECONDS),
      self,
      WaitTimeout()
    ))
  }

  private def send(): Unit = {
    if (pong.nonEmpty) {
      pong.get ! Ping("Ping")
      setTimeout()
      System.out.println(s"Ping")
    } else {
      System.out.println("Error: pong-actor was not found")
    }
  }

  override def preStart(): Unit = {
    pong = Some(context.actorSelection("../pong"))
    send()
  }

  override def receive: Receive = {
    case Pong(msg) =>
      if (waitEvent.nonEmpty)
        waitEvent.get.cancel()
      send()
    case _: WaitTimeout =>
      System.out.println(s"${sender.path.toString}: Pong message was not sent")
      send()
  }
}