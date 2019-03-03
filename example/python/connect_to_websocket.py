import json
import requests
import time 
import hmac, hashlib, base64
import random, string   
from datetime import datetime
from threading import Thread
from websocket import create_connection
from os.path import join as path_join
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


def connect(url, api_key, secret):
    ts = utc_timestamp()
    headers = make_auth_header(ts, "api/stream", api_key, secret)
    return create_connection(url, header=headers)

def disconnect(ws):
    try:
      ws.close()
    except WebSocketConnectionClosedException as e:
      pass

def listen(ws):
    subscribe = """{
        "messageType":         "subscribe",
        "marketDepthLevel":    20,
        "recentTradeMaxCount": 20
      }
    """
    ws.send(subscribe)

    running = True
    while running:
      try:
        start_t = 0
        if time.time() - start_t >= 30:
            # Set a 30 second ping to keep connection alive
            ws.ping("keepalive")
            start_t = time.time()
        data = ws.recv()
        try: 
          msg = json.loads(data)
        except: 
          raise f"Failed to parse message a json: {data}"
      except ValueError as e:
        running = False
      except Exception as e:
        running = False
      else:
        print(f"received: {msg}")

def run(url, api_key, secret):
    def loop(api_key, secret):
      ws = connect(url, api_key, secret)
      listen(ws)
      disconnect(ws)
    thread = Thread(target=loop, args=(api_key, secret))
    thread.start()


if __name__ == '__main__':
    apiKey = "[[api-key]]"
    secret = "[[secret]]"
    group   = "[[account-group]]"

    # Make sure your API key has view and trade permissions
    # If you do not plan to place order with websocket, try connecting with:
    # wss://bitmax.io/api/public/ETH-BTC
    url     = f"wss://bitmax.io/{group}/api/stream/ETH-BTC"
    
    run(url, api_key, secret)
