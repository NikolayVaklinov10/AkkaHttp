package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.stream.ActorMaterializer

object DirectivesBreakdown extends App {
  implicit val system = ActorSystem("DirectivesBreakdown")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher
  import akka.http.scaladsl.server.Directives._

  /*
  Type #1: filtering directives
   */

  val simpleHttpMethodRoute =
    post {  // equivalent directives for get, put, patch, delete, head, options
      complete(StatusCodes.Forbidden)
    }

  val simplePathRoute =
    path("about") {
      complete(
        HttpEntity(
          ContentTypes.`application/json`,
          """
            |<html>
            |<body>
            | Hello from the about page!
            |</body>
            |</html>
            |""".stripMargin
        )
      )
    }

  val complexPathRoute =
    path("api" / "myEndpoint") {
      complete(StatusCodes.OK)
    }  // /api/myEndpoint

  // bad practice
  val dontConfuse =
    path("api/myEndpoint") {
      complete(StatusCodes.OK)
    }

  val pathEndRoute =
    pathEndOrSingleSlash {  // localhost:8080 OR localhost:8080/
      complete(StatusCodes.OK)
    }

  Http().bindAndHandle(complexPathRoute, "localhost", 8080)

  /**
   * Type #2: extraction directives
   */

  // GET on /api/item/42
  val pathExtractionRoute =
  path("api" / "item" / IntNumber) { (itemNumber: Int) =>
    // other directives
    println(s"I've got a number in my path: $itemNumber")
    complete(StatusCodes.OK)
  }


}
