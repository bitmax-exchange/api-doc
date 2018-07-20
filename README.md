Bitmax | API Document
==============================================

Basics  
---------------------------------------------

### Product Symbols

The exchange adopted format `baseAssetCode/quoteAssetCode` for product symbols. For instance, `ETH/BTC = 13.25` means the price of 1 BTC is 13.25 ETH. Althought not required, 
it is recommended to use `ETH-BTC` in API paths to avoid polluting the path string.

### Data Format

All data exchanged are in JSON format, (except KYC ID verifiation, which uses multipart-form).


Public RESTful APIs
----------------------------------------------
 
RESTful API entry point: `https://bitmax.io/api/`

### Products

#### List of all assets

    GET api/assets

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


#### Get a single product

    GET api/product?symbol=ETH-BTC

The query takes one parameter `symbol` and returns a single object with three parts - `product`, `baseAsset`, and `quoteAsset`. 

    // some fields are omitted for conciseness
    {
      "product": {
        "symbol":         "ETH/BTC",
        "statusCode":     "Normal",  // enum: Normal, NotTrading
        "pricePrecision":  8,
      },
      "baseAsset":  { ... }  // same structure as in GET api/assets
      "quoteAsset": { ... }  // same structure as in GET api/assets
    }


#### List all products

    GET api/products 

The query returns a List of products. 

    [
      { ... }, // same structure as in GET api/product
      { ... },
      ...
    ]


### Market Depth

    GET api/marketdepth?symbol=ETH-BTC&n=10

The query takes two parameters:

* `symbol` - a valid symbols
* `n`      - number of levels to be included in the order book. `n` is currently limited to 100 or fewer. 

The query returns an object consists of the inner-most `n` bid levels and `n` ask levels (top-of-the-book). 

    {
       "m": "depth",            // message type
       "s": "ETH/BTC",          // symbol
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


### Market Trades 

    GET api/markettrades?symbol=ETH-BTC&n=10

The query takes two parameters:

* `symbol` - a valid symbol
* `n`      - number of trades to be included in the response. `n` is currently limited to 100 or fewer.

The query retuns an object containing a list of recent trades. 

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


Authenticated RESTful APIs
----------------------------------------------

### Authentication

To use the authenticated RESTful APIs, you need to first apply for an API key and a secret key to sign the message. You should 
include the API key in each of your request along with a signature signed using the secret key. Please don't share the secret
key with anyone. (Please contact us to obtain your keys.)

Each authenticated request must include the following fields in the header:

* `x-auth-key` - the API key 
* `x-auth-signature` - the message signed using __sha256__ using the __base64-decoded__ secret key 
  on the prehash string `{timestamp}+{api_path}`. 
* `x-auth-timestamp` - milliseconds since UNIX epoch in UTC

For instance, to query all your balance through api path `balances` at `2018-06-26 20:49:34.012 UTC` (timestamp=`1530046174012`), 
you could obtain the signature by applying the sha256 algorithm to string `1530046174012+balances`. The signing process can be implemented 
in python 3.6+ as:

    // python 3.6+
    import hmac, hashlib, base64

    key = "eb1vhzkNbG...Gw19EsWP6x"  // a secret key that you should
                                     // never share with others

    msg = bytearray("1530047198600+balances".encode("utf-8"))

    hmac_key = base64.b64decode(key)
    signature = hmac.new(hmac_key, msg, hashlib.sha256)
    signature_b64 = base64.b64encode(signature.digest()).decode("utf-8")  

    print(signature_b64) // the signature of interest

### API Response Format

One major difference between public RESTful API and the authenticated RESTful API is that the latter returns more structured objects. 
In the success case, the response will have status code 200 and the returned object will be:  

    {
      "status": "success",
      "data": ...   // the data of interest, either a JSON object or a JSON list 
    }

In case of failure, the response will have status code not equal to 200 and the returned object will be:  

    { 
      "status": "error",
      "msg": "{error message}"   // the actual error message. 
    }


### Balances (`api_path=balances`)

    GET api/balances

The API returns a list of all your current balances. 

    {
      "status": "success",
      "data": [
        {
            "assetCode":       "TSC",
            "assetName":       "Ethereum", 
            "totalAmount":     "20.03",    // total balance amount 
            "availableAmount": "20.03",    // balance amount available to trade 
            "btcValue":        "70.81"     // the current BTC value of the balance
        },
        ...
      ]
    }

### Orders 

#### Place a New Order (`api_path=order/new`)

    POST api/order/new 

Arguments:
    
    {
      "status": "success",
      "data": {
        "coid":        "xxx...xxx",    // a unique identifier  
        "time":        1528988100000,  // milliseconds since UNIX epoch in UTC  
        "symbol":      "ETH/BTC",      
        "orderPrice":  "13.5",          
        "orderQty":    "3.5",          
        "orderType":   "limit",        // currently we only support limit order
        "side":        "buy"           // buy or sell
      }
    }

Each request should contain a unique identifier `coid`. `coid` is no more than 32-charaters and consists of only lower case characters (`a-z`), 
upper case characters (`A-Z`) and digits (`0-9`).  

Each request should also specify `time` - the request time as the total milliseconds since UNIX epoch in UTC. Requests placed more than 30 seconds 
ago are deemed as expired and will not be processed. 


#### Cancel an Order (`api_path=order/cancel`)

    POST api/order/cancel  

Arguments 

    {
      "status": "success",
      "data": {
        "coid":      "xxx...xxx",    // a unique identifier, see POST api/order/new  
                                     // for details
        
        "origCoid":  "yyy...yyy"     // the coid of the order to cancel 

        "time":      1528988100000,  // milliseconds since UNIX epoch in UTC, 
                                     // see POST api/order/new for details 
        "symbol":    "ETH/BTC" 
      }
    }

You must correclty specify the `origCoid` in order to cancel an open order. The exchange will reject the request if it cannot find
any open order using the provided `origCoid`. 


#### List Open Orders (`api_path=orders/open`)

    GET api/orders/open?page=1&pagesize=50

The query returns a list order objects.

    {
      "status": "success"
      "data": [ 
        {
          "time":        1528988100000,
          "coid":        "xxx...xxx",     // the unique identifier, you will need 
                                          // this value to cancel this order
          "symbol":      "ETH/BTC",
          "baseAsset":   "ETH",
          "quoteAsset":  "BTC",
          "side":        "buy",
          "orderPrice":  "13.45",
          "orderQty":    "3.5",
          "filled":      "1.5",           // filled quantity 
          "fee":         "0.00012",       // cumulative fee paid for this order
          "feeAsset":    "ETH",           // the asset 
          "status":      "pending"
        },
        ...
      ]
    }


WebSocket API
----------------------------------------------

WebSocket entry point: `wss://bitmax-sandbox.io/api/tradeview/[symbol]` 

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
    } 


### Market depth

Each market depth message is a JSON object containing the current quantity at specific 
prices levels. There is no direct way of getting the top-of-the-book data. You need to maintain the 
current depth book and derive the best bid/ask. This can be done by two steps:

1. Use the first `depth` message to build the initial depth book.  
2. Use later messages to update the depth book. Messages contain the new total size at the 
   indicated price level. You should replace the old quantity using message received. When the 
   replacement quantity is zero, it means there is no order sitting on the corresponding price level.

All `depth` messages have the same structure: 

    {
       "m": "depth",             // message type
       "s": "ETH/BTC",           // symbol
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


### Order Management 

With our websocket API, you can monitor the status of all your active orders, place new orders, and 
cancel existing orders. 


#### Order updates 

Once connected to websocket streams, you will start receiving real time updated of your own orders. It 
contains both order execution report and current balances. 

    {
      "m":      "order",        // message type
      "coid":   "xxx...xxx",    // client order id, need to cancel order
      "s":      "ETH/BTC",      // symbol
      "ba":     "ETH",          // base asset 
      "qa":     "BTC",          // quote asset 
      "t":       1528988100000, // timestamp
      "p":      "13.45",        // order price 
      "q":      "3.5",          // order quantity
      "f":      "1.5",          // filled quantity
      "ap":     "13.45",        // average price
      "bb":     "10.00",        // base asset total balance
      "bpb":    "12.00",        // base asset pending balance
      "qb":     "1.0",          // quote asset total balance
      "qpb":    "0.8513",       // quote asset pending balance
      "fee":    "0.00012",      // fee
      "fa":     "ETH",          // fee asset
      "side":   "buy",          // side
      "status": "completed"     // order status
    }

Since only new order updates will be streamed, it is recommendated that you load the initial snap of all you orders 
using the RESTful API `GET api/orders/open`.


#### Place a New Order with WebSocket

To place a new order, you need to send to the server a `newOrderRequest`:

    {
       "messageType": "newOrderRequest",
       "time":        1528988100000,  // current timestamp
       "coid":        "xxxx...xxx"    // a 32-character unique client order Id
       "symbol":      "ETH/BTC",      // symbol 
       "orderPrice":  "2.23",         // order price
       "orderQty":    "34.3",         // order quantity
       "orderType":   "limit",        // always limit
       "side":        "buy"           // buy / sell
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

