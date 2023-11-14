package utils

import (
	"crypto/hmac"
	"crypto/sha256"
	"demo/config"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"github.com/go-resty/resty/v2"
	"log"
	"net/url"
	"strings"
	"time"
)

//公共get请求
func PublicGet(urlStr string, jsonParams string) interface{} {
	var path string
	if jsonParams == "" {
		path = urlStr
	} else {
		strParams := JsonToParamStr(jsonParams)
		path = urlStr + "?" + strParams
		fmt.Println("路径:", path)
	}
	//创建请求
	client := resty.New()
	//发送请求
	resp, err := client.R().Get(path)

	if err != nil {
		log.Fatal("请求报错：", err)
	}

	// fmt.Println("Response Info:", resp)
	return resp
}

//私有get请求
func PrivateGet(urlStr string, jsonParams string) interface{} {
	var path string
	timestamp := time.Now().UnixNano() / 1e6
	fmt.Println(timestamp)
	if jsonParams == "" {
		message := fmt.Sprintf("timestamp=%d", timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?timestamp=%d&signature=%s", urlStr, timestamp, sign)
		fmt.Println("message:", message)
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	} else {
		strParams := JsonToParamStr(jsonParams)
		message := fmt.Sprintf("%s&timestamp=%d", strParams, timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?%s&timestamp=%d&signature=%s", urlStr, strParams, timestamp, sign)
		fmt.Println("message:", ParamsEncode(message))
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	}
	//创建请求
	client := resty.New()
	//发送请求
	resp, err := client.R().SetHeaders(map[string]string{
		"X-MEXC-APIKEY": config.API_KEY,
		"Content-Type":  "application/json",
	}).Get(path)

	if err != nil {
		log.Fatal("请求报错：", err)
	}

	// fmt.Println("Response Info:", resp)
	return resp
}

//私有post请求
func PrivatePost(urlStr string, jsonParams string) interface{} {
	var path string
	timestamp := time.Now().UnixNano() / 1e6
	fmt.Println(timestamp)
	if jsonParams == "" {
		message := fmt.Sprintf("timestamp=%d", timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?timestamp=%d&signature=%s", urlStr, timestamp, sign)
		fmt.Println("message:", message)
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	} else {
		strParams := JsonToParamStr(jsonParams)
		message := fmt.Sprintf("%s&timestamp=%d", strParams, timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?%s&timestamp=%d&signature=%s", urlStr, strParams, timestamp, sign)
		fmt.Println("message:", ParamsEncode(message))
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	}
	//创建请求
	client := resty.New()
	//发送请求
	resp, err := client.R().SetHeaders(map[string]string{
		"X-MEXC-APIKEY": config.API_KEY,
		"Content-Type":  "application/json",
	}).Post(path)

	if err != nil {
		log.Fatal("请求报错：", err)
	}

	// fmt.Println("Response Info:", resp)
	return resp
}

//私有delete请求
func PrivateDelete(urlStr string, jsonParams string) interface{} {
	var path string
	timestamp := time.Now().UnixNano() / 1e6
	fmt.Println(timestamp)
	if jsonParams == "" {
		message := fmt.Sprintf("timestamp=%d", timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?timestamp=%d&signature=%s", urlStr, timestamp, sign)
		fmt.Println("message:", message)
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	} else {
		strParams := JsonToParamStr(jsonParams)
		message := fmt.Sprintf("%s&timestamp=%d", strParams, timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?%s&timestamp=%d&signature=%s", urlStr, strParams, timestamp, sign)
		fmt.Println("message:", ParamsEncode(message))
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	}
	//创建请求
	client := resty.New()
	//发送请求
	resp, err := client.R().SetHeaders(map[string]string{
		"X-MEXC-APIKEY": config.API_KEY,
		"Content-Type":  "application/json",
	}).Delete(path)

	if err != nil {
		log.Fatal("请求报错：", err)
	}

	// fmt.Println("Response Info:", resp)
	return resp
}

//私有put请求
func PrivatePut(urlStr string, jsonParams string) interface{} {
	var path string
	timestamp := time.Now().UnixNano() / 1e6
	fmt.Println(timestamp)
	if jsonParams == "" {
		message := fmt.Sprintf("timestamp=%d", timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?timestamp=%d&signature=%s", urlStr, timestamp, sign)
		fmt.Println("message:", message)
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	} else {
		strParams := JsonToParamStr(jsonParams)
		message := fmt.Sprintf("%s&timestamp=%d", strParams, timestamp)
		sign := ComputeHmac256(message, config.SEC_KEY)
		path = fmt.Sprintf("%s?%s&timestamp=%d&signature=%s", urlStr, strParams, timestamp, sign)
		fmt.Println("message:", ParamsEncode(message))
		fmt.Println("sign:", sign)
		fmt.Println("path:", path)
	}
	//创建请求
	client := resty.New()
	//发送请求
	resp, err := client.R().SetHeaders(map[string]string{
		"X-MEXC-APIKEY": config.API_KEY,
		"Content-Type":  "application/json",
	}).Put(path)

	if err != nil {
		log.Fatal("请求报错：", err)
	}

	// fmt.Println("Response Info:", resp)
	return resp
}

//格式化参数字符串
func JsonToParamStr(jsonParams string) string {
	//转化json参数->参数字符串
	var paramsarr []string
	var arritem string
	m := make(map[string]string)
	err := json.Unmarshal([]byte(jsonParams), &m)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Printf("map:%v\n", m)
	i := 0
	for key, value := range m {

		arritem = fmt.Sprintf("%s=%s", key, value)
		paramsarr = append(paramsarr, arritem)
		i++
		fmt.Println("遍历：", i, "总共", len(m))
		if i > len(m) {
			break
		}
	}
	paramsstr := strings.Join(paramsarr, "&")
	fmt.Println("参数字符串：", paramsstr)
	return paramsstr
}

//urlencode
func ParamsEncode(paramStr string) string {
	return url.QueryEscape(paramStr)
}

//加密
func ComputeHmac256(Message string, sec_key string) string {
	key := []byte(sec_key)
	h := hmac.New(sha256.New, key)
	h.Write([]byte(Message))
	return hex.EncodeToString(h.Sum(nil))
}
