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

package uk.gov.hmrc.forexrates.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.forexrates.repositories.ForexRepository
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext


class ForexRatesController @Inject()(
                                      cc: ControllerComponents,
                                      repository: ForexRepository
                                    )(implicit ec: ExecutionContext) extends BackendController(cc) {

  def get(date: LocalDate, baseCurrency: String, targetCurrency: String): Action[AnyContent] = Action.async {
    repository.get(date, baseCurrency, targetCurrency).map {
      case Some(exchangeRate) => Ok(Json.toJson(exchangeRate))
      case None => NotFound
    }
  }
}
