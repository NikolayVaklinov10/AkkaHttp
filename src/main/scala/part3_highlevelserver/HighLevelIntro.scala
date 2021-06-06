package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object HighLevelIntro extends App {

  //the usual implicits
  implicit val system = ActorSystem("HighLevelIntro")
  implicit val materializer = ActorMaterializer()

  // the Directives import for the high level akka http implicits
  import akka.http.scaladsl.server.Directives._


  val simpleRoute: Route =
    path("home") { // Directives
      complete(StatusCodes.OK) // Directives
    }

  val pathGetRoute: Route =
    path("home") {
      get {
        complete(StatusCodes.OK)
      }
    }

  // chaining directives with ~

  val chainedRoute: Route =
    path("myEndpoint") {
      get {
        complete(StatusCodes.OK)
      } /* VERY IMPORTANT ---> */ ~
        post {
          complete(StatusCodes.Forbidden)
        }
    } ~
      path("home") {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            """
              |<html>
              | <body>
              |   Hello from the high level Akka HTTP!
              | </body>
              |</html>
          """.stripMargin
          )
        )
      } // Routing tree

  Http().bindAndHandle(pathGetRoute, "localhost", 8080)


}
