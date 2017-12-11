package test.sender.mailbox
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

case object AskNameMessage
case object worker
class TestActor extends Actor {
  def receive = {
    case AskNameMessage => // respond to the "ask" request
      sender ! "Fred"

    case worker =>
      println("worker actor")

    case _ => println("that was unexpected")

  }
}
object AskTest extends App {
  // create the system and actor
  val system = ActorSystem("AskTestSystem")
  val myActor = system.actorOf(Props[TestActor], name = "myActor")

  implicit val timeout = Timeout(5 seconds)
  val future = myActor ? AskNameMessage
  myActor ! worker

  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)

/*  val future2: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
  val result2 = Await.result(future2, 1 second)
  println(result2)*/

  system.terminate()
}
