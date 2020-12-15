package me.yifeiyuan.hf.aidl

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import me.yifeiyuan.aidl.server.Account

/**
 * Created by 程序亦非猿 on 2020/12/12.
 */
class MessengerService : Service() {

    companion object {
        const val TAG = "MessengerService"

        class ServerHandler : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(TAG, "handleMessage() called with: msg = $msg")
                when (msg.what) {
                    1 -> {
                    }

                    2 -> {
                        val bundle = msg.obj as Bundle
                        bundle.classLoader = Account.javaClass.classLoader
                        val account = bundle.getParcelable<Account>("account")
                        Log.d(TAG, "handleMessage: 接受到 Account 信息：$account")
                    }

                    3 -> {
                        val bundle = msg.data as Bundle
                        bundle.classLoader = Account.javaClass.classLoader
                        val account = bundle.getParcelable<Account>("account")
                        Log.d(TAG, "handleMessage: 接受到 Account 信息：$account")
                    }

                    41 -> {

                    }

                    42 -> {

                    }
                }
            }
        }
    }

    private lateinit var messenger: Messenger

    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(ServerHandler())
        return messenger.binder
    }

}