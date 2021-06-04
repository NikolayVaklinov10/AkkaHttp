package part2_lowlevelserver

import java.io.InputStream
import java.security.{KeyStore, SecureRandom}

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import akka.stream.ActorMaterializer
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

object LowLevelHttp extends App {
  implicit val system = ActorSystem("LowLevelHttps")
  implicit val materializer = ActorMaterializer()

  val requestHolder: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.GET, _, _, _, _) =>
      HttpResponse(
        StatusCodes.OK, // HTTP 200
        entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            |<body>
            |   Hello from Akka HTTPS!
            |</body>
            |</html>
            |""".stripMargin
        )
      )

    case request: HttpRequest =>
      request.discardEntityBytes()
      HttpResponse(
        StatusCodes.NotFound, // 404
        entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            |<body>
            |   OOPS! Sadly The resource can't be found.
            |</body>
            |</html>
            |""".stripMargin
        )
      )
  }

  val httpsBinding = Http().bindAndHandleSync(requestHandler, "localhost", 8443, HttpsContext.httpsConnectionContext)


}
