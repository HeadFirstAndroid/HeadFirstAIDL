package me.yifeiyuan.hf.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import me.yifeiyuan.aidl.server.IServer

//IServer 的服务端实现
class Server : Service() {

    private val TAG = "Server"

    private val server: IServer = object : IServer.Stub() {

        override fun connectServer(token: String?): Boolean {

            Log.d(TAG, "connectServer() called with: token = $token")

            if (token.equals("client")){
                return true
            }

            return false
        }
    }


    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind() called with: intent = $intent")
        return server.asBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(
            TAG,
            "onStartCommand() called with: intent = $intent, flags = $flags, startId = $startId"
        )
        return super.onStartCommand(intent, flags, startId)
    }
}