package com.ahei.chatclient


import android.widget.Toast
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.greenrobot.eventbus.EventBus

class MessageHandler: SimpleChannelInboundHandler<String>(){

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: String) {
        EventBus.getDefault().post(ChatMessageEvent(msg))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}