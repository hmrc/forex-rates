package uk.gov.hmrc.forexrates.models

import java.time.LocalDateTime

case class ExchangeRate(date: LocalDateTime, baseCurrency: String, targetCurrency: String, value: BigDecimal)
