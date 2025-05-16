package main

import (
	"encoding/json"
	"log"
	"os"
	"os/signal"
	"time"

	"github.com/gorilla/websocket"
	"google.golang.org/protobuf/proto"
	pb "proto-ws-go/pb"
)

const (
	websocketURL = "wss://wbs-api.mexc.com/ws"
)

func main() {
	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt)

	c, _, err := websocket.DefaultDialer.Dial(websocketURL, nil)
	if err != nil {
		log.Fatal("dial:", err)
	}
	defer c.Close()

	done := make(chan struct{})

	// 订阅消息
	subscriptionMessage := map[string]interface{}{
		"method": "SUBSCRIPTION",
		"params": []string{"spot@public.limit.depth.v3.api.pb@USDCUSDT@5"},
	}
	subMsg, _ := json.Marshal(subscriptionMessage)

	// PING 消息
	pingMessage := map[string]string{
		"method": "PING",
	}
	pingMsg, _ := json.Marshal(pingMessage)

	// 发送订阅消息
	err = c.WriteMessage(websocket.TextMessage, subMsg)
	if err != nil {
		log.Println("write:", err)
		return
	}
	log.Printf("发送订阅消息: %s", subMsg)

	// 定时发送 PING
	ticker := time.NewTicker(15 * time.Second)
	defer ticker.Stop()

	go func() {
		defer close(done)
		for {
			select {
			case <-ticker.C:
				err := c.WriteMessage(websocket.TextMessage, pingMsg)
				if err != nil {
					log.Println("write:", err)
					return
				}
				log.Printf("发送 PING 消息: %s", pingMsg)
			case <-interrupt:
				log.Println("收到中断信号，关闭连接...")
				err := c.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseNormalClosure, ""))
				if err != nil {
					log.Println("write close:", err)
					return
				}
				return
			}
		}
	}()

	// 接收消息
	for {
		_, message, err := c.ReadMessage()
		if err != nil {
			log.Println("read:", err)
			return
		}

		// 处理 Protobuf 消息
		// 注意：这里需要替换为您的 Protobuf 解码逻辑
		// decodedMessage := &pb.PushDataV3ApiWrapper{}
		// if err := proto.Unmarshal(message, decodedMessage); err != nil {
		// 	log.Println("protobuf unmarshal error:", err)
		// 	continue
		// }

		decodedMessage := &pb.PushDataV3ApiWrapper{}
		if err := proto.Unmarshal(message, decodedMessage); err != nil {
			log.Println("protobuf unmarshal error:", err)
			continue
		}

		// 打印解码后的消息
		log.Printf("收到消息: %+v", decodedMessage)
	}
}
