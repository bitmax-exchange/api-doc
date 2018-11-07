#include <stdio.h>
#include <curl/curl.h>
#include <string>
#include <ctime>
#include <iostream>
#include <chrono>
#include <openssl/hmac.h>
#include <sstream>

using namespace std;

int OnDebug(CURL * curl, curl_infotype itype, char * pData, size_t size, void *)
{
	if(itype == CURLINFO_TEXT)
	{
		//printf("[TEXT]%s\n", pData);
	}
	else if(itype == CURLINFO_HEADER_IN)
	{
		printf("[HEADER_IN]%s\n", pData);
	}
	else if(itype == CURLINFO_HEADER_OUT)
	{
		printf("[HEADER_OUT]%s\n", pData);
	}
	else if(itype == CURLINFO_DATA_IN)
	{
		printf("[DATA_IN]%s\n", pData);
	}
	else if(itype == CURLINFO_DATA_OUT)
	{
		printf("[DATA_OUT]%s\n", pData);
	}
	return 0;
}
 
size_t OnWriteData(void* buffer, size_t size, size_t nmemb, void* lpVoid)
{
	string* str = dynamic_cast<string*>((string *)lpVoid);
	if( NULL == str || NULL == buffer )
	{
		return -1;
	}
 
    char* pData = (char*)buffer;
    str->append(pData, size * nmemb);
	return nmemb;
}

int HmacEncode(const char * key, unsigned int key_length,
                const char * input, unsigned int input_length,
                unsigned char * &output, unsigned int &output_length) {

        const EVP_MD * engine = NULL;

        engine = EVP_sha256();
 
        output = (unsigned char*)malloc(EVP_MAX_MD_SIZE);
 
        HMAC_CTX ctx;
        HMAC_CTX_init(&ctx);
        HMAC_Init_ex(&ctx, key, strlen(key), engine, NULL);
        HMAC_Update(&ctx, (unsigned char*)input, strlen(input));        // input is OK; &input is WRONG !!!
 
        HMAC_Final(&ctx, output, &output_length);
        HMAC_CTX_cleanup(&ctx);
 
        return 0;
}

static const  string base64_chars = 
             "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
             "abcdefghijklmnopqrstuvwxyz"
             "0123456789+/";
 
 
static inline bool is_base64(unsigned char c) {
  return (isalnum(c) || (c == '+') || (c == '/'));
}
 
string base64_encode(unsigned char const* bytes_to_encode, unsigned int in_len) {
   string ret;
    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];
 
  while (in_len--) {
    char_array_3[i++] = *(bytes_to_encode++);
    if (i == 3) {
      char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
      char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
      char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
      char_array_4[3] = char_array_3[2] & 0x3f;
 
      for(i = 0; (i <4) ; i++)
        ret += base64_chars[char_array_4[i]];
      i = 0;
    }
  }
 
  if (i)
  {
    for(j = i; j < 3; j++)
      char_array_3[j] = '\0';
 
    char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
    char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
    char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
    char_array_4[3] = char_array_3[2] & 0x3f;
 
    for (j = 0; (j < i + 1); j++)
      ret += base64_chars[char_array_4[j]];
 
    while((i++ < 3))
      ret += '=';
 
  }
 
  return ret;
}

class CHttpClient
{
public:
	CHttpClient(string key, string secret) 
    {
        m_bDebug = false;
        m_strAPIKey = key;
        m_strAPISecret = secret;
    }

    ~CHttpClient(void)
    {
    
    }
 
public:
	/**
	* @brief HTTP POST请求
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strPost 输入参数,使用如下格式para1=val1¶2=val2&…
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Post成功
	*/
	int Post(const string & strUrl, const string & strPost, string & strResponse)
    {
        CURLcode res;
        CURL* curl = curl_easy_init();
        if(NULL == curl)
        {
            return CURLE_FAILED_INIT;
        }
        if(m_bDebug)
        {
            curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
            curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
        }
        curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
        curl_easy_setopt(curl, CURLOPT_POST, 1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, strPost.c_str());
        curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&strResponse);
        curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
        curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 3);
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 3);
        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
        return res;
    }
	/**
	* @brief HTTP GET请求
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Post成功
	*/
	int Get(const string & strUrl, string & strResponse)
    {
        CURLcode res;
        CURL* curl = curl_easy_init();
        if(NULL == curl)
        {
            return CURLE_FAILED_INIT;
        }
        if(m_bDebug)
        {
            curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
            curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
        }
        curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
        curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&strResponse);
        /**
        * 当多个线程都使用超时处理的时候，同时主线程中有sleep或是wait等操作。
        * 如果不设置这个选项，libcurl将会发信号打断这个wait从而导致程序退出。
        */
        curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
        curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 3);
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 3);
        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
        return res;
    }

    /**
	* @brief HTTP GET请求 ListOrders
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Post成功
	*/
	int ListOrders(const string & strUrl, string & strResponse)
    {
        CURLcode res;
        CURL* curl = curl_easy_init();
        if(NULL == curl)
        {
            return CURLE_FAILED_INIT;
        }
        if(m_bDebug)
        {
            curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
            curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
        }
        curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
        curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&strResponse);
        /**
        * 当多个线程都使用超时处理的时候，同时主线程中有sleep或是wait等操作。
        * 如果不设置这个选项，libcurl将会发信号打断这个wait从而导致程序退出。
        */
        curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
        curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 3);
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 3);

        struct curl_slist *headers=NULL; /* init to NULL is important */


        // get timestamp in  millisecond
        chrono::milliseconds ms = chrono::duration_cast< chrono::milliseconds >(chrono::system_clock::now().time_since_epoch());

        string authKey = string("x-auth-key: ") + m_strAPIKey;
        string authTimestamp = string("x-auth-timestamp: ") + to_string(ms.count());

        string msg = to_string(ms.count()) + string("+order/open");
        string signmsg;
        Hmac256(msg,signmsg);

        string authSign = string("x-auth-signature: ") + signmsg;

        headers = curl_slist_append(headers, authKey.c_str());
        headers = curl_slist_append(headers, authSign.c_str());
        headers = curl_slist_append(headers, authTimestamp.c_str());
        /* pass our list of custom made headers */
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);


        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
        curl_slist_free_all(headers); /* free the header list */

        return res;
    }

    /**
	* @brief HTTP GET请求 ListOrders
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Post成功
	*/
	int NewOrder(const string & strUrl, string & strResponse)
    {
        CURLcode res;
        CURL* curl = curl_easy_init();
        if(NULL == curl)
        {
            return CURLE_FAILED_INIT;
        }
        if(m_bDebug)
        {
            curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
            curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
        }

        // get timestamp in  millisecond
        chrono::milliseconds ms = chrono::duration_cast< chrono::milliseconds >(chrono::system_clock::now().time_since_epoch());
        string coid = "7S2XiDl4A7RZXYLNz1ptDkyWupvA5hbX"; // must be generated by random

        stringstream ss;
        ss << "{";
        ss << "\"coid\":\"" << coid << "\",";
        ss << "\"time\":" << ms.count() << ",";
        ss << "\"symbol\":\"ETH/BTC\",";                               
        ss << "\"orderPrice\":\"0.0035\",";                           
        ss << "\"orderQty\":\"6\",";                                  
        ss << "\"orderType\":\"limit\",";                           
        ss << "\"side\":\"buy\"";
        ss << "}";


        string postData  = ss.str();

        printf("%s",postData.c_str());

        curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
        curl_easy_setopt(curl, CURLOPT_POST, 1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS,postData.c_str());
        curl_easy_setopt(curl, CURLOPT_POSTFIELDSIZE, -1L);
        curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&strResponse);
        curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
        curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 3);
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 3);
        

        struct curl_slist *headers=NULL; /* init to NULL is important */



        string msg = to_string(ms.count()) + "+order+" + coid;
        string signmsg;
        Hmac256(msg,signmsg);

        string authSign = string("x-auth-signature: ") + signmsg;
        string authKey = string("x-auth-key: ") + m_strAPIKey;
        string authTimestamp = string("x-auth-timestamp: ") + to_string(ms.count());
        string authCoid = string("x-auth-coid: ") + coid;

        headers = curl_slist_append(headers, "Content-Type: application/json");
        headers = curl_slist_append(headers, authKey.c_str());
        headers = curl_slist_append(headers, authSign.c_str());
        headers = curl_slist_append(headers, authTimestamp.c_str());
        headers = curl_slist_append(headers, authCoid.c_str());
        
        /* pass our list of custom made headers */
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);


        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
        curl_slist_free_all(headers); /* free the header list */

        return res;
    }


    /**
	* @brief HTTP GET请求 ListOrders
	* @param strUrl 输入参数,请求的Url地址,如:http://www.baidu.com
	* @param strResponse 输出参数,返回的内容
	* @return 返回是否Post成功
	*/
	int CancelOrder(const string & strUrl, string & strResponse)
    {
        CURLcode res;
        CURL* curl = curl_easy_init();
        if(NULL == curl)
        {
            return CURLE_FAILED_INIT;
        }
        if(m_bDebug)
        {
            curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
            curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION, OnDebug);
        }

        // get timestamp in  millisecond
        chrono::milliseconds ms = chrono::duration_cast< chrono::milliseconds >(chrono::system_clock::now().time_since_epoch());
        string coid = "7S2XiDl4A7RZXYLNz1ptDkyWupv16hbX"; // must be generated by random

        stringstream ss;
        ss << "{";
        ss << "\"coid\":\"" << coid << "\",";
        ss << "\"time\":" << ms.count() << ",";
        ss << "\"symbol\":\"ETH/BTC\",";                               
        ss << "\"origCoid\":\"jv40GpbRdK0jBurBw0EwLEVsYHbW2QLg\"";        
        ss << "}";

        string postData  = ss.str();

        printf("%s",postData.c_str());

        curl_easy_setopt(curl, CURLOPT_URL, strUrl.c_str());
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "DELETE");
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS,postData.c_str());
        curl_easy_setopt(curl, CURLOPT_POSTFIELDSIZE, -1L);
        curl_easy_setopt(curl, CURLOPT_READFUNCTION, NULL);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&strResponse);
        curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);
        curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 3);
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 3);
        

        struct curl_slist *headers=NULL; /* init to NULL is important */



        string msg = to_string(ms.count()) + "+order+" + coid;
        string signmsg;
        Hmac256(msg,signmsg);

        string authSign = string("x-auth-signature: ") + signmsg;
        string authKey = string("x-auth-key: ") + m_strAPIKey;
        string authTimestamp = string("x-auth-timestamp: ") + to_string(ms.count());
        string authCoid = string("x-auth-coid: ") + coid;

        headers = curl_slist_append(headers, "Content-Type: application/json");
        headers = curl_slist_append(headers, authKey.c_str());
        headers = curl_slist_append(headers, authSign.c_str());
        headers = curl_slist_append(headers, authTimestamp.c_str());
        headers = curl_slist_append(headers, authCoid.c_str());
        
        /* pass our list of custom made headers */
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);


        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
        curl_slist_free_all(headers); /* free the header list */

        return res;
    }

    int Hmac256(string data, string & encoded)
    {
        unsigned char * mac = NULL;
        unsigned int mac_length = 0;

        int ret = HmacEncode(m_strAPISecret.c_str(), m_strAPISecret.length(), data.c_str(), data.length(), mac, mac_length);
    
        if(0 == ret) {
            cout << "Algorithm HMAC encode succeeded!" << endl;
        } else {
            cout << "Algorithm HMAC encode failed!" << endl;
            return -1;
        }
        
        string sign((char * )mac,mac_length);

        encoded = base64_encode(reinterpret_cast<const unsigned char*>(sign.c_str()), sign.length());

        if(mac) 
        {
            free(mac);
        }

        return 0;
    }

public:
	void SetDebug(bool bDebug)
    {
        m_bDebug = bDebug;
    }
private:
	bool m_bDebug;
    string m_strAPIKey;
    string m_strAPISecret;
};

int main(){
    string APIKey = "";
    string APISecret = "";

    CHttpClient client(APIKey,APISecret);

    client.SetDebug(true);

    string resp;

    // client.Post("https://bitmax-test.io/1/api/v1/order","funklkadawdadadad",resp);

    // GET /api/v1/assets
    // int res = client.Get("https://bitmax-test.io/api/v1/assets",resp);
    // printf("res :%d , resp %s \n",res, resp.c_str());

    // GET <account-group>/api/v1/order/open
    // int res = client.ListOrders("https://bitmax-test.io/1/api/v1/order/open",resp);
    // printf("res :%d , resp %s \n",res, resp.c_str());

    // // POST <account-group>/api/v1/order
    // int res = client.NewOrder("https://bitmax-test.io/1/api/v1/order",resp);
    // printf("res :%d , resp %s \n",res, resp.c_str());

    int res = client.CancelOrder("https://bitmax-test.io/1/api/v1/order",resp);
    printf("res :%d , resp %s \n",res, resp.c_str());
    
}