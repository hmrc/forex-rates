package uk.gov.hmrc.forexrates.controllers

import base.SpecBase
import org.mockito.MockitoSugar.when
import org.scalatest.OptionValues.convertOptionToValuable
import play.api.http.Status.OK
import play.api.inject.bind
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.repositories.ForexRepository

import java.time.LocalDate
import scala.concurrent.Future

class ForexRatesControllerSpec extends SpecBase {

  val mockRepository = mock[ForexRepository]

  val requestDate: LocalDate = LocalDate.of(2022, 1, 22)
  val baseCurrency = "Test"
  val targetCurrency = "Test2"
  val rate = BigDecimal(500)

  val exchangeRate: ExchangeRate = ExchangeRate(
    date = requestDate,
    baseCurrency = baseCurrency,
    targetCurrency = targetCurrency,
    value = rate
  )

  ".get" - {

    lazy val request = FakeRequest(GET, routes.ForexRatesController.get(requestDate, baseCurrency, targetCurrency).url)

    "must return forex rates when data is found" in {

      when(mockRepository.get(requestDate, baseCurrency, targetCurrency)) thenReturn Future.successful(Some(exchangeRate))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual Json.toJson(exchangeRate)
      }
    }

    "must return forex rates when no data is found" in {

      when(mockRepository.get(requestDate, baseCurrency, targetCurrency)) thenReturn Future.successful(None)

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual NOT_FOUND
      }
    }
  }

  ".get date range" - {
    val dateTo = requestDate.plusDays(5)
    lazy val request = FakeRequest(GET, routes.ForexRatesController.getRatesInDateRange(requestDate, dateTo, baseCurrency, targetCurrency).url)

    "must return forex rates when data is found" in {

      when(mockRepository.get(requestDate, dateTo, baseCurrency, targetCurrency)) thenReturn Future.successful(Seq(exchangeRate))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual Json.toJson(Seq(exchangeRate))
      }
    }
  }
}

