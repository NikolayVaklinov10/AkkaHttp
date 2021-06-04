package part2_lowlevelserver

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object LowLevelHttp extends App {
  implicit val system = ActorSystem("LowLevelHttps")
  implicit val materializer = ActorMaterializer()

}
