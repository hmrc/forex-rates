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

import org.scalacheck.Arbitrary.arbitrary
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

  val session = mongoComponent.client.startSession().head().futureValue

  val requestDate: LocalDate = LocalDate.of(2022, 1, 22)
  val baseCurrency = "Test"
  val targetCurrency = "Test2"
  val rate = BigDecimal(500)

  val dateFrom = LocalDate.of(2022, 1, 20)
  val dateTo = LocalDate.of(2022, 1, 22)

  val exchangeRate1 = ExchangeRate(requestDate, baseCurrency, targetCurrency, rate)
  val exchangeRate2 = exchangeRate1.copy(date = exchangeRate1.date.plusDays(1))

  ".get" - {

    "must return none when no data exists for requested date" in {

      val result = repository.get(requestDate, baseCurrency, targetCurrency).futureValue

      result mustBe None
    }

    "must return a valid ExchangeRate when data exists for requested date" in {

      insert(exchangeRate1).futureValue

      val result = repository.get(requestDate, baseCurrency, targetCurrency).futureValue

      result mustBe Some(exchangeRate1)
    }
  }

  ".get(dateRange)" - {


    "must return none when data within requested date range is not found" in {

      val result = repository.get(dateFrom, dateTo, baseCurrency, targetCurrency, session).futureValue

      result mustBe Seq.empty
    }

    "must return a Sequence of exchange rate data for requested date range" in {

      val multipleExchangeRates = Seq(exchangeRate1.copy(date = dateFrom), exchangeRate1.copy(date = dateTo))

      repository.insert(multipleExchangeRates, session)

      val result = repository.get(dateFrom, dateTo, baseCurrency, targetCurrency, session).futureValue

      result mustBe multipleExchangeRates
    }
  }

  ".insert" - {

    "must insert one forex rss data entry when data received from rss api" in {

      val result = repository.insert(Seq(exchangeRate1), session).futureValue

      result mustBe Seq(exchangeRate1)
    }

    "must return None when insert forex rss data is unsuccessful" in {

      val result1 = repository.insert(Seq(exchangeRate1), session).futureValue
      val result2 = repository.insert(Seq(exchangeRate1), session).futureValue

      result1 mustBe Seq(exchangeRate1)
      result2 mustBe Seq.empty
    }
  }

  ".insertIfNotPresent" - {

    "must insert if data is not already present" in {

      val result = repository.insertIfNotPresent(Seq(exchangeRate1, exchangeRate2)).futureValue

      result mustBe Seq(exchangeRate1, exchangeRate2)

    }

    "must only save non-duplicated rate when there are conflicting rates present" in {

      repository.collection.insertOne(exchangeRate1).head().futureValue

      val result = repository.insertIfNotPresent(Seq(exchangeRate1, exchangeRate2)).futureValue

      result mustBe Seq(exchangeRate2)
    }
  }
}
