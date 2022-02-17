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

package uk.gov.hmrc.forexrates.testutils

object EcbForexData {

  val exampleXml: String =
    """<?xml version="1.0" encoding="utf-8"?>
      |<rdf:RDF  xmlns = "http://purl.org/rss/1.0/" xmlns:cb = "http://www.cbwiki.net/wiki/index.php/Specification_1.1" xmlns:dc = "http://purl.org/dc/elements/1.1/" xmlns:dcterms = "http://purl.org/dc/terms/" xmlns:rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation = "http://www.w3.org/1999/02/22-rdf-syntax-ns# rdf.xsd" >
      |    <channel  rdf:about = "http://www.ecb.europa.eu/rss/gbp.html">
      |        <title>ECB | Pound sterling (GBP) - Euro foreign exchange reference rates</title>
      |        <link>http://www.ecb.europa.eu/home/html/rss.en.html</link>
      |        <description>The reference rates are based on the regular daily concertation procedure between central banks within and outside the European System of Central Banks, which normally takes place at 2.15 p.m. (14:15) ECB time.</description>
      |        <items>
      |            <rdf:Seq>
      |                <rdf:li rdf:resource="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-27&amp;rate=0.83368" />
      |                <rdf:li rdf:resource="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-26&amp;rate=0.83458" />
      |                <rdf:li rdf:resource="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-25&amp;rate=0.83713" />
      |                <rdf:li rdf:resource="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-24&amp;rate=0.83803" />
      |                <rdf:li rdf:resource="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-21&amp;rate=0.83633" />
      |            </rdf:Seq>
      |        </items>
      |        <dc:publisher>European Central Bank</dc:publisher>
      |        <dcterms:license>http://www.ecb.europa.eu/home/html/disclaimer.en.html</dcterms:license>
      |    </channel>
      |    <item rdf:about="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-27&amp;rate=0.83368">
      |        <title xml:lang="en">0.83368 GBP = 1 EUR 2022-01-27 ECB Reference rate</title>
      |        <link>http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-27&amp;rate=0.83368</link>
      |        <description xml:lang="en">1 EUR buys 0.83368 Pound sterling (GBP) - The reference exchange rates are published both by electronic market information providers and on the ECB's website shortly after the concertation procedure has been completed. Reference rates are published according to the same  calendar as the TARGET system.</description>
      |        <dc:date>2022-01-27T14:15:00+01:00</dc:date>
      |        <dc:language>en</dc:language>
      |        <cb:statistics>
      |            <cb:country>U2</cb:country>
      |            <cb:institutionAbbrev>ECB</cb:institutionAbbrev>
      |            <cb:exchangeRate>
      |                <cb:value frequency="daily" decimals="5">0.83368</cb:value>
      |                <cb:baseCurrency unit_mult="0">EUR</cb:baseCurrency>
      |                <cb:targetCurrency>GBP</cb:targetCurrency>
      |                <cb:rateType>Reference rate</cb:rateType>
      |            </cb:exchangeRate>
      |        </cb:statistics>
      |    </item>
      |    <item rdf:about="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-26&amp;rate=0.83458">
      |        <title xml:lang="en">0.83458 GBP = 1 EUR 2022-01-26 ECB Reference rate</title>
      |        <link>http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-26&amp;rate=0.83458</link>
      |        <description xml:lang="en">1 EUR buys 0.83458 Pound sterling (GBP) - The reference exchange rates are published both by electronic market information providers and on the ECB's website shortly after the concertation procedure has been completed. Reference rates are published according to the same  calendar as the TARGET system.</description>
      |        <dc:date>2022-01-26T14:15:00+01:00</dc:date>
      |        <dc:language>en</dc:language>
      |        <cb:statistics>
      |            <cb:country>U2</cb:country>
      |            <cb:institutionAbbrev>ECB</cb:institutionAbbrev>
      |            <cb:exchangeRate>
      |                <cb:value frequency="daily" decimals="5">0.83458</cb:value>
      |                <cb:baseCurrency unit_mult="0">EUR</cb:baseCurrency>
      |                <cb:targetCurrency>GBP</cb:targetCurrency>
      |                <cb:rateType>Reference rate</cb:rateType>
      |            </cb:exchangeRate>
      |        </cb:statistics>
      |    </item>
      |    <item rdf:about="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-25&amp;rate=0.83713">
      |        <title xml:lang="en">0.83713 GBP = 1 EUR 2022-01-25 ECB Reference rate</title>
      |        <link>http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-25&amp;rate=0.83713</link>
      |        <description xml:lang="en">1 EUR buys 0.83713 Pound sterling (GBP) - The reference exchange rates are published both by electronic market information providers and on the ECB's website shortly after the concertation procedure has been completed. Reference rates are published according to the same  calendar as the TARGET system.</description>
      |        <dc:date>2022-01-25T14:15:00+01:00</dc:date>
      |        <dc:language>en</dc:language>
      |        <cb:statistics>
      |            <cb:country>U2</cb:country>
      |            <cb:institutionAbbrev>ECB</cb:institutionAbbrev>
      |            <cb:exchangeRate>
      |                <cb:value frequency="daily" decimals="5">0.83713</cb:value>
      |                <cb:baseCurrency unit_mult="0">EUR</cb:baseCurrency>
      |                <cb:targetCurrency>GBP</cb:targetCurrency>
      |                <cb:rateType>Reference rate</cb:rateType>
      |            </cb:exchangeRate>
      |        </cb:statistics>
      |    </item>
      |    <item rdf:about="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-24&amp;rate=0.83803">
      |        <title xml:lang="en">0.83803 GBP = 1 EUR 2022-01-24 ECB Reference rate</title>
      |        <link>http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-24&amp;rate=0.83803</link>
      |        <description xml:lang="en">1 EUR buys 0.83803 Pound sterling (GBP) - The reference exchange rates are published both by electronic market information providers and on the ECB's website shortly after the concertation procedure has been completed. Reference rates are published according to the same  calendar as the TARGET system.</description>
      |        <dc:date>2022-01-24T14:15:00+01:00</dc:date>
      |        <dc:language>en</dc:language>
      |        <cb:statistics>
      |            <cb:country>U2</cb:country>
      |            <cb:institutionAbbrev>ECB</cb:institutionAbbrev>
      |            <cb:exchangeRate>
      |                <cb:value frequency="daily" decimals="5">0.83803</cb:value>
      |                <cb:baseCurrency unit_mult="0">EUR</cb:baseCurrency>
      |                <cb:targetCurrency>GBP</cb:targetCurrency>
      |                <cb:rateType>Reference rate</cb:rateType>
      |            </cb:exchangeRate>
      |        </cb:statistics>
      |    </item>
      |    <item rdf:about="http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-21&amp;rate=0.83633">
      |        <title xml:lang="en">0.83633 GBP = 1 EUR 2022-01-21 ECB Reference rate</title>
      |        <link>http://www.ecb.europa.eu/stats/exchange/eurofxref/html/eurofxref-graph-gbp.en.html?date=2022-01-21&amp;rate=0.83633</link>
      |        <description xml:lang="en">1 EUR buys 0.83633 Pound sterling (GBP) - The reference exchange rates are published both by electronic market information providers and on the ECB's website shortly after the concertation procedure has been completed. Reference rates are published according to the same  calendar as the TARGET system.</description>
      |        <dc:date>2022-01-21T14:15:00+01:00</dc:date>
      |        <dc:language>en</dc:language>
      |        <cb:statistics>
      |            <cb:country>U2</cb:country>
      |            <cb:institutionAbbrev>ECB</cb:institutionAbbrev>
      |            <cb:exchangeRate>
      |                <cb:value frequency="daily" decimals="5">0.83633</cb:value>
      |                <cb:baseCurrency unit_mult="0">EUR</cb:baseCurrency>
      |                <cb:targetCurrency>GBP</cb:targetCurrency>
      |                <cb:rateType>Reference rate</cb:rateType>
      |            </cb:exchangeRate>
      |        </cb:statistics>
      |    </item>
      |</rdf:RDF>
      |""".stripMargin

}
