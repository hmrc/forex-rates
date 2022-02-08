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

package repositories

import org.scalatest.OptionValues
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.repositories.ForexRepository
import uk.gov.hmrc.mongo.test.{CleanMongoCollectionSupport, DefaultPlayMongoRepositorySupport}

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global

class ForexRepositorySpec
  extends AnyFreeSpec
    with Matchers
    with DefaultPlayMongoRepositorySupport[ExchangeRate]
    with CleanMongoCollectionSupport
    with ScalaFutures
    with IntegrationPatience
    with OptionValues {

  override protected val repository =
    new ForexRepository(
      mongoComponent = mongoComponent
    )

  val requestDate: LocalDate = LocalDate.of(2022, 1, 22)
  val baseCurrency = "Test"
  val targetCurrency = "Test2"
  val rate = BigDecimal(500)

  val dateFrom = LocalDate.of(2022, 1, 20)
  val dateTo = LocalDate.of(2022, 1, 22)

  val exchangeRate: ExchangeRate = ExchangeRate(
    date = requestDate,
    baseCurrency = baseCurrency,
    targetCurrency = targetCurrency,
    value = rate
  )

  ".get" - {

    "must return none when no data exists for requested date" in {

      val result = repository.get(requestDate, baseCurrency, targetCurrency).futureValue

      result mustBe None
    }

    "must return a valid ExchangeRate when data exists for requested date" in {

      insert(exchangeRate).futureValue

      val result = repository.get(requestDate, baseCurrency, targetCurrency).futureValue

      result mustBe Some(exchangeRate)
    }
  }

  ".get(dateRange)" - {

    "must return none when data within requested date range is not found" in {

      val result = repository.get(dateFrom, dateTo, baseCurrency, targetCurrency).futureValue

      result mustBe Seq.empty
    }

    "must return a Sequence of exchange rate data for requested date range" in {

      val multipleExchangeRates = Seq(exchangeRate.copy(date = dateFrom), exchangeRate.copy(date = dateTo))

      repository.insert(multipleExchangeRates)

      val result = repository.get(dateFrom, dateTo, baseCurrency, targetCurrency).futureValue

      result mustBe multipleExchangeRates
    }
  }

  ".insert" - {

    "must insert one forex rss data entry when data received from rss api" in {

      val result = repository.insert(Seq(exchangeRate)).futureValue

      result mustBe Seq(exchangeRate)
    }

    "must return None when insert forex rss data is unsuccessful" in {

      val result1 = repository.insert(Seq(exchangeRate)).futureValue
      val result2 = repository.insert(Seq(exchangeRate)).futureValue

      result1 mustBe Seq(exchangeRate)
      result2 mustBe Seq.empty
    }
  }
}
