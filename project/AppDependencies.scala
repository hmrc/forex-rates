import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "7.15.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-28"         % "1.1.0",
    "com.enragedginger"       %%  "akka-quartz-scheduler"     % "1.9.1-akka-2.6.x"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % "7.15.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-28"    % "1.1.0",
    "org.scalatest"           %% "scalatest"                  % "3.2.12",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "5.1.0",
    "org.scalatestplus"       %% "scalacheck-1-15"            % "3.2.11.0",
    "org.scalatestplus"       %% "mockito-3-4"                % "3.2.10.0",
    "org.mockito"             %% "mockito-scala"              % "1.17.7",
    "com.github.tomakehurst"  %  "wiremock-standalone"        % "2.27.2",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.62.2"
  ).map(_ % "test, it")
}
