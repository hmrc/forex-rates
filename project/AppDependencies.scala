import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt.*

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"  % "9.5.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-30"         % "2.2.0",
    "io.github.samueleresca"  %% "pekko-quartz-scheduler"     % "1.2.0-pekko-1.0.x"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % "9.5.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % "2.2.0",
    "org.scalatest"           %% "scalatest"                  % "3.2.19",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "7.0.1",
    "org.scalatestplus"       %% "scalacheck-1-15"            % "3.2.11.0",
    "org.scalatestplus"       %% "mockito-4-6"                % "3.2.15.0",
    "org.mockito"             %% "mockito-scala"              % "1.17.37",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.8"
  ).map(_ % "test, it")
}
