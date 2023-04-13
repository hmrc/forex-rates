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

package uk.gov.hmrc.forexrates.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.forexrates.repositories.ForexRepository
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import uk.gov.hmrc.forexrates.formats.ExchangeRateJsonFormatter._


class ForexRatesController @Inject()(
                                      cc: ControllerComponents,
                                      repository: ForexRepository
                                    )(implicit ec: ExecutionContext) extends BackendController(cc) {

  private val EURO = "EUR"
  def get(date: LocalDate, targetCurrency: String): Action[AnyContent] = Action.async {
    repository.get(date, EURO, targetCurrency).map {
      case Some(exchangeRate) => Ok(Json.toJson(exchangeRate))
      case None => NotFound
    }
  }

  def getInverse(date: LocalDate, baseCurrency: String): Action[AnyContent] = Action.async {
    repository.get(date, EURO, baseCurrency).map {
      case Some(exchangeRate) => Ok(Json.toJson(exchangeRate.copy(baseCurrency = exchangeRate.targetCurrency, targetCurrency = exchangeRate.baseCurrency, value = 1/exchangeRate.value)))
      case None => NotFound
    }
  }

  def getRatesInDateRange(dateFrom: LocalDate, dateTo: LocalDate, targetCurrency: String): Action[AnyContent] = Action.async {
    if(dateTo.isBefore(dateFrom)) {
      Future.successful(BadRequest(Json.toJson("DateTo cannot be before DateFrom")))
    } else {
      for {
        rates <- repository.get(dateFrom, dateTo, EURO, targetCurrency)
      } yield {
        Ok(Json.toJson(rates))
      }
    }
  }

  def getInverseRatesInDateRange(dateFrom: LocalDate, dateTo: LocalDate, baseCurrency: String): Action[AnyContent] = Action.async {
    if(dateTo.isBefore(dateFrom)) {
      Future.successful(BadRequest(Json.toJson("DateTo cannot be before DateFrom")))
    } else {
      for {
        rates <- repository.get(dateFrom, dateTo, EURO, baseCurrency)
      } yield {
        val inverseRates = rates.map(
          exchangeRate => exchangeRate.copy(baseCurrency = exchangeRate.targetCurrency, targetCurrency = exchangeRate.baseCurrency, value = 1 / exchangeRate.value)
        )
        Ok(Json.toJson(inverseRates))
      }
    }
  }

  def getLatest(numberOfRates: Int, targetCurrency: String): Action[AnyContent] = Action.async {
    for{
      exchangeRates <- repository.getLatest(numberOfRates, EURO, targetCurrency)
    } yield {
      Ok(Json.toJson(exchangeRates))
    }
  }

  def getInverseLatest(numberOfRates: Int, baseCurrency: String): Action[AnyContent] = Action.async {
    for {
      exchangeRates <- repository.getLatest(numberOfRates, EURO, baseCurrency)
    } yield {
      val inverseRates = exchangeRates.map(rate => rate.copy(baseCurrency = rate.targetCurrency, targetCurrency = rate.baseCurrency, value = 1/rate.value))
      Ok(Json.toJson(inverseRates))
    }
  }
}

