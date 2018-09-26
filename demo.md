# Demo code for Place an Order using BitMax API

## Node.js

    let CryptoJS = require("crypto-js");
    let request = require('request');

    let apikey = "[your-api-key]"
    let secret = "[your-secret]"

    let coid = 'iHVVxgRLRC2DEgEWZzSarVHELVLpezr0'  // unique order id, a random string of length 32
    
    let timestamp = +(new Date().getTime())
    let msg = timestamp+"+order+" + coid
    let hash = CryptoJS.HmacSHA256(msg, secret)
    let hashInBase64 = CryptoJS.enc.Base64.stringify(hash)

    let body = {
        coid:        coid,
        time:        timestamp,
        symbol:     "ETH/BTC",
        orderPrice: "0.030",
        orderQty:   "0.1",
        orderType:  "limit",
        side:       "buy"
    }
    request({ 
            url: 'https://bitmax.io/1/api/v1/order',
            method: "POST",
            json: true,
            body: body,
            headers: {
                'x-auth-key':       apikey,
                'x-auth-signature': hashInBase64,
                'x-auth-timestamp': timestamp,
                'x-auth-coid':      coid
            },
        },
        function (error, response, body) {
            if (!error && response.statusCode == 200) {
                console.log(body)
            }
        }
    );

