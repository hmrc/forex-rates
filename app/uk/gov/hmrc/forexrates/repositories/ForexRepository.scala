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

import org.mongodb.scala.ClientSession
import org.mongodb.scala.model.{Filters, IndexModel, IndexOptions, Indexes}
import uk.gov.hmrc.forexrates.logging.Logging
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import uk.gov.hmrc.mongo.transaction.{TransactionConfiguration, Transactions}

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ForexRepository @Inject()(
                                 val mongoComponent: MongoComponent,
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
  ) with Transactions
    with Logging {

  private implicit val tc: TransactionConfiguration = TransactionConfiguration.strict

  def get(date: LocalDate, baseCurrency: String, targetCurrency: String): Future[Option[ExchangeRate]] = {
    collection
      .find(Filters.and(
        Filters.equal("date", date),
        Filters.equal("baseCurrency", baseCurrency),
        Filters.equal("targetCurrency", targetCurrency)
      ))
      .headOption()
  }

  def get(dateFrom: LocalDate, dateTo: LocalDate, baseCurrency: String, targetCurrency: String): Future[Seq[ExchangeRate]] = {
    collection
      .find(
        Filters.and(
          Filters.gte("date", dateFrom),
          Filters.lte("date", dateTo),
          Filters.equal("baseCurrency", baseCurrency),
          Filters.equal("targetCurrency", targetCurrency)
        ))
      .toFuture()
  }

  private def get(dateFrom: LocalDate, dateTo: LocalDate, baseCurrency: String, targetCurrency: String, session: ClientSession): Future[Seq[ExchangeRate]] = {
    collection
      .find(
        session,
        Filters.and(
        Filters.gte("date", dateFrom),
        Filters.lte("date", dateTo),
        Filters.equal("baseCurrency", baseCurrency),
        Filters.equal("targetCurrency", targetCurrency)
      ))
      .toFuture()
  }

  def insert(exchangeRate: Seq[ExchangeRate], session: ClientSession): Future[Seq[ExchangeRate]] = {
    collection
      .insertMany(session, exchangeRate)
      .toFuture()
      .map(_ => exchangeRate)
      .recover {
        case e: Exception =>
          logger.info(s"There was an error while inserting exchange rate data ${e.getMessage}", e)
          Seq.empty
      }
  }

  def insertIfNotPresent(ratesFromEcb: Seq[ExchangeRate]): Future[Seq[ExchangeRate]] = {
    for{
      result <- withSessionAndTransaction{
        session =>
          val ratesToSave = for {
            savedRates <- get(ratesFromEcb.head.date, ratesFromEcb.last.date, ratesFromEcb.head.baseCurrency, ratesFromEcb.head.targetCurrency, session)
          } yield {
            val pairedFeeds = ratesFromEcb.map(retrievedRate => (retrievedRate, savedRates.find(savedFeed => savedFeed.date == retrievedRate.date)))

            pairedFeeds.filter {
              case (rateFromEcb, savedRate) => savedRate.exists(_.value != rateFromEcb.value)
            }.foreach(
              conflict => logger.error(s"Conflict found when retrieving rates. Retrieved: ${conflict._1} Previously saved: ${conflict._2.get}")
            )
            pairedFeeds.filter{
              case (_, savedRate) => savedRate.isEmpty
            }.map(_._1)
          }
          ratesToSave.flatMap {
            case rates if rates.nonEmpty =>
              insert(rates, session)
            case rates =>
              Future.successful(rates)
          }
      }
    } yield result
  }
}

