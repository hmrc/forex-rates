package uk.gov.hmrc.forexrates.services

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.mockito.ArgumentMatchersSugar.eqTo
import uk.gov.hmrc.forexrates.base.SpecBase
import uk.gov.hmrc.forexrates.config.AppConfig
import uk.gov.hmrc.forexrates.connectors.EcbForexConnector
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.repositories.ForexRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EcbForexServiceSpec extends SpecBase {

  private val mockEcbForexConnector = mock[EcbForexConnector]
  private val mockForexRepository = mock[ForexRepository]
  private val mockAppConfig = mock[AppConfig]
  private val service: EcbForexServiceImpl = new EcbForexServiceImpl(mockEcbForexConnector, mockForexRepository, mockAppConfig)

  "EcbForexService#triggerFeedUpdate" - {
    val currency1 = "GBP"
    val currencies = Seq(currency1)
    val exchangeRate1 = arbitrary[ExchangeRate].sample.value
    val exchangeRate2 = arbitrary[ExchangeRate].sample.value.copy(date = exchangeRate1.date.plusDays(1))

    "save all of the retrieved rates if they weren't previously saved" in {
      when(mockAppConfig.currencies) thenReturn currencies
      when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq(exchangeRate1, exchangeRate2))
      when(mockForexRepository.get(any(), any(), any(), any())) thenReturn Future.successful(Seq.empty)
      when(mockForexRepository.insert(Seq(exchangeRate1, exchangeRate2))) thenReturn Future.successful(Seq(exchangeRate1, exchangeRate2))
      service.triggerFeedUpdate().futureValue mustBe Seq(exchangeRate1, exchangeRate2)
    }

    "save only the rates that were not previously saved" in {
      when(mockAppConfig.currencies) thenReturn currencies
      when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq(exchangeRate1, exchangeRate2))
      when(mockForexRepository.get(any(), any(), any(), any())) thenReturn Future.successful(Seq(exchangeRate1))
      when(mockForexRepository.insert(Seq(exchangeRate2))) thenReturn Future.successful(Seq(exchangeRate2))
      service.triggerFeedUpdate().futureValue mustBe Seq(exchangeRate2)
    }
  }

}
