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

package uk.gov.hmrc.forexrates.repositories

import org.mongodb.scala.model.{Filters, IndexModel, IndexOptions, Indexes}
import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ForexRepository @Inject()(
                                 mongoComponent: MongoComponent,
                               )(implicit ec: ExecutionContext)
  extends PlayMongoRepository[ExchangeRate](
    collectionName = "exchangeRates",
    mongoComponent = mongoComponent,
    domainFormat = ExchangeRate.format,
    indexes = Seq(
      IndexModel(
        Indexes.ascending("date", "baseCurrency", "targetCurrency"),
        IndexOptions()
          .name("exchangeRatesIndex")
          .unique(true)
      )
    )
  )
    with Logging {

  def get(date: LocalDate, baseCurrency: String, targetCurrency: String): Future[Option[ExchangeRate]] = {
    collection
      .find(Filters.and(
        Filters.equal("date", date),
        Filters.equal("baseCurrency", baseCurrency),
        Filters.equal("targetCurrency", targetCurrency)
      ))
      .headOption()
  }

  def insert(exchangeRate: ExchangeRate): Future[Option[ExchangeRate]] = {
    collection
      .insertOne(exchangeRate)
      .toFuture()
      .map(_ => Some(exchangeRate))
      .recover {
        case e: Exception =>
          logger.info(s"There was an error while inserting exchange rate data ${e.getMessage}", e)
          None
      }
  }
}

