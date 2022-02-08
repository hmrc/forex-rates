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

package uk.gov.hmrc.forexrates.models

import play.api.mvc.PathBindable
import uk.gov.hmrc.forexrates.logging.Logging

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Locale
import scala.util.{Failure, Success, Try}

package object Binders extends Logging {

  implicit val pathBindable: PathBindable[LocalDate] = new PathBindable[LocalDate] {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withLocale(Locale.UK)
      .withZone(ZoneId.systemDefault())

    override def bind(key: String, value: String): Either[String, LocalDate] = {
      Try {
        LocalDate.parse(value, dateTimeFormatter)
      } match {
        case Success(value) => Right(value)
        case Failure(_) => Left("Invalid Period")
      }

    }

    override def unbind(key: String, value: LocalDate): String =
      dateTimeFormatter.format(value)
  }

}
