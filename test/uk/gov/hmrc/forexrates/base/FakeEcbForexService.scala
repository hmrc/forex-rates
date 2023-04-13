package uk.gov.hmrc.forexrates.base

import uk.gov.hmrc.forexrates.services.EcbForexService

import scala.concurrent.Future

class FakeEcbForexService extends EcbForexService {
  override val jobName: String = "RetrieveForexRatesJob"

  override def invoke: Future[Boolean] = Future.successful(true)
}
