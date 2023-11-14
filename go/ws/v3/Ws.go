package main

import (
	"fmt"
	"github.com/gorilla/websocket"
	"log"
	"net/url"
	"os"
	"os/signal"
	"time"
)

var addr = "wbs.mexc.com"
var listenKey = "b93a4d29dc50a5e22270b3858acc8478ebbccf2ce2564f54281fb802d03eb4"

// 订阅频道
var payload = `{"method":"PING"}`

func main() {

	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt)
	// ws路径
	u := url.URL{Scheme: "wss", Host: addr, Path: "/ws", RawQuery: fmt.Sprintf("listenKey=%s", listenKey)}
	log.Printf("connecting to %s", u.String())

	c, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	if err != nil {
		log.Fatal("dial:", err)
	}
	defer c.Close()

	done := make(chan struct{})
	//读
	go func() {
		defer close(done)
		for {
			_, message, err := c.ReadMessage()
			if err != nil {
				log.Println("read:", err)
				return
			}
			log.Printf("recv: %s", message)
		}
	}()
	// //监听typein
	// reader := bufio.NewReader(os.Stdin) //阅读器 读取输入的数据
	// for {
	// 	msga, _, _ := reader.ReadLine()
	// 	err := c.WriteMessage(websocket.TextMessage, msga)
	// 	log.Println("发送:", string(msga))
	// 	if err != nil {
	// 		log.Println("write:", err)
	// 		return
	// 	}
	// }

	//写
	go func() {
		err := c.WriteMessage(websocket.TextMessage, []byte(payload))
		log.Println("发送:", payload)
		if err != nil {
			log.Println("write:", err)
			return
		}
	}()

	ticker := time.NewTicker(time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-done:
			return
		case <-interrupt:
			log.Println("interrupt")

			// Cleanly close the connection by sending a close message and then
			// waiting (with timeout) for the server to close the connection.
			err := c.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseNormalClosure, ""))
			if err != nil {
				log.Println("write close:", err)
				return
			}
			select {
			case <-done:
			case <-time.After(time.Second):
			}
			return
		}
	}
}
