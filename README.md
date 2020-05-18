Bitmax | API Document
==============================================

FMZ是亚洲最大的数字货币量化交易平台。   
它支持JavaScript/Python/C++/My语言/可视化，并且支持了BitMax的API。  
点此查看：https://www.fmz.com/api


## Deprecation Warning:

<b>The BitMax Exchange will stop supporting these APIs from August 1, 2020 onward.</b>

APIs from this document have been <span style="color: red">deprecated</span>. Please use the BitMax Pro API instead.

BitMax Pro API is the latest release of APIs allowing our users to access the exchange programmatically. It is a major revision of the older releases. The BitMax team re-implemented the entire backend system in support for the BitMax Pro API. It is designed to be fast, flexible, stable, and comprehensive.

https://bitmax-exchange.github.io/bitmax-pro-api/#bitmax-pro-api-documentation

Older Releases
----------------------------------------------

### API v2 (Beta Version)

You are invited to use our v2 APIs for improved API design and better support for margin trading. Please note that v2 APIs are still in beta testing and are subject to futher improvements. 

Please visit: https://bitmax-exchange.github.io/BitMax-API-v2/#bitmax-api-documentation-v2


### v1.2 Release Note

[doc](bitmax-api-doc-v1.2.md)

* Added support for margin trading
* Added new API (v1) for obtaining bar history, with performance improvements.
* Added symbol and order side filter to the cancel all API.
* Added new API (v2) for retrieving historical orders. 

Older Releases
----------------------------------------------

### v1.1 Release Note

[doc](archive/bitmax-api-doc-v1.1.md)

* Added support for additional order types: market, stop_market, stop_limit
* Added new order Time-in-force instructions: good-till-canceled (GTC), immediate-or-cancel (IOC)

### v1.0 Release Note

[doc](archive/bitmax-api-doc-v1.0.md)

* First release
* Support RESTful and websocket APIs
