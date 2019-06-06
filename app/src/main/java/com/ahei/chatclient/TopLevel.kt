package com.ahei.chatclient

import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import org.slf4j.LoggerFactory

val LOGGER = LoggerFactory.getLogger("Global")
val DECODER = StringDecoder()
val ENCODER = StringEncoder()

var UID = ""