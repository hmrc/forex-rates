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

package uk.gov.hmrc.forexrates.generators

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import uk.gov.hmrc.forexrates.models.ExchangeRate

import java.time.LocalDate

trait ModelGenerators {
  self: Generators =>

  implicit val arbitraryExchangeRate: Arbitrary[ExchangeRate] =
    Arbitrary {
      for {
        date <- datesBetween(LocalDate.of(2021, 9, 1), LocalDate.of(2022, 9, 1))
        fromCurrency <- Gen.stringOfN(3, Gen.alphaChar)
        toCurrency <- Gen.stringOfN(3, Gen.alphaChar)
        value <- Gen.choose[BigDecimal](0, 1000000)
      } yield ExchangeRate(date, fromCurrency, toCurrency, value)
    }

}
