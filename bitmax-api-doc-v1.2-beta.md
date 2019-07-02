Bitmax | Beta API Document v1.2
==============================================


WebSocket API for Cash Trading
----------------------------------------------

Authenticated WebSocket entry point: 

* API Entry: `wss://bitmax.io/<account-group>/api/stream/cash-beta/[symbol]`

(View permission needed; trade permission needed if user wants to place/cancel orders)


### New in Beta Version

This API is designed to replace:

    wss://bitmax.io/<account-group>/api/stream/[symbol]


#### All invalid client order requests will receive a "Rejected" message

In the beta version API, all client order requests - including placing a new order, canceling one order, and cancelling all orders - will receive an immediate rejected message if the request is invalid. All rejected message contains the following fields:

    m          String, for requests such as placing a new order, canceling one order, or 
               cancelling all orders, this field is always "order"
    coid       String, this is the coid included in the request. For the cancel all request 
               in which no coid is included, this field is simply set to "cancel-all" 
    status     String, always "Rejected"
    errorCode  String, an integer represented as a String (this is to be consistent with other order messages)
    reason     String, text explaining why order is rejected. 

Example:

    {
      "m"        : "order",
      "coid"     : "xxx...xxx",
      "status"   : "Rejected",
      "errorCode": "19001",  // an integer represented as a String (to be consistent with other order messages)
      "reason"   : "Order Id is already in use" 
    }

#### By default, Market Summary and Bar Data Streams will be Turned Off

One needs to set `skipSummary = false` and/or `skipBars = false` in the `Subscribe` message to turn them on.

