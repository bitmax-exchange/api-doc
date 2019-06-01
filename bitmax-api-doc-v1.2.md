Bitmax | API Document v1.2
==============================================

Note: we changed our API URLs on 2018-08-24. Even though the old URLs still work, you are strongly recommended to use the new URLs as described on this page.

RESTful APIs: Bird's-eye View
---------------------------------------------

Method  | Auth? | URL                        | Description
------- | ----- | -------------------------- | ---------------------
GET     | N     | api/v1/assets              | List all assets 
GET     | N     | api/v1/products            | List all products
GET     | N     | api/v1/fees                | Current trading fees (mining)
GET     | N     | api/v1/quote               | Level 1 order book data 
GET     | N     | api/v1/depth               | Level 2 order book data
GET     | N     | api/v1/trades              | Market trades
GET     | N     | api/v1/ticker/24hr         | 24-hour rolling statistics of all products
GET     | N     | api/v1/barhist/info        | Bar history meta data
GET     | N     | api/v1/barhist             | Bar history data
GET     | Y*    | api/v1/user/info           | Basic user data (no need to provide account group)
GET     | Y     | api/v1/transaction         | Deposit/Withdraw history

Cash Account APIs

Method  | Auth? | URL                        | Description
------- | ----- | -------------------------- | ---------------------
GET     | Y     | api/v1/balance             | Cash account balance
GET     | Y     | api/v1/order/open          | List all cash account open orders
GET     | Y     | api/v1/order/<coid>        | Retrieve a cash order with order id
POST    | Y     | api/v1/order               | Place an order from the cash account
POST    | Y     | api/v1/order/batch         | Place multiple orders from the cash account
DELETE  | Y     | api/v1/order               | Cancel a cash account open order
DELETE  | Y     | api/v1/order/batch         | Cancel multiple orders from the cash account
DELETE  | Y     | api/v1/order/all           | Cancel all cash account open orders

Margin Account APIs

Method  | Auth? | URL                        | Description
------- | ----- | -------------------------- | ---------------------
GET     | N     | api/v1/margin/ref-price    | Reference prices (margin account)
GET     | Y     | api/v1/margin/balance      | Margin account balance
GET     | Y     | api/v1/margin/order/open   | List margin account open orders
GET     | Y     | api/v1/margin/order/<coid> | Retrieve a margin order with order id
POST    | Y     | api/v1/margin/order        | Place an order from the margin account
DELETE  | Y     | api/v1/margin/order        | Cancel a margin account open order

Cash & Margin Account APIs

Method  | Auth? | URL                        | Description
------- | ----- | -------------------------- | ---------------------
GET     | Y     | api/v2/order/history       | (v2) List historical orders (cash + margin)


Public RESTful APIs
---------------------------------------------

### Product Symbols

The exchange adopted format `baseAssetCode/quoteAssetCode` for product symbols. For instance, `ETH/BTC = 0.052` means the price of 1 ETH is 0.052 BTC. Althought not required,
it is recommended to use `ETH-BTC` in API paths to avoid polluting the path string.

### RESTful Entry Point

Public RESTful API entry point: `https://bitmax.io/api/<version-num>`.
Public RESTful API entry point for the test site: `https://bitmax-test.io/api/<version-num>`.

### List of all assets

    GET api/v1/assets

The API returns a List of object, each contains a list of asset property fields:

    // some fields are omitted for conciseness
    [
      {
        "assetCode":        "ETH",
        "assetName":        "Ethereum",
        "withdrawalFee":     0.0001,
        "minWithdrawalAmt":  0.1,
        "statusCode":       "Normal"     // enum: Normal, NotTrading
      },
      ...
    ]


### List all products

    GET api/v1/products

Successful response: a list of all product objects. Each product object contains three parts -
`product`, `baseAsset`, and `quoteAsset`.

    // some fields are omitted for conciseness
    [
        {
            "symbol" : "LBA/BTC",
            "baseAsset" : "LBA",
            "quoteAsset" : "BTC",
            "priceScale" : 8,  // maximum precision for order price allowed to place an order, see below for details
            "qtyScale" : 2,    // maximum precision for order quantity allowed to place an order, see below for details 
            "status" : "Normal"
        },
        ...
    ]

### Get Current Trading Fees

    GET api/v1/fees
    
Successful response:

    {
      "maker": {
        "mining": "0.001",
        "noMining": "0.001",
        "rebate": "0.0004"
      },
      "taker": {
        "mining": "0.001",
        "noMining": "0.0004"
      }
    }


### Market Quote (Level 1 Order Book Data) of One Product

    GET api/v1/quote?symbol=<symbol>

The query takes one parameter

* `symbol` - a valid sample. Example: `symbol=ETH-BTC`

Successful response:

    {
      "symbol":   "ETH/BTC",
      "bidPrice": "0.033048",
      "bidSize":  "1.56",
      "askPrice": "0.033057",
      "askSize":  "0.108"
    }

### Market Depth (Level 2 Order Book Data) of One Product

    GET api/v1/depth

The query takes two parameters:

* `symbol` - a valid symbol. Example `symbol=ETH-BTC`
* `n`      - number of levels to be included in the order book. `n` is currently limited to 100 or fewer. Example `n=10`

Successful response: an object consists of the inner-most `n` bid levels and `n` ask levels (top-of-the-book).

    {
       "m":      "depth",       // message type
       "s":      "ETH/BTC",     // symbol
       "ts":     1557422548511, // timestamp, 64-bit integer
       "seqnum": 604599926,     // sequence number, 64-bit integer 
       "asks": [                // ask levels (list of individual levels)
           ["9924", "59.16"],   // [price, quantity]
           ["9914", "95.04"],
           ...
       ],
       "bids": [                // bid levels (list of individual levels)
           ["14180", "60.17"],  // [price, quantity]
           ["14190", "13.39"],
           ...
       ]
    }

Each `depth` message contains a timestamp `ts` field and a sequence number `seqnum` field. 

* timestamp (`ts`) - the UTC timestamp in milliseconds of the latest depth update contained in the message.
* sequence number (`seqnum`) - the sequence number is a strictly increasing integer number for each depth update assigned by the server. In the message, it is the `seqnum` of the lastest depth update.

Both timestamp and sequence number are set for each symbol. Please do not compare values from difference symbols. Note that these fields are **experimental** and may be changed in the future.


### Market Trades

    GET api/v1/trades

The query takes two parameters:

* `symbol` - a valid symbol. Example `symbol=ETH-BTC`
* `n`      - number of trades to be included in the response. `n` is currently limited to 100 or fewer. Example `n=10`

Successful response: an object containing a list of recent trades.

    {
      "m": "marketTrades",       // message type
      "s": "ETH/BTC",            // symbol
      "trades": [
        {
          "p":  "13.75",         // price
          "q":  "6.68",          // quantity
          "t":  1528988084944,   // timestamp
          "bm": False            // if true, the buyer is the market maker
        },
        {
          "p":  "13.75",         // price
          "q":  "6.68",          // quantity
          "t":  1528988084944,   // timestamp
          "bm": False            // if true, the buyer is the market maker
        },
        ...
      ]
    }


### 24-hour Rolling Statistics of All Products

    GET api/v1/ticker/24hr

Successful response: list of objects containing 24-hour rolling statistics for all products.

    [
       {  
          "symbol":       "LTC/ETH",
          "interval":     "1d",
          "barStartTime":  1536869696784,
          "openPrice":    "10.32",
          "closePrice":   "9.83",
          "highPrice":    "11.9",
          "lowPrice":     "7.23",
          "volume":       "123.45"
       }
    ]


### 24-hour Rolling Statistics of one Product

    GET api/v1/ticker/24hr?symbol=<sym>

The query takes one parameter:

* `symbol` - a valid symbol. Example `symbol=ETH-BTC`

Successful response: the 24-hour rolling statistics of the product specified.

    {  
      "symbol":       "LTC/ETH",
      "interval":     "1d",
      "barStartTime":  1536869696784,
      "openPrice":    "10.32",
      "closePrice":   "9.83",
      "highPrice":    "11.9",
      "lowPrice":     "7.23",
      "volume":       "123.45"
    }


### Bar History 

#### Bar History Info

    GET api/v1/barhist/info

Successful response: list of all intervals currently supported by the exchange.

    [
      "1",   //   1 minute 
      "5",   //   5 minutes
      "15",  //  15 minutes
      "30",  //  30 minutes
      "60",  //  60 minutes
      "120", // 120 minutes
      "240", // 240 minutes
      "360", // 360 minutes
      "720", // 720 minutes
      "1d",  //   1 day 
      "1w",  //   1 week 
      "1m",  //   1 month
    ]

#### Bar History Data

    GET api/barhist/

Parameters:

    FieldName    Example         Description
    ---------    -------         -----------
    symbol       ETH-BTC         the product symbol
    from         1539654780000   start time, milliseconds since UNIX epoch in UTC
    to           1539645600000   end time, milliseconds since UNIX epoch in UTC
    interval     1               the length of the bar

Requesting more than 1000 bars might result in error.

Successful response: list of bars from `from` to `to`:

    [
      {
        "m":  "bar",          // message 
        "s":  "ETH/BTC",      // symbol
        "ba": "ETH",          // base asset 
        "qa": "BTC",          // quote asset 
        "i":  "1",            // interval: 1/5/30/60/360/1d
        "t":  1531911360000,  // time
        "o":  "0.00745",      // open
        "c":  "0.00762",      // close 
        "h":  "0.00771",      // high
        "l":  "0.00742",      // low 
        "v":  "12.334"        // volume 
      },
      ...
    ]


### Reference Prices (Margin)

    GET api/v1/margin/ref-price
    GET api/v1/margin/ref-price/<asset>  // for price of the given asset

Reference prices are used by BitMax to calculate the risk profile of margin accounts (including asset borrowable amount, transferable amount, and account's leverage, etc.). 

Successful response: 
    
    {
        "BTMX":   "0.066125218",
        "ZEC":    "51.82",
        "USDC":   "1.007",
        "XRP":    "0.311085",
        ...
    }



Authenticated RESTful APIs
----------------------------------------------

Private RESTful API entry point: `https://bitmax.io/<account-group>/api/<version-num>` (see API Entry Point section below for details)

### Authentication

To use the authenticated RESTful APIs, you need to first apply for an API key and a secret key to sign the message. You should
include the API key in each of your request along with a signature signed using the secret key. Please don't share the secret
key with anyone. (Please contact us to obtain your keys.)

Each authenticated request must include the following fields in the header:

* `x-auth-key` - the API key
* `x-auth-signature` - the message signed using __sha256__ using the __base64-decoded__ secret key
  on the prehash string `{timestamp}+{api_path}`, or `{timestamp}+{api_path}+{coid}` for placing/canceling orders.
* `x-auth-timestamp` - milliseconds since UNIX epoch in UTC
* `x-auth-coid` - this field is only required when placing a new order or canceling an order, see below for details

For instance, to query all your balance through api path `balances` at `2018-06-26 20:49:34.012 UTC` (timestamp=`1530046174012`),
you could obtain the signature by applying the sha256 algorithm to string `1530046174012+balances`. The signing process can be implemented
in python 3.6+ as:

    # python 3.6+
    import hmac, hashlib, base64

    key = "eb1vhzkNbG...Gw19EsWP6x"  # a secret key that you should
                                     # never share with others

    msg = bytearray("1530047198600+balances".encode("utf-8"))

    hmac_key = base64.b64decode(key)
    signature = hmac.new(hmac_key, msg, hashlib.sha256)
    signature_b64 = base64.b64encode(signature.digest()).decode("utf-8")  

    print(signature_b64) # the signature of interest

__Remark__: to place a new order or to cancel an order, you should include order ID in the message:

    coid = "lx3r...R9Lo"
    msg = bytearray("1530047198600+order+{}".format(coid).encode("utf-8"))

__Remark__: to place or cancel multiple orders, you should include all order IDs in the message:

    coids = "+".join(["lx3r...R9Lo", "ck8e...pE91", "Xlds...1Sce"])
    msg = bytearray("1530047198600+order/batch+{}".format(coids).encode("utf-8"))

Please note that we will switch to a new signing method from 2018-09-26 onward. Although you can still use the algorithm above, you are encouraged to switch 
to the new signing method.

    # python 3.6+
    import hmac, hashlib, base64

    key = "eb1vhzkNbG...Gw19EsWP6x"  # a secret key that you should never share with others

    msg = bytearray("1530047198600+balances".encode("utf-8"))

    hmac_key = bytearray(secret.encode("utf-8"))  
    signature = base64.b64encode(hmac.new(hmac_key, msg, digestmod=hashlib.sha256).digest())

    print(signature) # the signature of interest


### API Entry Point

BitMax assign dedicated servers to users within the same **account group**. This greatly increases the per-user throughput of each server.
API users are expected to specify the account group in the query URL in order to connect to the desired server. Otherwise, the request
will be rejected.

The API entry point for authenticated APIs is `https://bitmax.io/<account-group>/api`. For instance, the user with account group 3 should
use `https://bitmax.io/3/api` to query authenticated data.

Account group is expected stay the same over time. However, it is recommended that API users check the account group at the begining of the
program. Refer to `GET user/info` for how to get account group.  

### API Calls with Input Data

Some API calls require user input data. The server only accept JSON as input type, please make sure you specify the data type in the request header:

    Content-Type: application/json

### User Info (`api_path=user/info`)

    GET api/v1/user/info

Successful response: an object with basic user information.

    {
      "accountGroup": 5
    }

### List all Balances of the Cash Account (`api_path=balance`)

    GET <account-group>/api/v1/balance

Successful response: a list of all your current balances.

    {
      "code": 0,
      "status": "success",     // this field will be deprecated soon
      "email": "foo@bar.com",  // this field will be deprecated soon
      "data": [
        {
            "assetCode":       "TSC",
            "assetName":       "Ethereum",
            "totalAmount":     "20.03",    // total balance amount
            "availableAmount": "20.03",    // balance amount available to trade
            "inOrderAmount":   "0.000",    // in order amount
            "btcValue":        "70.81"     // the current BTC value of the balance 
                                           // ("btcValue" might not be available when price is missing)
        },
        ...
      ]
    }

### Get Balance of one Asset of the Cash Account (`api_path=balance`)

    GET <account-group>/api/v1/balance/<asset>

Successful response: one object with current balance data of the asset specified.

    {
      "code": 0,
      "status": "success",     // this field will be deprecated soon
      "email": "foo@bar.com",  // this field will be deprecated soon
      "data": {
          "assetCode":       "TSC",
          "assetName":       "Ethereum",
          "totalAmount":     "20.03",    // total balance amount
          "availableAmount": "20.03",    // balance amount available to trade
          "btcValue":        "70.81"     // the current BTC value of the balance
        }
    }


### List all Balances of the Margin Account (`api_path=margin/balance`)

    GET <account-group>/api/v1/margin/balance

Successful response: a list of all your current balances.

    {
      "code": 0,
      "status": "success",     // this field will be deprecated soon
      "email": "foo@bar.com",  // this field will be deprecated soon
      "data": [
        {
            "assetCode":       "USDC",           
            "totalAmount":     "0",
            "availableAmount": "0",       
            "borrowedAmount":  "0",       // Amount borrowed
            "interest":        "0",       // Interest owed 
            "interestRate":    "0.0003",  // Interest rate 
            "maxBorrowable":   "0",       // maximum amount the user can borrow the current asset (in addition to the current borrowed amount)
            "maxSellable":     "0",       // maximum amount the user can sell the current asset
            "maxTransferable": "0"        // maximum amount the user can transfer to his/her cash account
        },       
        ...
      ]
    }

We added a special assetCode `BTMXD` as the amount of BTMX locked. The `availableAmount` indicates the amount of `BTMX` locked, and the `totalAmount` is the sum 
of locked `BTMX` and the amount of `BTMX` that was requested for unlock (which will be converted to floating `BTMX` in no longer than 24 hours). 


### Get Deposit/Withdraw History (`api_path=transaction`)

    GET <account-group>/api/v1/transaction

Parameters:

    FieldName    Example         Description
    ---------    -------         -----------
    assetCode    "BTC"           Optional
    page         1               the page index, starts at 1
    pageSize     10              the page size
    txType       "deposit"       Optional, accepted values: deposit, withdrawal

Successful response contains:

    {
      'page':     1,
      'pageSize': 2,
      'hasNext':  False, 
      'data': [
        {
          'time': 1539125157899,
          'asset': 'BTC',
          'transactionType': 'withdrawal',
          'amount':          '1.00000000',
          'commission':      '0.00000000',
          'networkTransactionId': '12E729B2B0283170C5D61B2EAF672186060EDE4D9EF8FDA78DEE2A76BD5B07A6',
          'status': 'completed'
        },
        ...
      ]
    }



### Orders

#### Stages of an Order's Life Cycle 

An order may be in one of the following `status`:

* `PendingNew` - only available for stop orders.  
* `New`
* `PartiallyFilled`
* `Filled`
* `Canceled`
* `Rejected` 

#### Place a New Order 

    POST <account-group>/api/v1/order  // api_path="order", placing order from the cash account 
    POST <account-group>/api/v1/margin/order  // api_path="margin/order", placing order from the margin account

For this API you must include `x-auth-coid` in your request header.

Request body schema: `application/json`

    FieldName    FieldType    Example         Description
    ---------    ---------    -------         -----------
    coid         string       "xxx...xxx"     a unique identifier of length 32
    time         long         1528988100000   milliseconds since UNIX epoch in UTC  
    symbol       string       "ETH/BTC"       
    orderPrice   string       "13.5"          optional, limit price of the order. This field is required for limit orders and stop limit orders. 
    stopPrice    string       "15.7"          optional, stop price of the order. This field is required for stop market orders and stop limit orders.
    orderQty     string       "3.5"           
    orderType    string       "limit"         order type, you shall specify one of the following: "limit", "market", "stop_market", "stop_limit".
    side         string       "buy"           "buy" or "sell"
    postOnly     boolean      true            Optional, if true, the order will either be posted to the limit order book or be cancelled, i.e. the order cannot take liquidity; default value is false
    timeInForce  string       "GTC"           Optional, default is "GTC". Currently, we support "GTC" (good-till-canceled) and "IOC" (immediate-or-cancel). 

Each request should contain a unique identifier `coid`. `coid` is no more than 32-charaters and consists of only lower case characters (`a-z`),
upper case characters (`A-Z`) and digits (`0-9`).  

Please note that you should be careful about decimal places for `orderPrice`, `stopPrice`, and `orderQty`. The number of decimal places for `orderPrice`/`stopPrice` should not exceed `priceScale`, and that for `orderQty` should not exceed `qtyScale`, otherwise, they will be rounded. (`priceScale`/`qtyScale` for each symbol can be obtained from API `GET api/v1/products`.)

Each request should also specify `time` - the request time as the total milliseconds since UNIX epoch in UTC. Requests placed more than 30 seconds ago are treated as expired and will not be processed.

The table below shows how to correctly configure order of different types: (o - required, x - optional)

Field       | Market | Limit  | StopMarket | StopLimit
----------- | ------ | ------ | ---------- | ----------
coid        |   o    |   o    |     o      |     o
time        |   o    |   o    |     o      |     o
symbol      |   o    |   o    |     o      |     o
orderPrice  |        |   o    |            |     o 
orderQty    |   o    |   o    |     o      |     o
orderType   |   o    |   o    |     o      |     o
side        |   o    |   o    |     o      |     o
postOnly    |        |   x    |            |          
stopPrice   |        |        |     o      |     o
timeInForce |        |   x    |            |          

Success response:

    {
      "code": 0,
      "email": "foo@bar.com",  // this field will be deprecated soon
      "status": "success",     // this field will be deprecated soon
      data: {
        "coid": "xxx...xxx",
        "action": "new",
        "success": true  // success = true means the order has been submitted to the matching engine. 
      }
    }

Example of a failed response:

    {
      'code': 6010, 
      'message': 'Not enough balance.'
    }

Response code `200 OK` means the order has been received by the server. However, it doesn't imply that the order has been successfully 
submitted to the matching engine. API users should use the `success: boolean` field in the API response to check if the order has been successfully passed to the system. For instance, if one doesn't have enough balance, he/she will get `success = false`.

Even if the order has been passed to the matching engine, it might still be rejected. For example, postOnly order can be rejected if the order would otherwise take liquidity. API users should use the list open order API to monitor the status of the order placed. (see below)

#### Placing Multiple Orders from the Cash Account (`api_path=order/batch`)

You may combine multiple orders into one request

    POST <account-group>/api/v1/order/batch

For this API, you must include `x-auth-coid` in your request header. You must concatenate IDs of all orders with character `+`. The order of IDs in the header 
must match the orders in the request. 

You may submit up to 10 orders at a time. Server will respond with `InvalidRequest` if you submit more than 10 orders.

Request body: `application/json`

    {
      "orders": [
        {
          "coid":       "xxx...xxx",
          "time":       1528988100000,  // timestamp, must be the same for all orders 
          "symbol":     "ETH/BTC",
          "orderPrice": "13.5",
          "orderQty":   "3.5",
          "orderType":  "limit",
          "side":       "buy"
        },
        ...
      ]
    }

Successful response: an object containing a list of `(symbol, orderId)` 

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      'data': [['ZIL/BTC', 'Ykfs1bh5jhsT0ON0XTSNthoXkwuQ7UOy'],
               ['ZIL/BTC', 'Ch2bu4QHLtxLExDcSIR7e3m25UUoudya'],
               ['ZIL/BTC', 'AUYsHr8oeyPoUUE9Vi6W7dQOZqueDiVt']]
    }



#### Cancel an Order

    DELETE <account-group>/api/v1/order         // api_path="order", cancel an order from the cash account
    DELETE <account-group>/api/v1/margin/order  // api_path="margin/order", cancel an order from the margin account

For this API you must include `x-auth-coid` in your request header. Note that you must use the cash account API to cancel cash orders and margin 
account API to cancel margin orders.

Request body schema: `application/json`

    FieldName   FieldType    Example         Description
    ---------   ---------    -------         -----------
    coid        string       "xxx...xxx"     a unique identifier, see POST api/order for details

    origCoid    string       "yyy...yyy"     the coid of the order to cancel

    time        long         1528988100000   milliseconds since UNIX epoch in UTC,
                                             see POST api/order for details

    symbol      string       "ETH/BTC"

You must correclty specify the `origCoid` in order to cancel an open order. The exchange will reject the request if it cannot find
any open order using the provided `origCoid`.

Response code `200 OK` means the order has been placed successfully in our system. API users should use websocket to monitor the
status of the order placed.

Successful response:

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      'data': {'action': 'cancel',
               'coid': 'gaSRTi3o3Yo4PaXpVK0NSLP47vmJuLea',
               'success': True}
    }

Example of a failed response:

    {
      'code': 60060, 
      'message': 'The order is already filled or canceled.'
    }

#### Cancel Multiple Orders (`api_path=order/batch`)

You may delete multiple orders in the single request:

    DELETE <account-group>/api/v1/order/batch

For this API you must include `x-auth-coid` in your request header. You must concatenate IDs of all orders with character `+`. The order of IDs in the header 
must match the orders in the request. 

You may submit up to 10 orders at a time. Server will respond with `InvalidRequest` if you submit more than 10 orders.

Request body: `application/json`

    {
      "orders": [
        {
          "coid":       "xxx...xxx",
          "origCoid":   "yyy...yyy",
          "time":       1528988100000,  // timestamp, must be the same for all orders 
          "symbol":     "ETH/BTC",
        },
        ...
      ]
    }

Successful response: an object containing a list of `(symbol, orderId)` 

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      "data": [
        ["ETH/BTC", "xxx...xxx"],
        ...
      ]
    }

#### Cancel All Open Orders (`api_path=order/all`)

    DELETE <account-group>/api/v1/order/all

This query sends cancel request for all open orders as specified by the user. 

Parameters

* `symbol` - optional string field. Example `symbol=ETH-BTC`.
* `side`   - optional string field (case-insensitive). Either "buy" or "sell".


#### List of All Open Orders

    GET <account-group>/api/v1/order/open         // api_path="order/open", list all open orders of the cash account
    GET <account-group>/api/v1/margin/order/open  // api_path="margin/order/open", list all open orders of the margin account

Successful response: List of all your open orders. (Filtering by symbol will be supported in the next release)

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      "data": [
        {
          "time":        1528988100000,
          "coid":        "xxx...xxx",     // the unique identifier, you will need
                                          // this value to cancel this order
          "symbol":      "ETH/BTC",
          "baseAsset":   "ETH",
          "quoteAsset":  "BTC",
          "side":        "buy",
          "orderPrice":  "13.45",         // only available for limit and stop limit orders
          "stopPrice":   "20.05",         // only available for stop market and stop limit orders
          "orderQty":    "3.5",
          "filled":      "1.5",           // filled quantity
          "fee":         "0.00012",       // cumulative fee paid for this order
          "feeAsset":    "ETH",           // the asset
          "status":      "PartiallyFilled"
        },
        ...
      ]
    }

### List Historical Orders (`api_path=order/history`)

    GET <account-group>/api/v2/order/history

(Experimental: the parameters below are subject to change)

Parameters:

* `symbol` - optional, string type. 
* `category` - optional, string type. 
* `orderType` - optional, string type. 
* `page` - optional, integer type. Page starts at 1.
* `pageSize` - optional, integer type. 
* `side` - optional, string type. buy or sell, case insensitive.
* `startTime` - optional, integer type. Milliseconds since UNIX epoch representing the start of the range
* `endTime` - optional, integer type. Milliseconds since UNIX epoch representing the end of the range
* `status` - optional, string type. Status can only be one of "Filled", "Canceled", "Rejected".

Please note that we exclude all canceled / rejected orders with no fill from the result set. 

Successful response: list of all your orders history, (current open orders are not included.)

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      'data': {
        'page': 1,
        'pageSize': 20,
        'limit': 500,
        'hasNext': False,
        'data': [
          {
            'time':             1553100213658,
            'coid':            'QgQIMJhPFrYfUf60ZTihmseTqhzzwOCx',
            'execId':          '331',
            'symbol':          'BTMX/USDT',
            'orderType':       'Market',
            'baseAsset':       'BTMX',
            'quoteAsset':      'USDT',
            'side':            'Buy',
            'stopPrice':       '0.000000000',     // only meaningful for stop market and stop limit orders
            'orderPrice':      '0.123000000',     // only meaningful for limit and stop limit orders
            'orderQty':        '9229.409000000',  
            'filledQty':       '9229.409000000',
            'avgPrice':        '0.095500000',
            'fee':             '0.352563424',
            'feeAsset':        'USDT',
            'btmxCommission':  '0.000000000',
            'status':          'Filled',
            'notional':        '881.408559500',
            'userId':          '5DNEppWy33SayHjFQpgQUTjwNMSjEhD3',
            'accountId':       'ACPHERRWRIA3VQADMEAB2ZTLYAXNM3PJ',
            'accountCategory': 'CASH',
            'errorCode':       'NULL_VAL',
            'execInst':        'NULL_VAL'
          },
          ...
        ]
      }
    }


Remark, the query returns the latest `n` orders within the specified range. To query more history, use the timestamp of the oldest order as the new `endTime` and run the query again. 

#### Get Basic Order Data of One Order (`api_path=order`)

    GET <account-group>/api/v1/order/<coid>

Successful response: basic data of an open orders.

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      "data": {
          "time":        1528988100000,
          "coid":        "xxx...xxx",     // the unique identifier, you will need
                                          // this value to cancel this order
          "symbol":      "ETH/BTC",
          "baseAsset":   "ETH",
          "quoteAsset":  "BTC",
          "side":        "buy",
          "orderPrice":  "13.45",         // only available for limit and stop limit orders
          "stopPrice":   "20.05",         // only available for stop market and stop limit orders
          "orderQty":    "3.5",
          "filled":      "1.5",           // filled quantity
          "fee":         "0.00012",       // cumulative fee paid for this order
          "feeAsset":    "ETH",           // the asset
          "status":      "PartiallyFilled"
        }
    }

#### Get Fills of One Order (`api_path=order/fills`)

    GET <account-group>/api/v1/order/fills/<coid>

Successful response: list of all fills of the order specified.

    {
      'code': 0,
      'status': 'success',     // this field will be deprecated soon
      'email': 'foo@bar.com',  // this field will be deprecated soon
      "data": {
          "time":        1528988100000,
          "coid":        "xxx...xxx",     // the unique identifier, you will need
                                          // this value to cancel this order
          "symbol":      "ETH/BTC",
          "baseAsset":   "ETH",
          "quoteAsset":  "BTC",
          "side":        "buy",
          "orderPrice":  "13.45",         // only available for limit and stop limit orders
          "stopPrice":   "20.05",         // only available for stop market and stop limit orders
          "orderQty":    "3.5",
          "filled":      "1.5",           // filled quantity
          "fee":         "0.00012",       // cumulative fee paid for this order
          "feeAsset":    "ETH",           // the asset
          "status":      "PartiallyFilled"
        }
    }

### Get Deposit address of one Asset (`api_path=deposit`)

    POST <account-group>/api/v1/deposit/<asset>

Request body schema: `application/json`

    {
      "requestId": "xxx...xxx",     # a unique identifier  
      "time":       1528988100000,  # milliseconds since UNIX epoch in UTC  
      "assetCode": "ETH"
    }

You must sign the message `"<timestamp>+deposit+<requestId>"`, and set `x-auth-coid=<requestId>` in the header.

Successful response: one object with the the deposit address associated with the authenticated user.

    {
        "data": {
            "address": "0x481de74994f2ebf85d29b5462026af73b0d4e062"
        },
        "email": "<your-email-address>",
        "status": "success"
    }

### Withdrawal Request 

This API is currently unavailable. 


WebSocket API
----------------------------------------------

WebSocket entry point: 

* Public: `wss://bitmax.io/api/public/[symbol]`
* Authenticated (View and Trade permission needed): `wss://bitmax.io/<account-group>/api/stream/[symbol]`

Similiar to Authenticated servers, BitMax assign dedicated servers to stream data to users in the same account group  
via websocket. For instance, user in account group 3 will subscribe all `ETH-BTC` messages via:

    wss://bitmax.io/3/api/stream/[symbol]

**Note:** [symbol] in websocket connections must be seperated by a hyphen(-), e.g, ETH-BTC. Slash(/) is treated specially in URLs as path separators.

### Websocket Authentication

Connecting to websocket API follows almost the same authentication process as the authenticated RESTful APIs. You
need to add the following headers to your websocket request:

* `x-auth-key`
* `x-auth-signature` - the message signed using __sha256__ using the __base64-decoded__ secret key
  on the prehash string `{timestamp}+api/stream"`.  
* `x-auth-timestamp`

Once the websocket is connected, you need to send a `subscribe` message in order to start receiving data streams.
Currently we stream three type of messages.

* `depth` - market depth
* `marketTrades` - market trades
* `order` - client's own orders.

All webSocket messages are in JSON format and are very similar to RESTful APIs. However, the field names are shortened
to reduce message size.  


### Subscribe to WebSocket Streams

After connecting to websocket, you need to send an `subscribe` message in order to start receiving data streams.

    {
      "messageType":         "subscribe",  // message type

      "marketDepthLevel":    20,           // max number of price levels on
                                           // each side to be included in
                                           // the first market depth message  

      "recentTradeMaxCount": 20,           // max number of recent trades to
                                           // be included in the first market
                                           // trades message  
      
      "skipSummary":  false,  // optional, set to true if you don't want to
                              // receive summary messages, default false
      
      "skipBars":     false   // optional, set to true if you don't want to 
                              // receive bar messages, default false

    }


### Market depth

Each market depth message is a JSON object containing the current quantity at specific
prices levels. There is no direct way of getting the top-of-the-book data. You need to maintain the
current depth book and derive the best bid/ask. This can be done by two steps:

1. Use the first `depth` message to build the initial depth book.  
2. Use later messages to update the depth book. Messages contain the new total size at the
   indicated price level. You should replace the old quantity using message received. When the
   replacement quantity is zero, it means there is no order sitting on the corresponding price level.

All `depth` messages have the same structure, see [depth](https://github.com/bitmax-exchange/api-doc/blob/master/bitmax-api-doc-v1.2.md#market-depth-level-2-order-book-data-of-one-product) for more details.

    {
       "m":      "depth",        // message type
       "s":      "ETH/BTC",      // symbol
       "ts":     1557422548511,  // timestamp, 64-bit integer
       "seqnum": 604599926,      // sequence number, 64-bit integer 
       "asks": [                 // ask levels, could be empty
           ["13.45", "59.16"],   // price, quantity
           ["13.37", "95.04"],   
           ...
       ],
       "bids": [                 // bid levels, could be empty
           ["13.21", "60.17"],
           ["13,10", "13.39"],
           ...
       ]
    }


### Market Trades

Once you send the `subscribe` message, you will start receiving continuous market trade stream. All market trades messages
follow the same structure, which contains one or more trades.

    {
      "m": "marketTrades",      // message type
      "s": "ETH/BTC",           // symbol
      "trades": [
        {
          "p":  "13.75",         // price
          "q":  "6.68",          // quantity
          "t":  1528988084944,   // timestamp
          "bm": False            // if true, the buyer is the market maker
        },
        {
          "p":  "13.75",         // price
          "q":  "6.68",          // quantity
          "t":  1528988084944,   // timestamp
          "bm": False            // if true, the buyer is the market maker
        },
        ...
      ]
    }

### Market Summary

Each market summary data record contains current information about a single product. The data is streamed in batches - we stream 
out market data of all products every 30 seconds. 

    {
      "m":  "summary",
      "s":  "ETH/BTC",     // product symbol
      "ba": "ETH",         // base asset
      "qa": "BTC",         // quote asset 
      "i":  "1d",          // for market summary data, the interval is always 1d
      "t":  1528988000000, // timestamp in UTC
      "o":  "3.24",        // open
      "c":  "3.56",        // close
      "h":  "3.77",        // high
      "l":  "3.21",        // low
      "v":  "10.234",      // volume
    }


### Bar Chart Data

Bar data is almost the same as the market summary data, except that:
  
* Message type is `bar` 
* There is only one symbol per websocket session
* The interval field `i` may take multiple values: `1`, `5`, `30`, `160`, `360`, `1d`.  

We stream bar data in batches. Every 30 seconds, we stream bar data messages at all interval levels. You may use 
these data to update bar chart directly (replace bars). However, you should also update the bar chart using the 
market trade messages.  

    {
      "m":  "summary",
      "s":  "ETH/BTC",     // product symbol
      "ba": "ETH",         // base asset
      "qa": "BTC",         // quote asset 
      "i":  "5",           // 1/5/30/60/360/1d
      "t":  1528988500000, // timestamp in UTC
      "o":  "3.24",        // open
      "c":  "3.56",        // close
      "h":  "3.77",        // high
      "l":  "3.21",        // low
      "v":  "10.234",      // volume
    }


### Order Management

With our websocket API, you can monitor the status of all your active orders, place new orders, and
cancel existing orders.


#### Order updates

Once connected to websocket streams, you will start receiving real time updated of your own orders. It
contains both order execution report and current balances.

    {
      "m":        "order",            // message type
      "execId":   "12345",            // for each user, this is a strictly increasing long integer (represented as string)
      "coid":     "xxx...xxx",        // client order id, (needed to cancel order)
      "origCoid": "yyy...yyy",        // optional, the original order id, see canceling orders for more details
      "orderType":"Limit",            // Limit, Market, StopLimit, StopMarket 
      "s":        "ETH/BTC",          // symbol
      "t":         1528988100000,     // timestamp
      "p":        "13.45",            // limit price, only available for limit and stop limit orders
      "sp":       "14.5",             // stop price, only available for stop market and stop limit orders
      "q":        "3.5",              // order quantity
      "l":        "0.15",             // last quantity, the quantity executed by the last fill  
      "f":        "1.5",              // filled quantity, this is the aggregated quantity executed by all past fills
      "ap":       "13.45",            // average filled price
      "bb":       "10.00",            // base asset total balance
      "bpb":      "12.00",            // base asset pending balance
      "qb":       "1.0",              // quote asset total balance
      "qpb":      "0.8513",           // quote asset pending balance
      "fee":      "0.00012",          // fee
      "fa":       "BTC",              // fee asset
      "bc":       "0.0024",           // if possitive, this is the BTMX commission charged by reverse mining, if negative, this is the mining output of the current fill.
      "btmxBal":  "983",              // optional, the BTMX balance of the current account. This field is only available when bc is non-zero.
      "side":     "Buy",              // side
      "status":   "PartiallyFilled",  // order status
      "errorCode":"",                 // if the order is rejected, this field explains why
      "cat":      "CASH",             // account category: CASH/MARGIN
      "ei":       "POST"              // execution instruction               
    }

Since only new order updates will be streamed, it is recommendated that you load the initial snap of all you orders
using the RESTful API `GET api/v1/order/open`.


#### Place a New Order with WebSocket

To place a new order, you need to send to the server a `newOrderRequest`:

    {
       "messageType": "newOrderRequest",
       "time":         1528988100000, // current timestamp
       "coid":        "xxxx...xxx"    // a 32-character unique client order Id
       "symbol":      "ETH/BTC",      // symbol
       "orderPrice":  "2.23",         // order price
       "orderQty":    "34.3",         // order quantity
       "orderType":   "limit",        // always limit
       "side":        "buy",          // buy / sell
       "postOnly":     false,         // optional, boolean
       "stopPrice":   "2.50",         // optional, only needed for stop market and stop limit orders
       "timeInForce": "GTC"           // optional, default is "GTC", also supports "IOC"
    }

The client order Id field (`coid`) is a unique id to identify your order. Once the order
is successfully placed, this Id will be echod back in the `origCoid` field of the order update
message.


#### Cancel an Order with WebSocket

To cancel an order, you need send to the server a `cancelOrderRequest`:

    {
      "messageType": "cancelOrderRequest",   // message type
      "time":        1528988100000,          // current timestamp
      "coid":        "xxxx...xxx",           // a 32-character unique client order Id
      "origCoid":    "yyyy...yyy",           // the coid of the order to be canceled   
      "symbol":      "ETH/BTC"               // symbol
    }
