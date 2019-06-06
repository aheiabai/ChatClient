package com.ahei.chatclient

import android.app.Application
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Slf4JLoggerFactory

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE)

    }
}