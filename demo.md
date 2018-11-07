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

## Python 3.6+

    import json 
    import requests
    from datetime import datetime
    import hmac, hashlib, base64
    import random, string   
    from pprint import pprint  

    def uuid32():
        return ''.join(random.choice(string.ascii_uppercase + string.ascii_lowercase + string.digits) for _ in range(32))

    def utc_timestamp():
      tm = datetime.utcnow().timestamp()
      return int(tm * 1e3)

    def make_auth_header(timestamp, api_path, api_key, secret, coid=None): 
      # convert timestamp to string   
      if isinstance(timestamp, bytes):
        timestamp = timestamp.decode("utf-8")
      elif isinstance(timestamp, int):
        timestamp = str(timestamp)

      if coid is None:
        msg = bytearray(f"{timestamp}+{api_path}".encode("utf-8"))
      else:
        msg = bytearray(f"{timestamp}+{api_path}+{coid}".encode("utf-8"))

      hmac_key = base64.b64decode(secret)
      signature = hmac.new(hmac_key, msg, hashlib.sha256)
      signature_b64 = base64.b64encode(signature.digest()).decode("utf-8")  
      header = {
        "x-auth-key": api_key,
        "x-auth-signature": signature_b64,
        "x-auth-timestamp": timestamp,
      }

      if coid is not None:
        header["x-auth-coid"] = coid

      return header


    def POST(url, *args, **kwargs):
      def __parse_response(res):
        if res is None:
          return None 

        if res.status_code == 200:
          data = json.loads(res.text)
          return data
        else:
          print(f"request failed, error code = {res.status_code}")
          print(res.text)
      try: 
        res = requests.post(url, *args, **kwargs)
        return __parse_response(res)
      except requests.exceptions.ConnectionError: 
        print(f"[WARN] Failed to connect {url}")
        return None
      except: 
        raise

    def place_new_order(url, api_key, secret, account_group, symbol, price, quantity, side):
      ts = utc_timestamp()
      coid = uuid32()
      headers = make_auth_header(ts, "order", api_key, secret, coid)
      order = dict(
          coid       = coid,
          time       = ts,
          symbol     = symbol.replace("-", "/"),
          orderPrice = str(price),
          orderQty   = str(quantity),
          orderType  = "limit",
          side       = side.lower()
      )
      
      pprint(headers)

      return POST(f"{url}/{account_group}/api/v1/order", json=order, headers=headers)


    if __name__ == "__main__":

      res = place_new_order(
        "https://bitmax.io",  # replace 1 with your account group
        "[[api-key]]",
        "[[secret]]",
        account_group = 1,
        symbol = "ETH/BTC",
        price = "0.03",
        quantity = "0.2",
        side = "sell")

      pprint(res)


## c++
  https://github.com/bitmax-exchange/api-doc/blob/master/example/cpp/main.cpp

## golang
  https://github.com/bitmax-exchange/api-doc/blob/master/example/golang/main.go