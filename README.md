# YMmoneyTransfer
The app implements p2p transfer for Yandex Money.

In order to launch, insert your "client_id" into com.mairos.ymoneytransfer.network.Constants.
Where to get: https://money.yandex.ru/myservices/new.xml.

Common instructions: https://tech.yandex.ru/money/doc/dg/concepts/About-docpage/.
"/oauth/token", "/api/request-payment" and "/api/process-payment" were implemented (interface com.mairos.ymoneytransfer.network.YMoneyApi).

Build: gradle:1.3.0