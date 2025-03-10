/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.forexrates.scheduler

import org.apache.pekko.actor.Props
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite



class SchedulingActorSpec extends PlaySpec with GuiceOneAppPerSuite {

  class Setup {
    val schedulingActorCompanionObject: SchedulingActor.type = SchedulingActor
  }

  ".props" should {

    "return the correct type of props" in new Setup {
      val expectedType: Props = Props[SchedulingActor]()
      val result: Props = schedulingActorCompanionObject.props
      result mustBe expectedType
    }
  }
}