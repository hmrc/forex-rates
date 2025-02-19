import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt.*

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"  % "9.9.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-30"         % "2.2.0",
    "io.github.samueleresca"  %% "pekko-quartz-scheduler"     % "1.2.0-pekko-1.0.x"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % "9.9.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % "2.2.0",
    "org.scalatestplus"       %% "scalacheck-1-15"            % "3.2.11.0",
  ).map(_ % "test, it")
}
