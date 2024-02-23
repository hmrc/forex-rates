package uk.gov.hmrc.forexrates.base

import org.apache.pekko.actor.ActorSystem
import play.api.Configuration
import play.api.inject.ApplicationLifecycle
import uk.gov.hmrc.forexrates.scheduler.SchedulingActor
import uk.gov.hmrc.forexrates.scheduler.SchedulingActor.RetrieveExchangeRatesClass
import uk.gov.hmrc.forexrates.scheduler.jobs.RetrieveForexRatesJob

import javax.inject.Inject

class FakeRetrieveForexRatesJob @Inject()(val config: Configuration,
                                          val service: FakeEcbForexService,
                                          val applicationLifecycle: ApplicationLifecycle
                                              ) extends RetrieveForexRatesJob {
  override val jobName: String = "RetrieveForexRatesJob"
  override val scheduledMessage: SchedulingActor.ScheduledMessage[_] = RetrieveExchangeRatesClass(service)
  override val actorSystem: ActorSystem = ActorSystem(jobName)
}
