
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


## Run the application

To update from Nexus and start all services from the RELEASE version instead of snapshot
```
sm --start ONE_STOP_SHOP_ALL -r
```

### To run the application locally execute the following:
```
sm --stop ONE_STOP_SHOP_RETURNS
```
and 
```
sbt 'run 10197'
```
### To use the test only endpoints:
```
sbt run -Dapplication.router=testOnlyDoNotUseInAppConf.Routes
```


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
