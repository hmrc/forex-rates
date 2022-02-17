package uk.gov.hmrc.forexrates.base

import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.forexrates.generators.Generators
import uk.gov.hmrc.forexrates.scheduler.jobs.RetrieveForexRatesJob
import uk.gov.hmrc.forexrates.services.EcbForexService

trait SpecBase extends AnyFreeSpec
  with Matchers
  with OptionValues
  with ScalaFutures
  with MockitoSugar
  with Generators {

  protected def applicationBuilder: GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[EcbForexService].to[FakeEcbForexService],
        bind[RetrieveForexRatesJob].to[FakeRetrieveForexRatesJob]
      )
}
