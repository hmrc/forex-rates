package uk.gov.hmrc.forexrates.connectors

import play.api.http.Status.OK
import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object EcbForexHttpParser extends Logging {

  type EcbForexResponse = Seq[ExchangeRate]

  implicit object EcbForexReads extends HttpReads[EcbForexResponse] {
    override def read(method: String, url: String, response: HttpResponse): EcbForexResponse =
      response.status match {
        case OK =>
          response.body
          Seq.empty
        case status =>
          logger.error(s"Error response from ECB $url, received status $status, body of response was: ${response.body}")
          Seq.empty
      }
  }

}
