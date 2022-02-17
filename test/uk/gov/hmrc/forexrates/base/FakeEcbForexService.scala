package uk.gov.hmrc.forexrates.base

import uk.gov.hmrc.forexrates.services.EcbForexService

import scala.concurrent.{ExecutionContext, Future}

class FakeEcbForexService extends EcbForexService {
  override val jobName: String = "RetrieveForexRatesJob"

  override def invoke(implicit ec: ExecutionContext): Future[Boolean] = Future.successful(true)
}
