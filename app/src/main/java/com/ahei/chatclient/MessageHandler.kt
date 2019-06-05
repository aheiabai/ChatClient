package com.ahei.chatclient


import android.widget.Toast
import com.ahei.chatclient.model.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.greenrobot.eventbus.EventBus

class MessageHandler: SimpleChannelInboundHandler<String>(){

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: String) {
        if(msg.startsWith("Welcome ", false)){
            UID = msg.substring(8, msg.length - 1)
            EventBus.getDefault().post(ChatMessageEvent("You are $UID"))
        }else{
            val index1 = msg.indexOf(' ')
            val index2 = msg.indexOf(' ', index1 + 1)
            val uid = msg.substring(0, index1)
            val text = msg.substring(index2)
            EventBus.getDefault().post(Message(uid, text))
        }

    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}