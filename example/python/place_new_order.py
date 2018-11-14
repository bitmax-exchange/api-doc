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
    try: 
        res = requests.post(url, *args, **kwargs)
        return __parse_response(res)
    except requests.exceptions.ConnectionError: 
        print(f"[WARN] Failed to connect {url}")
        return None
    except: 
        raise

def GET(url, *args, **kwargs):
  try: 
    res = requests.get(url, *args, **kwargs)
    return __parse_response(res)
  except requests.exceptions.ConnectionError: 
    print(f"[WARN] Failed to connect {url}")
    return None
  except: 
    raise

def __parse_response(res):
  if res is None:
    return None 

  if res.status_code == 200:
    data = json.loads(res.text)
    return data
  else:
    print(f"request failed, error code = {res.status_code}")
    print(res.text)

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

def user_info(url, api_key, secret): 
    ts = utc_timestamp()
    headers = make_auth_header(ts, "user/info", api_key, secret)
    return GET(f"{url}/api/v1/user/info", headers=headers)

if __name__ == "__main__":

    url = "https://bitmax.io"
    apiKey = "[[api-key]]"
    secret = "[[secret]]"
    
    account_group = user_info(url, apiKey, secret)
    account_group = account_group['accountGroup']
    
    res = place_new_order(
        url,
        apiKey,
        secret,   
        account_group,
        symbol = "ETH/BTC",
        price = "0.03",
        quantity = "0.2",
        side = "sell")

    pprint(res)