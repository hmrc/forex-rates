package uk.gov.hmrc.forexrates.services

import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.mockito.ArgumentMatchersSugar.eqTo
import uk.gov.hmrc.forexrates.base.SpecBase
import uk.gov.hmrc.forexrates.config.AppConfig
import uk.gov.hmrc.forexrates.connectors.EcbForexConnector
import uk.gov.hmrc.forexrates.models.ExchangeRate

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EcbForexServiceSpec extends SpecBase {

  private val mockEcbForexConnector = mock[EcbForexConnector]
  private val mockAppConfig = mock[AppConfig]
  private val service: EcbForexService = new EcbForexService(mockEcbForexConnector, mockAppConfig)

  "EcbForexService#triggerFeedUpdate" - {
    val currency1 = "GBP"
    val currencies = Seq(currency1)
    val exchangeRate1 = arbitrary[ExchangeRate].sample.value

    "be successful when" - {
      "after pulling successfully it sends feeds to repo" in {

        when(mockAppConfig.currencies) thenReturn currencies
        when(mockEcbForexConnector.getFeed(eqTo(currency1))) thenReturn Future.successful(Seq(exchangeRate1))

        service.triggerFeedUpdate().futureValue mustBe ()
      }
    }
  }

}
