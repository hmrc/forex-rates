package uk.gov.hmrc.forexrates.formats



import org.scalatest.freespec.AnyFreeSpec
import play.api.libs.json.Json
import uk.gov.hmrc.forexrates.base.SpecBase

import java.time.{Clock, Instant, LocalDate, ZoneId}
import scala.concurrent.ExecutionContext
import uk.gov.hmrc.forexrates.models.ExchangeRate
import uk.gov.hmrc.forexrates.formats.ExchangeRateJsonFormatter.*

class ExchangeRateJsonFormatterSpec extends SpecBase {

  private val stubClock: Clock = Clock.fixed(LocalDate.of(2022, 1, 22).atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  "ExchangeRateJsonFormatter" - {

    "serialize and deserialize ExchangeRate correctly" in {

      val exchangeRate = ExchangeRate(
        date = LocalDate.of(2022, 1, 22),
        baseCurrency = "Test",
        targetCurrency = "Test2",
        value = BigDecimal(500),
        created = Instant.now(stubClock)
      )

      val json = Json.toJson(exchangeRate)
      json mustBe Json.parse(
        s"""{
          "targetCurrency":"Test2",
          "baseCurrency":"Test",
          "date":"2022-01-22",
          "value":500,
          "created":"${Instant.now(stubClock).toString}"
        }"""
      )

      val parsedExchangeRate = json.as[ExchangeRate]
      parsedExchangeRate mustBe exchangeRate
    }

  }
}


