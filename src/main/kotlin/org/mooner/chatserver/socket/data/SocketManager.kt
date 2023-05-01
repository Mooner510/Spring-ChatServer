package org.mooner.chatserver.socket.data

import org.mooner.chatserver.socket.exception.ChannelNotFound
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import java.io.BufferedOutputStream
import java.io.OutputStreamWriter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class SocketManager {
    data class Channel(
        val uuid: UUID
    )

    companion object {
        private val channels: MutableMap<UUID, MutableSet<WebSocketSession>> = ConcurrentHashMap()
        private val out: OutputStreamWriter = OutputStreamWriter(BufferedOutputStream(System.out))

        fun newChannel(): Pair<Channel, MutableSet<WebSocketSession>> {
            val uuid = UUID.randomUUID()
            val newKeySet = ConcurrentHashMap.newKeySet<WebSocketSession>()
            channels[uuid] = newKeySet
            return Pair(Channel(uuid), newKeySet)
        }

        private fun getChannelID(session: WebSocketSession): UUID {
            session.uri?.path?.let {
                return UUID.fromString(it.substring(it.lastIndexOf('/') + 1 until it.length))
            }
            throw ChannelNotFound("null")
        }

        private fun getChannel(uuid: UUID): MutableSet<WebSocketSession> {
            channels[uuid]?.let { return it }
            throw ChannelNotFound(uuid.toString())
        }

        fun getChannel(session: WebSocketSession): MutableSet<WebSocketSession> {
            try {
                return getChannel(getChannelID(session))
            } catch (e: ChannelNotFound) {
                session.close(CloseStatus.BAD_DATA)
                leave(session)
                throw e
            }
        }

        fun join(session: WebSocketSession) {
            getChannel(session).add(session)
        }

        fun leave(session: WebSocketSession) {
            val channelID = getChannelID(session)
            val channel = getChannel(channelID)
            channel.remove(session)
            if (channel.isEmpty()) channels.remove(channelID)
        }

        fun log(str: String) {
            out.write(str + "\n")
            out.flush()
        }
    }
}