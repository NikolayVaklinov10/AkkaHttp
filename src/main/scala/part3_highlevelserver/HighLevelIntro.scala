package part3_highlevelserver

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object HighLevelIntro extends App {

  //the usual implicits
  implicit val system = ActorSystem("HighLevelIntro")
  implicit val materializer = ActorMaterializer()

  // the Directives import for the high level akka http implicits
  import akka.http.scaladsl.server.Directives._



}
