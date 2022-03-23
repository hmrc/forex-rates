package uk.gov.hmrc.forexrates.services

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, anyUrl, get, urlEqualTo}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.mockito.ArgumentMatchersSugar.eqTo
import org.mockito.Mockito
import org.scalatest.BeforeAndAfterEach
import play.api.inject.bind
import uk.gov.hmrc.forexrates.base.SpecBase
import uk.gov.hmrc.forexrates.config.AppConfig
import uk.gov.hmrc.forexrates.connectors.{EcbForexConnector, WireMockHelper}
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.repositories.ForexRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EcbForexServiceSpec extends SpecBase with WireMockHelper with BeforeAndAfterEach {

  private val mockEcbForexConnector = mock[EcbForexConnector]
  private val mockForexRepository = mock[ForexRepository]
  private val mockAppConfig = mock[AppConfig]
  private val service: EcbForexServiceImpl = new EcbForexServiceImpl(mockEcbForexConnector, mockForexRepository, mockAppConfig)


  override def beforeEach(): Unit = {
    Mockito.reset(mockEcbForexConnector, mockForexRepository)
  }

  "EcbForexService#triggerFeedUpdate" - {
    val currency1 = "GBP"
    val currencies = Seq(currency1)
    val exchangeRate1 = arbitrary[ExchangeRate].sample.value
    val exchangeRate2 = arbitrary[ExchangeRate].sample.value.copy(date = exchangeRate1.date.plusDays(1))

    "save all of the retrieved rates if they weren't previously saved" in {
      when(mockAppConfig.currencies) thenReturn currencies
      when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq(exchangeRate1, exchangeRate2))
      when(mockForexRepository.insertIfNotPresent(any())) thenReturn Future.successful(Seq(exchangeRate1, exchangeRate2))
      service.triggerFeedUpdate().futureValue mustBe Seq(exchangeRate1, exchangeRate2)
      verify(mockForexRepository, times(1)).insertIfNotPresent(Seq(exchangeRate1, exchangeRate2))
    }

    "save only the rates that were not previously saved" in {
      when(mockAppConfig.currencies) thenReturn currencies
      when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq(exchangeRate1, exchangeRate2))
      when(mockForexRepository.insertIfNotPresent(any())) thenReturn Future.successful(Seq(exchangeRate2))
      service.triggerFeedUpdate().futureValue mustBe Seq(exchangeRate2)
      verify(mockForexRepository, times(1)).insertIfNotPresent(Seq(exchangeRate1, exchangeRate2))
    }

    "must return an empty sequence if no rates were retrieved from ECB feed" in {
      when(mockAppConfig.currencies) thenReturn currencies
      when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq.empty)
      service.triggerFeedUpdate().futureValue mustBe Seq.empty
      verifyNoInteractions(mockForexRepository)
    }

    "must handle errors from ECB feed" in {
      when(mockAppConfig.currencies) thenReturn currencies
      when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq.empty)
      service.triggerFeedUpdate().futureValue mustBe Seq.empty
      verifyNoInteractions(mockForexRepository)
    }

    "must handle XML parsing errors gracefully" in {

      val url = "/ecb-forex-rss-stub/rss/fxref-gbp.html"

      val app = applicationBuilder.configure(
        "microservice.services.ecb-forex.protocol" -> "http",
        "microservice.services.ecb-forex.host" -> "127.0.0.1",
        "microservice.services.ecb-forex.port" -> server.port,
        "microservice.services.ecb-forex.basePath" -> "ecb-forex-rss-stub"
      ).build()

      server.stubFor(
        get(urlEqualTo(url))
          .willReturn(aResponse()
            .withStatus(200)
            .withBody("invalid body")
          )
      )

      val serv = app.injector.instanceOf[EcbForexServiceImpl]

      serv.triggerFeedUpdate().futureValue mustBe Seq.empty
      verifyNoInteractions(mockForexRepository)
    }
  }
}

