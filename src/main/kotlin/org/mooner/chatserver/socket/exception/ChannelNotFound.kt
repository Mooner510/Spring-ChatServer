package org.mooner.chatserver.socket.exception

class ChannelNotFound(str: String) : RuntimeException("No channel found by name or id '$str'.")