import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "5.20.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-28"         % "0.59.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % "5.20.0"            % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-28"    % "0.59.0"            % Test,
    "org.scalatestplus"       %% "scalacheck-1-15"            % "3.2.11.0"          % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"        % "2.27.2"            % Test,
    "org.scalatestplus"       %% "mockito-3-4"                % "3.2.10.0"          % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.36.8"            % "test, it"
  )
}
