import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt.*

object AppDependencies {

  private val bootstrapVersion = "10.7.0"
  private val hmrcMongoVersion = "2.12.0"
  private val pekkoVersion = "1.6.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"   % bootstrapVersion,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-30"          % hmrcMongoVersion,
    "io.github.samueleresca"  %% "pekko-quartz-scheduler"      % "1.3.0-pekko-1.1.x",
    "org.apache.pekko"        %% "pekko-protobuf-v3"           % pekkoVersion,
    "org.apache.pekko"        %% "pekko-serialization-jackson" % pekkoVersion,
    "org.apache.pekko"        %% "pekko-stream"                % pekkoVersion,
    "org.apache.pekko"        %% "pekko-actor-typed"           % pekkoVersion,
    "org.apache.pekko"        %% "pekko-slf4j"                 % pekkoVersion
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % bootstrapVersion,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % hmrcMongoVersion,
    "org.scalatestplus"       %% "scalacheck-1-15"            % "3.2.11.0",
  ).map(_ % "test, it")
}
