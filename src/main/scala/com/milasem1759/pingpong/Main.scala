package com.milasem1759.pingpong

import akka.actor.{ActorSystem, Props}
import com.milasem1759.pingpong.actors.{PingActor, PongActor}

import scala.io.StdIn

/**
  * Created by Semenova Liudmila
  *
  */
object Main extends App {

  var system = ActorSystem("PingPong")

  try {
    val ping = system.actorOf(Props(new PingActor()), name = "ping")
    val pong = system.actorOf(Props(new PongActor()), name = "pong")
    StdIn.readLine()
  }
  finally {
    system.terminate()
  }
}

