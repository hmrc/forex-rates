/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.forexrates.scheduler.jobs

import akka.actor.ActorSystem
import play.api.Configuration
import play.api.inject.ApplicationLifecycle
import uk.gov.hmrc.forexrates.scheduler.ScheduledJob
import uk.gov.hmrc.forexrates.scheduler.SchedulingActor.RetrieveExchangeRatesClass
import uk.gov.hmrc.forexrates.services.EcbForexService

import javax.inject.Inject

trait RetrieveForexRatesJob extends ScheduledJob
class RetrieveForexRatesJobImpl @Inject()(val config: Configuration,
                                                val service: EcbForexService,
                                                val applicationLifecycle: ApplicationLifecycle
                                          ) extends RetrieveForexRatesJob {

  val jobName: String           = "RetrieveForexRatesJob"
  val actorSystem: ActorSystem  = ActorSystem(jobName)
  val scheduledMessage: RetrieveExchangeRatesClass = RetrieveExchangeRatesClass(service)

  schedule

}
