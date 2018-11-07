package main

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"strings"
	"time"
)

// Your key & Secret
var APIKey string = ""
var APISecret string = ""

/* -----------------------------  UUID Generater Example ----------------------------- */

const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

func init() {
	rand.Seed(time.Now().UTC().UnixNano())
}

func Uuid(n int) string {
	b := make([]byte, n)
	for i := range b {
		b[i] = letterBytes[rand.Intn(len(letterBytes))]
	}
	return string(b)
}

/* -----------------------------  UUID Generater Example ----------------------------- */

/* -----------------------------  HMAC256 Example ----------------------------- */
func ComputeHmac256(message string, secret string) string {
	key := []byte(secret)
	h := hmac.New(sha256.New, key)
	h.Write([]byte(message))
	return base64.StdEncoding.EncodeToString(h.Sum(nil))
}

/* -----------------------------  HMAC256 Example ----------------------------- */

type RPC struct {
	url    string
	client *http.Client
}

// GET /api/v1/assets
func (rpc *RPC) Assets() error {
	resp, err := rpc.client.Get(rpc.url + "/api/v1/assets")
	if err != nil {
		fmt.Println(err)
		return err
	}

	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		fmt.Println("ERROR http  status ", resp.StatusCode)
		return errors.New("error http  status")
	}

	data, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println(err)
		return err
	}

	fmt.Println(string(data))

	return nil
}

// GET <account-group>/api/v1/order/open
func (rpc *RPC) ListOrders() error {
	timeStamp := time.Now().UnixNano() / int64(time.Millisecond)

	req, err := http.NewRequest("GET", rpc.url+"/1/api/v1/order/open", nil)
	if err != nil {
		fmt.Println(err)
		return err
	}

	msg := fmt.Sprint(timeStamp) + "+order/open"

	signMsg := ComputeHmac256(msg, APISecret)

	fmt.Println(signMsg)

	req.Header.Set("x-auth-key", APIKey)
	req.Header.Set("x-auth-signature", signMsg)
	req.Header.Set("x-auth-timestamp", fmt.Sprint(timeStamp))

	resp, err := rpc.client.Do(req)
	if err != nil {
		fmt.Println(err)
		return err
	}

	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		fmt.Println("ERROR http  status ", resp.StatusCode)
		return errors.New("error http  status")
	}

	data, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println(err)
		return err
	}

	fmt.Println(string(data))

	return nil
}

// POST <account-group>/api/v1/order
func (rpc *RPC) NewOrder() error {
	coid := Uuid(32)

	timeStamp := time.Now().UnixNano() / int64(time.Millisecond)

	type OrderBody struct {
		Coid       string `json:"coid"`       // "xxx...xxx"     a unique identifier
		Time       int64  `json:"time"`       // 1528988100000   milliseconds since UNIX epoch in UTC
		Symbol     string `json:"symbol"`     // "ETH/BTC"
		OrderPrice string `json:"orderPrice"` // "13.5"
		OrderQty   string `json:"orderQty"`   // "3.5"
		OrderType  string `json:"orderType"`  // "limit"         currently we only support limit order
		Side       string `json:"side"`       // "buy"           buy or sell
	}

	order := OrderBody{
		Coid:       coid,
		Time:       timeStamp,
		Symbol:     "ETH/BTC",
		OrderPrice: "0.0030",
		OrderQty:   "1",
		OrderType:  "limit",
		Side:       "buy",
	}

	orderdata, err := json.Marshal(order)
	if err != nil {
		fmt.Println(err)
		return err
	}

	fmt.Println(string(orderdata))

	req, err := http.NewRequest("POST", rpc.url+"/1/api/v1/order", strings.NewReader(string(orderdata)))
	if err != nil {
		fmt.Println(err)
		return err
	}

	msg := fmt.Sprint(timeStamp) + "+order+" + coid

	signMsg := ComputeHmac256(msg, APISecret)

	fmt.Println(signMsg)

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("x-auth-key", APIKey)
	req.Header.Set("x-auth-signature", signMsg)
	req.Header.Set("x-auth-timestamp", fmt.Sprint(timeStamp))
	req.Header.Set("x-auth-coid", coid)

	resp, err := rpc.client.Do(req)
	if err != nil {
		fmt.Println(err)
		return err
	}

	defer resp.Body.Close()

	data, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println(err)
		return err
	}

	if resp.StatusCode != http.StatusOK {
		fmt.Println("ERROR http status ", resp.StatusCode)
		return errors.New("error http status")
	}

	fmt.Println(string(data))

	return nil
}

//DELETE <account-group>/api/v1/order
func (rpc *RPC) CancelOrder() error {
	coid := Uuid(32)
	timeStamp := time.Now().UnixNano() / int64(time.Millisecond)

	type CancelBody struct {
		Coid     string `json:"coid"`     //  "xxx...xxx"     a unique identifier, see POST api/order for details
		OrigCoid string `json:"origCoid"` //  "yyy...yyy"     the coid of the order to cancel
		Time     int64  `json:"time"`     //  1528988100000   milliseconds since UNIX epoch in UTC,  see POST api/order for details
		Symbol   string `json:"symbol"`   //  "ETH/BTC"
	}

	order := CancelBody{
		Coid:     coid,
		OrigCoid: "GpFshuLn8wRqb1TaewNrx4ugkY6ickox", // the coid of the order to cancel , you can get it from the API :   GET <account-group>/api/v1/order/open
		Time:     timeStamp,
		Symbol:   "ETH/BTC",
	}

	orderdata, err := json.Marshal(order)
	if err != nil {
		fmt.Println(err)
		return err
	}

	fmt.Println(string(orderdata))

	req, err := http.NewRequest("DELETE", rpc.url+"/1/api/v1/order", strings.NewReader(string(orderdata)))
	if err != nil {
		fmt.Println(err)
		return err
	}

	msg := fmt.Sprint(timeStamp) + "+order+" + coid

	signMsg := ComputeHmac256(msg, APISecret)

	fmt.Println(signMsg)

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("x-auth-key", APIKey)
	req.Header.Set("x-auth-signature", signMsg)
	req.Header.Set("x-auth-timestamp", fmt.Sprint(timeStamp))
	req.Header.Set("x-auth-coid", coid)

	resp, err := rpc.client.Do(req)
	if err != nil {
		fmt.Println(err)
		return err
	}

	defer resp.Body.Close()

	data, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println(err)
		return err
	}

	if resp.StatusCode != http.StatusOK {
		fmt.Println("ERROR http status ", resp.StatusCode)
		return errors.New("error http  status")
	}

	fmt.Println(string(data))
	return nil
}

func NewRPC(url string) *RPC {
	rpc := &RPC{
		url:    url,
		client: http.DefaultClient,
	}

	return rpc
}

func main() {

	fmt.Println(Uuid(32))

	rpc := NewRPC("https://bitmax-test.io")

	rpc.Assets()

	rpc.NewOrder()

	rpc.ListOrders()

	rpc.CancelOrder()
}
