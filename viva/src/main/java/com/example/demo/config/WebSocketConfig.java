package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	 @Override
	  public void registerStompEndpoints(StompEndpointRegistry registry) {
	    // 클라이언트가 연결할 endpoint
	    registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
	  }

	  @Override
	  public void configureMessageBroker(MessageBrokerRegistry registry) {
	    // 메시지를 구독할 prefix
	    registry.enableSimpleBroker("/topic");

	    // 메시지를 보낼 때 사용할 prefix
	    registry.setApplicationDestinationPrefixes("/app");
	  }
	
}
