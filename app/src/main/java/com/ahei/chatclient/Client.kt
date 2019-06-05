package com.ahei.chatclient

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

class Client(btn: ImageButton, text: EditText) : View.OnClickListener {


    private val host = "106.13.103.14"
    private val port = 8080
    private val btnSend = btn
    private val etMsg = text
    private lateinit var channel: Channel

    fun run() {
        val workerGroup = NioEventLoopGroup()

        val boot = Bootstrap()
        boot.group(workerGroup)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(ClientInitializer())

        channel = boot.connect(host, port).sync().channel() //connect

        btnSend.setOnClickListener(this)


        channel.closeFuture().sync() // shutdown

        workerGroup.shutdownGracefully()
    }

    override fun onClick(v: View?) {
        var msg = etMsg.text.toString().trim()
        if (msg.isNotEmpty()) {
            etMsg.setText("")
            msg += "\r\n"
            channel.writeAndFlush(msg)
        }
    }
}

