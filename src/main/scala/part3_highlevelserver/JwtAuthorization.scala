package part3_highlevelserver

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object JwtAuthorization extends App {
  implicit val system = ActorSystem("JwtAuthorization")
  implicit val materialization = ActorMaterializer()
  import system.dispatcher

}
