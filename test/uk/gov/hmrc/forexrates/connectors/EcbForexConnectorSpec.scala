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

import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Seconds, Span}
import play.api.Application
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers.running
import uk.gov.hmrc.forexrates.base.SpecBase
import uk.gov.hmrc.forexrates.models.RetrievedExchangeRate
import uk.gov.hmrc.forexrates.testutils.EcbForexData

import java.time.LocalDate

class EcbForexConnectorSpec extends SpecBase with WireMockHelper with IntegrationPatience {

  private val url = "/ecb-forex-rss-stub/rss/fxref-gbp.html"

  private def application: Application = {
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.ecb-forex.protocol" -> "http",
        "microservice.services.ecb-forex.host" -> "127.0.0.1",
        "microservice.services.ecb-forex.port" -> server.port,
        "microservice.services.ecb-forex.basePath" -> "ecb-forex-rss-stub"
      )
      .build()
  }

  val errorStatuses = Seq(BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR)

  "EcbForexConnector get" - {

    "return feed items" in {
      val app = application

      server.stubFor(
        get(urlEqualTo(url))
          .willReturn(aResponse()
            .withStatus(200)
            .withBody(EcbForexData.exampleXml)
          )
      )

      running(app) {
        val connector = app.injector.instanceOf[EcbForexConnector]
        val result = connector.getFeed("GBP").futureValue

        result mustBe Seq(
          RetrievedExchangeRate(LocalDate.of(2022,1, 27), "EUR", "GBP", BigDecimal(0.83368)),
          RetrievedExchangeRate(LocalDate.of(2022,1, 26), "EUR", "GBP", BigDecimal(0.83458)),
          RetrievedExchangeRate(LocalDate.of(2022,1, 25), "EUR", "GBP", BigDecimal(0.83713)),
          RetrievedExchangeRate(LocalDate.of(2022,1, 24), "EUR", "GBP", BigDecimal(0.83803)),
          RetrievedExchangeRate(LocalDate.of(2022,1, 21), "EUR", "GBP", BigDecimal(0.83633))
        )
      }
    }

    "return empty sequence and log the error when xml received from ecb cannot be parsed" in {

      val app = application

      server.stubFor(
        get(urlEqualTo(url))
          .willReturn(aResponse()
            .withStatus(200)
            .withBody("invalid body")
          )
      )

      running(app) {
        val connector = app.injector.instanceOf[EcbForexConnector]
        val result = connector.getFeed("GBP").futureValue

        result mustBe Seq.empty
      }
    }

    errorStatuses.foreach { status =>
      s"return empty sequence and log the error when $status received from ecb" in {
        val app = application

        server.stubFor(
          get(urlEqualTo(url))
            .willReturn(aResponse()
              .withStatus(500)
              .withBody("error")
            )
        )

        running(app) {
          val connector = app.injector.instanceOf[EcbForexConnector]
          val result = connector.getFeed("GBP").futureValue

          result mustBe Seq.empty
        }
      }
    }

    "return empty sequence and log the error when Http Exception received from ecb" in {
      val app = application

      server.stubFor(
        get(urlEqualTo(url))
          .willReturn(aResponse()
            .withStatus(504)
            .withFixedDelay(21000))
      )

      running(app) {
        val connector = app.injector.instanceOf[EcbForexConnector]

        whenReady(connector.getFeed("GBP"), Timeout(Span(30, Seconds))) { exp =>
          exp mustBe Seq.empty
        }
      }
    }

  }

}
