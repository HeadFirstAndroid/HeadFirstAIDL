package me.yifeiyuan.hf.aidl.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import me.yifeiyuan.aidl.server.Account
import me.yifeiyuan.hf.aidl.App

/**
 * Created by 程序亦非猿 on 2020/12/12.
 */
class MessengerService : Service() {

    companion object {
        const val TAG = "MessengerService"

        class ServerHandler : Handler() {

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(TAG, "MessengerService handleMessage() called with: msg = $msg")
                Toast.makeText(App.app,"MessengerService handleMessage: 接受到信息：$msg",Toast.LENGTH_SHORT).show()

                when (msg.what) {

                    1 -> {//客户端发送简单消息

                    }

                    2 -> {//客户端发送带对象的消息 msg.obj
                        val bundle = msg.obj as Bundle
                        bundle.classLoader = Account::class.java.classLoader //Thread.currentThread().contextClassLoader App.javaClass.classLoader 也可以
                        val account = bundle.getParcelable<Account>("account")
                        Log.d(TAG, "MessengerService handleMessage: 接受到 Account 信息：$account")
                        Toast.makeText(App.app,"MessengerService handleMessage: 接受到 Account 信息：$account",Toast.LENGTH_SHORT).show()
                    }

                    3 -> {//客户端发送带对象的消息 msg.data
                        val bundle = msg.data as Bundle
                        bundle.classLoader = Account::class.java.classLoader
                        val account = bundle.getParcelable<Account>("account")
                        Log.d(TAG, "MessengerService handleMessage: 接受到 Account 信息：$account")
                        Toast.makeText(App.app,"MessengerService handleMessage: 接受到 Account 信息：$account",Toast.LENGTH_SHORT).show()
                    }

                    41 -> {

                        val client = msg.replyTo

                    }

                    42 -> {//双向通信测试，服务端向客户端发送消息
                        msg.replyTo?.send(Message().apply {
                            what = 1
                        })
                    }
                }
            }
        }
    }

    private var messenger: Messenger = Messenger(ServerHandler())

    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }

}