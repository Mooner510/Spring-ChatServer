package org.mooner.chatserver.socket.handler

import org.mooner.chatserver.socket.data.SocketManager
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException

@Component
class ChatHandler : TextWebSocketHandler() {
    fun send(sender: WebSocketSession, message: TextMessage) {
        val channel = SocketManager.getChannel(sender)
        for (sess in channel) {
            try {
                if(sess.id != sender.id) sess.sendMessage(message)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        send(session, message)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        SocketManager.join(session)
        SocketManager.log("Connected: $session")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        SocketManager.leave(session)
        SocketManager.log("Disconnected: $session")
    }
}