package me.yifeiyuan.hf.aidl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class RemoteService : Service() {

    private val TAG = "RemoteService"

    private var server: RemoteServerImpl

    init {
        server = RemoteServerImpl()
    }

    class RemoteServerImpl : IRemoteServer.Stub() {

        private var _callback: ICallback? = null

        override fun init(callback: ICallback?) {
            _callback = callback
            _callback?.onSuccess(1, "errorMsg")
        }

        override fun request() {
            _callback?.onError(1, "wrong request")
        }

    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind() called with: intent = $intent")
        return server
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind() called with: intent = $intent")
        return super.onUnbind(intent)
    }
}