package com.ahei.chatclient


import com.ahei.chatclient.model.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

class MessageHandler: SimpleChannelInboundHandler<String>(){

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush("--UID $UID\r\n")
    }
    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        if(msg.startsWith("Welcome ", false)){
            UID = msg.substring(8, msg.length - 1)
            EventBus.getDefault().post(ChatMessageEvent("You are $UID"))
        }else if(msg.startsWith("Online ", false)){
            ONLINE_NUMBER = msg.substring(7, msg.length).toInt()
            EventBus.getDefault().post(Integer(ONLINE_NUMBER))
        }else{
            val index1 = msg.indexOf(' ')
            val index2 = msg.indexOf(' ', index1 + 1)
            val uid = msg.substring(0, index1)
            val text = msg.substring(index2)
            EventBus.getDefault().post(Message(uid, text))
        }

    }

    /*
    try to reconnect when channel broken
     */
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        LOGGER.warn("$UID unregistered")

        ctx.channel().eventLoop().schedule(object: Runnable{
            override fun run() {
                Client.connect()
            }
        }, Client.CONNECT_DELAY, TimeUnit.SECONDS)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}