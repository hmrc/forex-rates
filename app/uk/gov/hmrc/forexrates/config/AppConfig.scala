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

package uk.gov.hmrc.forexrates.config

import play.api.Configuration
import play.api.libs.ws.WSProxyServer
import uk.gov.hmrc.forexrates.formats.FormatHelper
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.http.ws.WSProxyConfiguration

import java.time.LocalDate
import javax.inject.{Inject, Singleton}

@Singleton
class AppConfig @Inject()
(
  config: Configuration,
  servicesConfig: ServicesConfig
) {
  val cacheTtl: Long = config.get[Long]("mongodb.timeToLiveInDays")

  lazy val wsProxyServer: Option[WSProxyServer] = WSProxyConfiguration.buildWsProxyServer(config)
  val authBaseUrl: String = servicesConfig.baseUrl("auth")
  val auditingEnabled: Boolean = config.get[Boolean]("auditing.enabled")
  val graphiteHost: String = config.get[String]("microservice.metrics.graphite.host")
  val ecbForexUrl: String = servicesConfig.baseUrl("ecb-forex") + "/" + config.get[String]("microservice.services.ecb-forex.basePath")

  val currencies: Seq[String] = config.get[Seq[String]]("features.forex-scheduler.currencies")

  val rateLoggerEnabled: Boolean = config.get[Boolean]("features.rate-logger.enabled")
  val rateLoggerBaseCurrency: String = config.get[String]("features.rate-logger.base-currency")
  val rateLoggerTargetCurrency: String = config.get[String]("features.rate-logger.target-currency")
  val rateLoggerDatesToCheck: Seq[LocalDate] = config.get[Seq[String]]("features.rate-logger.dates-to-check").map{ date =>
    LocalDate.parse(date, FormatHelper.dateTimeFormatter)
  }
}
