package part3_highlevelserver

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import part2_lowlevelserver.{GuitarStoreJsonProtocol,GuitarDB}

object HighLevelExample extends App with GuitarStoreJsonProtocol {
  implicit val system = ActorSystem("HighLevelExample")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  import GuitarDB._

  /*
    GET /api/guitar fetches ALL the guitars in the store
    GET /api/guitar?id=x fetches the guitar with id X
    GET /api/guitar/X fetches guitar with id X
    GET /api/guitar/inventory?inStock=true
   */




}
