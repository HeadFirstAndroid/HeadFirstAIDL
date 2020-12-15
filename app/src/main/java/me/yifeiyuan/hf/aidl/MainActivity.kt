package me.yifeiyuan.hf.aidl

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    var server :IRemoteServer? = null

    var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected() called with: name = $name, service = $service")
            server = IRemoteServer.Stub.asInterface(service)
            server?.init(object : ICallback {
                override fun asBinder(): IBinder {
                    return Binder()
                }

                override fun onSuccess(code: Int, msg: String?) {
                    Log.d(TAG, "onSuccess() called with: code = $code, msg = $msg")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                }

            })

            server?.request()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected() called with: name = $name")
        }
    }

    fun bindService(view: View) {
        bindService(Intent(this, RemoteService::class.java), connection, BIND_AUTO_CREATE)
    }

    fun testMessenger(view: View) {
        startActivity(Intent(this, TestMessengerActivity::class.java))
    }
}