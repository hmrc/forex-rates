package uk.gov.hmrc.forexrates.controllers

import uk.gov.hmrc.forexrates.base.SpecBase
import org.mockito.MockitoSugar.when
import org.scalatest.OptionValues.convertOptionToValuable
import org.scalatest.concurrent.IntegrationPatience
import play.api.http.Status.OK
import play.api.inject.bind
import play.api.libs.json.{JsArray, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.forexrates.connectors.WireMockHelper
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.repositories.ForexRepository

import java.time.LocalDate
import scala.concurrent.Future

class ForexRatesControllerSpec extends SpecBase with WireMockHelper with IntegrationPatience {

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

    "must return Not Found when no data is found" in {

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
    val request = FakeRequest(GET, routes.ForexRatesController.getRatesInDateRange(requestDate, dateTo, baseCurrency, targetCurrency).url)

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

    "must return empty sequence when data is not found" in {

      when(mockRepository.get(requestDate, dateTo, baseCurrency, targetCurrency)) thenReturn Future.successful(Seq.empty)

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual JsArray()
      }
    }

    "must throw exception when the call to the repository fails" in {

      when(mockRepository.get(requestDate, dateTo, baseCurrency, targetCurrency)) thenReturn Future.failed(new Exception("Error connecting to the db"))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        whenReady(result.failed) { exp => exp mustBe a[Exception] }

      }
    }
  }
}

