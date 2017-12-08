package test.sender.mailbox
import akka.actor._

case object Hello
case class Reply(str: String)
case class goodbye(Str: String)

/**
  * Expecting someone here
  */
class Sayhello(SendBye: ActorRef) extends Actor {

  def receive = {
    case Hello =>
      println("received hello")

      /**
        * communicating with `Saybye` eith ActorRef as `SendBye`
        */
      SendBye ! Reply("thanks for hello")

    /**
      * should go back to sender
      */
    case goodbye(str) =>
      println(s"received $str  ..ok")
      context.stop(self)

    /**
      * case where I do not understand received message
      */
    case _ => println("no clue in hello")
  }
}

class Saybye extends Actor {

  def receive = {

    /**
      * If I got Message as `Reply` gonna print
      * the received string as it is
      */
    case Reply(str) =>
      println("received bye " + str)
      sender ! goodbye("shutting down")
      context.stop(self)

    /**
      * case where I do not understand received message
      */
    case _ => println("no clue in bye")
  }
}

object greet extends App {

  val _system = ActorSystem("greeterSystem")

  /**
    * Created actor for `SendBye`
    */
  val SendBye = _system.actorOf(Props[Saybye], name = "itsForBye")

  /**
    * Created actor for `SayHello` and passing actor of `SendBye` as an argument
    */
  val SendHello =
    _system.actorOf(Props(new Sayhello(SendBye)), name = "itsforHello")

  SendHello ! Hello
}
