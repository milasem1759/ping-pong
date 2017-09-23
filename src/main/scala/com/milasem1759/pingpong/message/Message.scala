package com.milasem1759.pingpong.message

/**
  * Created by Semenova Liudmila
  *
  */
sealed trait Message

case class Ping(msg: String) extends Message

case class Pong(msg: String) extends Message

case class WaitTimeout() extends Message
