/*
 * Copyright 2023 HM Revenue & Customs
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

import uk.gov.hmrc.forexrates.config.AppConfig
import uk.gov.hmrc.forexrates.connectors.EcbForexHttpParser.*
import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpException, StringContextOps}

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class EcbForexConnector @Inject()(
                                   httpClientV2: HttpClientV2,
                                   appConfig: AppConfig,
                                 )(implicit ec: ExecutionContext) extends Logging {

  def getFeed(currency: String): Future[EcbForexResponse] = {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    val urlString = appConfig.ecbForexUrl + s"/rss/fxref-${currency.toLowerCase}.html"
    val url = new URL(urlString)
    httpClientV2.get(url"$url").withProxy.execute[EcbForexResponse].recover {
      case e: HttpException =>
        logger.error(s"Error response from ECB $url, received status ${e.responseCode}, body of response was: ${e.message}")
        Seq.empty
    }
  }

}
