package part3_highlevelserver

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object HighLevelIntro extends App {

  //the usual implicits
  implicit val system = ActorSystem("HighLevelIntro")
  implicit val materializer = ActorMaterializer()


}
