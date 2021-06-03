package part2_lowlevelserver

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCode, StatusCodes, Uri}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.ContentType
import akka.stream.ActorMaterializer
import part2_lowlevelserver.GuitarDB.{CreateGuitar, FindAllGuitars}
import spray.json._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future

case class Guitar(make: String, model: String)

object GuitarDB {
  case class CreateGuitar(guitar: Guitar)
  case class GuitarCreated(id: Int)
  case class FindGuitar(id: Int)
  case object FindAllGuitars
}

class GuitarDB extends Actor with ActorLogging {
  import GuitarDB._

  var guitars: Map[Int, Guitar] = Map()
  var currentGuitarId: Int = 0

  override def receive: Receive = {
    case FindAllGuitars =>
      log.info("Searching for all guitars")
      sender() ! guitars.values.toList

    case FindGuitar(id) =>
      log.info(s"Searching guitar by id: $id")
      sender() ! guitars.get(id)

    case CreateGuitar(guitar) =>
      log.info(s"Adding guitar $guitar with id $currentGuitarId")
      guitars = guitars + (currentGuitarId -> guitar)
      currentGuitarId += 1

  }
}
// this trait with its extension is needed for converting the data into Json and vice versa
trait GuitarStoreJsonProtocol extends DefaultJsonProtocol {
  implicit val guitarFormat = jsonFormat2(Guitar)
}


object LowLevelRest extends App with GuitarStoreJsonProtocol {

  implicit val system = ActorSystem("LowLevelRest")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  /*
  -GET on localhost:8080/api/guitar => ALL the guitars in the store
  POST on localhost:8080/api/guitar => insert the guitar into the store
   */

  // JSON -> marshalling
  val simpleGuitar = Guitar("Fender", "Startocaster")
  println(simpleGuitar.toJson.prettyPrint)

  // unmarshalling
  val simpleGuitarJsonString =
    """
      |{
      |  "make": "Fender",
      |  "model": "Startocaster"
      |}
      |""".stripMargin

  println(simpleGuitarJsonString.parseJson.convertTo[Guitar])

  /*
    setup
   */
  val guitarDb = system.actorOf(Props[GuitarDB], "LowLevelDB")
  val guitarList = List(
    Guitar("Fender","Stratcaster"),
    Guitar("Gibson","Les Paul"),
    Guitar("Martin","LX1")
  )

  guitarList.foreach { guitar =>
    guitarDb ! CreateGuitar(guitar)
  }

  /*
    server code
   */
  implicit val defaultTimeout = Timeout(2 seconds)

  val requestHandler: HttpRequest => Future[HttpResponse] = {
    case HttpRequest(HttpMethods.GET, Uri.Path("/api/guitar"),_,_,_) =>
      val guitarFuture:Future[List[Guitar]] = (guitarDb ? FindAllGuitars).mapTo[List[Guitar]]
      guitarFuture.map { guitars =>
        HttpResponse(
          entity = HttpEntity(
            ContentTypes.`application/json`,
            guitars.toJson.prettyPrint
          )
        )
      }
    case request: HttpRequest =>
      request.discardEntityBytes()
      Future {
        HttpResponse(status = StatusCodes.NotFound)
      }
  }

  Http().bindAndHandleAsync(requestHandler, "localhost", 8080)


}
