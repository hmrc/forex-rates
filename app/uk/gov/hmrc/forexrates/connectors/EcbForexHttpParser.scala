/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.forexrates.connectors

import play.api.http.Status.OK
import uk.gov.hmrc.forexrates.formats.Format
import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

import java.time.LocalDate
import scala.xml.{Elem, Node, XML}

object EcbForexHttpParser extends Logging {

  type EcbForexResponse = Seq[ExchangeRate]

  implicit object EcbForexReads extends HttpReads[EcbForexResponse] {
    override def read(method: String, url: String, response: HttpResponse): EcbForexResponse =
      response.status match {
        case OK =>
          val loadedXml = XML.loadString(response.body)
          xmlToExchangeRate(loadedXml)
        case status =>
          logger.error(s"Error response from ECB $url, received status $status, body of response was: ${response.body}")
          Seq.empty
      }
  }

  private def xmlToExchangeRate(baseElem: Elem): Seq[ExchangeRate] = {
    val listOfItems = baseElem \\ "RDF" \\ "item"
    listOfItems.map { singleItem =>
      parseSingleExchangeRate(singleItem)
    }
  }

  private def parseSingleExchangeRate(baseElem: Node): ExchangeRate = {
    val date = baseElem \\ "date"
    val testParsing = LocalDate.parse(date.text.split("T").head)
    val exchangeRateElem = baseElem \\ "statistics" \\ "exchangeRate"

    val value = exchangeRateElem \\ "value"
    val baseCurrency = exchangeRateElem \\ "baseCurrency"
    val targetCurrency = exchangeRateElem \\ "targetCurrency"


    ExchangeRate(
      testParsing,
      baseCurrency.text,
      targetCurrency.text,
      BigDecimal(value.text)
    )

  }

}
