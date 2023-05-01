package org.mooner.chatserver.socket.config

import org.mooner.chatserver.socket.handler.ChatHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(getHandler(), "ws/chat/*")
            .setAllowedOrigins("*")
//            .setHandshakeHandler()
//            .withSockJS()
//            .setHeartbeatTime(5000)
//            .setWebSocketEnabled(true)
    }

    @Bean
    fun getHandler(): ChatHandler {
        return ChatHandler()
    }
}