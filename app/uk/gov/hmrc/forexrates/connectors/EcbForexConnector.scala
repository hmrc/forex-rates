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

import uk.gov.hmrc.forexrates.config.AppConfig
import uk.gov.hmrc.forexrates.connectors.EcbForexHttpParser._
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class EcbForexConnector @Inject()(
                                   httpClient: ProxiedHttpClient,
                                   appConfig: AppConfig
                                 )(implicit ec: ExecutionContext) {

  def getFeed(currency: String): Future[EcbForexResponse] = {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    httpClient.GET[EcbForexResponse](
      appConfig.ecbForexUrl,
      Seq.empty
    )
  }

}
