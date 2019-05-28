package com.ahei.chatclient

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

class Client {
    private val host = "106.13.103.14"
    private val port = 8080

    fun run() {
        val workerGroup = NioEventLoopGroup()

        val boot = Bootstrap()
        boot.group(workerGroup)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(ClientInitializer())
            .connect(host, port).sync() //connect
            .channel().closeFuture().sync() // shutdown

        workerGroup.shutdownGracefully()
    }

}

