package com.ahei.chatclient

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder

class ClientInitializer: ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline: ChannelPipeline = ch.pipeline()

        pipeline.addLast(DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter()))
            .addLast(DECODER)
            .addLast(ENCODER)
            .addLast(MessageHandler())
    }

}