package uk.gov.hmrc.forexrates.controllers

import org.mockito.ArgumentMatchers.any
import uk.gov.hmrc.forexrates.base.SpecBase
import org.mockito.Mockito
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.MockitoSugar.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
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
import uk.gov.hmrc.forexrates.formats.ExchangeRateJsonFormatter._

class ForexRatesControllerSpec extends SpecBase with WireMockHelper with BeforeAndAfterEach {

  private val mockRepository = mock[ForexRepository]

  private val requestDate = LocalDate.of(2022, 1, 22)
  private val targetCurrency = "Test2"
  private val rate = BigDecimal(500)

  private val exchangeRate = ExchangeRate(
    date = requestDate,
    baseCurrency = "EUR",
    targetCurrency = targetCurrency,
    value = rate
  )

  val inverseExchangeRate = ExchangeRate(
    date = requestDate,
    baseCurrency = targetCurrency,
    targetCurrency = "EUR",
    value = 1/rate
  )

  override def beforeEach(): Unit = {
    super.beforeEach()
    Mockito.reset(mockRepository)
  }

  private val exchangeRateJson =
    s"""
       |{
       |"date": "${requestDate.toString}",
       |"baseCurrency": "EUR",
       |"targetCurrency": "$targetCurrency",
       |"value": $rate
       |}
       |""".stripMargin

  private val exchangeRateSeqJson = s"[$exchangeRateJson]"

  ".get" - {

    lazy val request = FakeRequest(GET, routes.ForexRatesController.get(requestDate, targetCurrency).url)

    "must return forex rates when data is found" in {

      when(mockRepository.get(any(), any(), any())) thenReturn Future.successful(Some(exchangeRate))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual Json.parse(exchangeRateJson)
      }
    }

    "must return Not Found when no data is found" in {

      when(mockRepository.get(any(), any(), any())) thenReturn Future.successful(None)

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

  ".getInverse" - {

    lazy val request = FakeRequest(GET, routes.ForexRatesController.getInverse(requestDate, targetCurrency).url)

    "must return forex rates when data is found" in {

      when(mockRepository.get(any(), any(), any())) thenReturn Future.successful(Some(exchangeRate))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual Json.toJson(inverseExchangeRate)
      }
    }

    "must return Not Found when no data is found" in {

      when(mockRepository.get(any(), any(), any())) thenReturn Future.successful(None)

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

  ".getRatesInDateRange" - {
    val dateTo = requestDate.plusDays(5)
    val request = FakeRequest(GET, routes.ForexRatesController.getRatesInDateRange(requestDate, dateTo, targetCurrency).url)

    "must return forex rates when data is found" in {

      when(mockRepository.get(any(), any(), any(), any())) thenReturn Future.successful(Seq(exchangeRate))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual Json.parse(exchangeRateSeqJson)
      }
    }

    "must return empty sequence when data is not found" in {

      when(mockRepository.get(any(), any(), any(), any())) thenReturn Future.successful(Seq.empty)

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

      when(mockRepository.get(any(), any(), any(), any())) thenReturn Future.failed(new Exception("Error connecting to the db"))

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

    "must return BadRequest when dateTo is before dateFrom" in {

      val dateTo = requestDate.minusDays(5)
      val request = FakeRequest(GET, routes.ForexRatesController.getRatesInDateRange(requestDate, dateTo, targetCurrency).url)

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe Json.toJson("DateTo cannot be before DateFrom")
        verifyNoInteractions(mockRepository)
      }
    }
  }

  ".getInverseDateRange" - {
    val dateTo = requestDate.plusDays(5)
    val request = FakeRequest(GET, routes.ForexRatesController.getInverseRatesInDateRange(requestDate, dateTo, targetCurrency).url)

    "must return forex rates when data is found" in {

      when(mockRepository.get(any(), any(), any(), any())) thenReturn Future.successful(Seq(exchangeRate))

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustEqual OK
        contentAsJson(result) mustEqual Json.toJson(Seq(inverseExchangeRate))
      }
    }

    "must return empty sequence when data is not found" in {

      when(mockRepository.get(any(), any(), any(), any())) thenReturn Future.successful(Seq.empty)

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

      when(mockRepository.get(any(), any(), any(), any())) thenReturn Future.failed(new Exception("Error connecting to the db"))

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

    "must return BadRequest when dateTo is before dateFrom" in {

      val dateTo = requestDate.minusDays(5)
      val request = FakeRequest(GET, routes.ForexRatesController.getInverseRatesInDateRange(requestDate, dateTo, targetCurrency).url)

      val app =
        applicationBuilder
          .overrides(
            bind[ForexRepository].toInstance(mockRepository))
          .build()

      running(app) {
        val result = route(app, request).value

        status(result) mustBe BAD_REQUEST
        contentAsJson(result) mustBe Json.toJson("DateTo cannot be before DateFrom")
        verifyNoInteractions(mockRepository)
      }
    }
  }
}

