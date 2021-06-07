package part3_highlevelserver

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import part2_lowlevelserver.{Guitar, GuitarDB, GuitarStoreJsonProtocol}

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

  /*
    setup
   */
  val guitarDb = system.actorOf(Props[GuitarDB], "LowLevelGuitarDB")
  val guitarList = List(
    Guitar("Fender", "Stratocaster"),
    Guitar("Gibson", "Les Paul"),
    Guitar("Martin", "LX1")
  )

  guitarList.foreach { guitar =>
    guitarDb ! CreateGuitar(guitar)
  }





}
