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

package uk.gov.hmrc.forexrates.services

import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.forexrates.repositories.ForexRepository

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ExchangeRateLogger @Inject()(

                                    forexRepository: ForexRepository)
                                  (implicit ec: ExecutionContext) extends Logging {

  def logPresenceOfRate(datesToCheck: Seq[LocalDate], baseCurrency: String, targetCurrency: String): Unit = {
    Future.sequence {datesToCheck.map { dateToCheck =>
      forexRepository.get(dateToCheck, baseCurrency, targetCurrency).map {
        case Some(exchangeRate) =>
          logger.info(s"The exchange rate for [$baseCurrency] to [$targetCurrency] [$dateToCheck] exists and is [${exchangeRate.value}]")
        case _ =>
          logger.info(s"The exchange rate [$dateToCheck] did not exist")
      }
    }}

  }

}
