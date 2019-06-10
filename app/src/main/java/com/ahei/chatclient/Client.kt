package com.ahei.chatclient


import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

object Client {
    private const val HOST = "106.13.103.14"
//    private const val HOST = "192.168.0.112"
    private const val PORT = 8080
    const val CONNECT_DELAY: Long = 7
    private val bootstrap: Bootstrap = Bootstrap()
    var channel: Channel? = null

    fun connect() {
        channel = bootstrap.connect(HOST, PORT).sync().channel() //connect
    }

    fun run() {
        val workerGroup = NioEventLoopGroup()

        bootstrap.group(workerGroup)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(ClientInitializer())

        connect()


//        channel.closeFuture().sync() // shutdown
//        workerGroup.shutdownGracefully()
    }


}

