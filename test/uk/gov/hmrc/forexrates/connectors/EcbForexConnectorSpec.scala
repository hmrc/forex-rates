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

import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.forexrates.base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock._
import play.api.Application
import play.api.test.Helpers.running
import uk.gov.hmrc.forexrates.testutils.EcbForexData

class EcbForexConnectorSpec extends SpecBase with WireMockHelper {

  private val url = "/rss/fxref-gbp.html"

  private def application: Application = {
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.ecb-forex.host" -> "127.0.0.1",
        "microservice.services.ecb-forex.port" -> server.port
      )
      .build()
  }

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

        result mustBe Seq()
      }
    }

  }

}
