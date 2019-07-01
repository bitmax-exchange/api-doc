Bitmax | Beta API Document v1.2
==============================================

BitMax developer team are constantly working on API enhancement. However, some changes might not be compatible 
with existing APIs. To assist the transition, we publish APIs with enhancements using slightly modified URLs (Beta APIs).
Beta APIs will co-exist with the original APIs (base APIs) with the expectation that base APIs will eventually be upgraded. 
 

Authenticated WebSocket API
----------------------------------------------

Authenticated WebSocket entry point: 

* Beta version: `wss://bitmax.io/<account-group>/api/stream/cash/beta/[symbol]`
* Base version: `wss://bitmax.io/<account-group>/api/stream/[symbol]`

(View permission needed)

### New in Beta Version

#### All client order requests will receive an ack message

In the beta version API, all client order requests - including placing a new order, canceling one order, and cancelling all orders - will receive an immediate ack message:

* If the ack `status` is `Submitted`, it means the order has been sent to our internal order managing system and user should expect at least one more message regarding the order. Note that in this case, the order could still be rejected. The 
* If the ack `status` is `Rejected`, it means the order is rejected and user should expect no further message for the order.


A typical ack message for `status = Submitted` look like the following:

    {
      "m"     : "order", 
      "coid"  : "xxx...xxx",  // The coid specified in the client request. Since the cancel-all 
                              // request doesn't require an Id, this field will simply be set
                              // to "cancel-all" in that case. 
      "status": "Submitted" 
    }

Ack message for `status = Rejected`: 

    {
      "m"        : "order",
      "coid"     : "xxx...xxx",
      "status"   : "Rejected",
      "errorCode": "19001",  // an integer represented as a String (to be consistent with other order messages)
      "reason"   : "Order Id is already in use" 
    }

#### By default, Market Summary and Bar Data Streams will be Turned Off

Users has to set `skipSummary = false` and/or `skipBars = false` in the `Subscribe` message to turn them on.

