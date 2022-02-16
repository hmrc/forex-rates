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

package uk.gov.hmrc.forexrates.services

import uk.gov.hmrc.forexrates.config.AppConfig
import uk.gov.hmrc.forexrates.connectors.EcbForexConnector
import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.repositories.ForexRepository
import uk.gov.hmrc.forexrates.scheduler.ScheduledService

import java.time.{Clock, LocalDateTime}
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait EcbForexService extends ScheduledService[Boolean] with Logging

class EcbForexServiceImpl @Inject()(
                                     ecbForexConnector: EcbForexConnector,
                                     forexRepository: ForexRepository,
                                appConfig: AppConfig
                               ) extends EcbForexService {


  override val jobName: String = "RetrieveForexRatesJob"

  override def invoke(implicit ec: ExecutionContext): Future[Boolean] = {
    for{
      _ <- triggerFeedUpdate()
    } yield {
      true
    }
  }

  def triggerFeedUpdate()(implicit ec: ExecutionContext): Future[Seq[ExchangeRate]] = {

    val allCurrencyInserts = appConfig.currencies.map { currency =>
      getRatesToSave(currency).flatMap(
        rates => forexRepository.insert(rates)
      )
    }
    Future.sequence(allCurrencyInserts).map(_.flatten)
  }


  private def getRatesToSave(currency: String)(implicit ec: ExecutionContext): Future[Seq[ExchangeRate]] = {
    for{
      retrievedFeed <- ecbForexConnector.getFeed(currency).map(feeds => feeds.sortBy(_.date.toEpochDay))
      savedRates <- forexRepository.get(retrievedFeed.head.date, retrievedFeed.last.date, retrievedFeed.head.baseCurrency, retrievedFeed.head.targetCurrency)
    } yield{
      val pairedFeeds = retrievedFeed.map(retrievedRate => (retrievedRate, savedRates.find(savedFeed => savedFeed.date == retrievedRate.date)))
      pairedFeeds.filter(pair => pair._2.exists(_.value != pair._1.value)).foreach(
        conflict => logger.error(s"Conflict found when retrieving rates. Retrieved: ${conflict._1} Previously saved: ${conflict._2.get}")
      )
        pairedFeeds.filter(pair => pair._2.isEmpty).map(_._1)
    }
  }

}
