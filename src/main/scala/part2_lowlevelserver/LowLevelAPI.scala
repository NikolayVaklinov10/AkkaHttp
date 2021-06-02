package part2_lowlevelserver

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object LowLevelAPI extends App {

  // the two implicits needed for akka streams on which akka http stands on top of
  implicit val system = ActorSystem("LowLevelServerAPI")
  implicit val materializer = ActorMaterializer()


}
