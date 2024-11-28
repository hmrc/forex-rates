
# forex-rates

This API retrieves and stores exchange rate data from the [ECB RSS](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html) feed.
The exchange rates are published around 16:00 CET every working day.
This API retrieves the rates daily at 16:05. This time is defined in the application.conf by a [cron expression](https://github.com/hmrc/forex-rates/blob/main/conf/application.conf#:~:text=from%20RSS%20feed%22-,expression,-%3D%20%220_5_16_*_*_). The rates are retrieved for the last 5 days and are saved in the database unless they've already been stored.


Resources
----------

| Method | URL                                           | Description                                                             | Example response |
| :----: |-----------------------------------------------|-------------------------------------------------------------------------|------------------|
| GET    | /rates/{date}/EUR/{targetCurrency}            | Retrieves exchange rate of EURO to targetCurrency for the specified date.|<pre>{<br>"date":"2021-10-31",<br>"baseCurrency":"EUR",<br>"targetCurrency":"GBP",<br>"value":0.8<br>}</pre>|
| GET    | /rates/{date}/{baseCurrency}/EUR              | Retrieves exchange rate of baseCurrency to EURO on the specified date.  |<pre>{<br>"date":"2021-10-31",<br>"baseCurrency":"GBP",<br>"targetCurrency":"EUR",<br>"value":1.1<br>}</pre>|
| GET    | /rates/{dateFrom}/{dateTo}/EUR/{targetCurrency}  | Retrieves exchange rates of EURO to targetCurrency on the specified date range.  |<pre>{<br>[<br>{<br>"date":"2021-10-12",<br>"baseCurrency":"EUR",<br>"targetCurrency":"GBP",<br>"value":0.8<br>},<br>{<br>"date":"2021-10-13",<br>"baseCurrency":"EUR",<br>"targetCurrency":"GBP",<br>"value":0.8<br>}]<br>}</pre>|
| GET    | /rates/{dateFrom}/{dateTo}/{baseCurrency}/EUR  | Retrieves exchange rates of baseCurrency to EURO on the specified date range.  |<pre>{<br>[<br>{<br>"date":"2021-10-12",<br>"baseCurrency":"BGP",<br>"targetCurrency":"EUR",<br>"value":1.1<br>},<br>{<br>"date":"2021-10-13",<br>"baseCurrency":"GBP",<br>"targetCurrency":"EUR",<br>"value":1.1<br>}]<br>}</pre>|
| GET    | /latest-rates/{numberOfRates}/EUR/{targetCurrency}| Retrieves the specified number of latest exchange rates of EURO to targetCurrency.|<pre>{<br>[<br>{<br>"date":"2021-10-12",<br>"baseCurrency":"EUR",<br>"targetCurrency":"GBP",<br>"value":0.8<br>},<br>{<br>"date":"2021-10-13",<br>"baseCurrency":"EUR",<br>"targetCurrency":"GBP",<br>"value":0.8<br>}]<br>}</pre>|
| GET    | /latest-rates/{numberOfRates}/{baseCurrency}/EUR  | Retrieves the specified number of latest exchange rates of baseCurrency to EURO .  |<pre>{<br>[<br>{<br>"date":"2021-10-12",<br>"baseCurrency":"BGP",<br>"targetCurrency":"EUR",<br>"value":1.1<br>},<br>{<br>"date":"2021-10-13",<br>"baseCurrency":"GBP",<br>"targetCurrency":"EUR",<br>"value":1.1<br>}]<br>}</pre>|

Requirements
------------

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.

## Run the application locally via Service Manager

```
sm2 --start FOREX_RATES_ALL -r
```

### To run the application locally from the repository, execute the following:
```
sm2 --stop FOREX_RATES
```
and 
```
sbt run -Dapplication.router=testOnlyDoNotUseInAppConf.Routes
```
### To use the test-only endpoints:

The test-only endpoint triggers the retrieval of the exchange rates from the ECB RSS feed.

|Method |URI                                   |
|:----: |--------------------------------------|
|GET    | /forex-rates/test-only/retrieve-rates|


Unit and Integration Tests
------------

To run the unit and integration tests, you will need to open an sbt session on the browser.

### Unit Tests

To run all tests, run the following command in your sbt session:
```
test
```

To run a single test, run the following command in your sbt session:
```
testOnly <package>.<SpecName>
```

An asterisk can be used as a wildcard character without having to enter the package, as per the example below:
```
testOnly *CorrectionControllerSpec
```

### Integration Tests

To run all tests, run the following command in your sbt session:
```
it:test
```

To run a single test, run the following command in your sbt session:
```
it:testOnly <package>.<SpecName>
```

An asterisk can be used as a wildcard character without having to enter the package, as per the example below:
```
it:testOnly *CorrectionRepositorySpec
```


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
