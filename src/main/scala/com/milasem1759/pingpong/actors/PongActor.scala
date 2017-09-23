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
class PongActor() extends Actor {

  val COUNTDOWN: Int = 4
  var count: Int = 0

  var waitEvent: Option[Cancellable] = None
  var ping: Option[ActorSelection] = None

  private def setTimeout(): Unit = {
    waitEvent = Some(context.system.scheduler.scheduleOnce(
      new FiniteDuration(1, TimeUnit.SECONDS),
      self,
      WaitTimeout()
    ))
  }

  private def send(): Unit = {
    if (ping.nonEmpty) {
      ping.get ! Pong("Pong")
      setTimeout()
      System.out.println(s"Pong")
    } else {
      System.out.println("Error: ping-actor was not found")
    }
  }

  override def preStart(): Unit = {
    ping = Some(context.actorSelection("../ping"))
  }

  override def receive: Receive = {
    case Ping(msg) =>
      if (waitEvent.nonEmpty)
        waitEvent.get.cancel()
      if (count < COUNTDOWN) {
        count += 1
        send()
      } else {
        count = 0
      }
    case _: WaitTimeout =>
      System.out.println(s"${sender.path.toString}: Ping message was not sent")
      send()
  }
}
