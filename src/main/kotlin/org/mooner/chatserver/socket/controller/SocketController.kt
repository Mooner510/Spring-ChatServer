package org.mooner.chatserver.socket.controller

import org.mooner.chatserver.socket.data.SocketManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SocketController {
    @GetMapping
    fun newChannel(): SocketManager.Channel {
        return SocketManager.newChannel().first
    }
}