package me.yifeiyuan.hf.clientapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.yifeiyuan.aidl.server.Account
import me.yifeiyuan.aidl.server.IServer

class MainActivity : AppCompatActivity() {

    private val TAG = "client_MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private lateinit var remoteServer: IServer

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected() called with: name = $name, service = $service")
            remoteServer = IServer.Stub.asInterface(service)

            var result = remoteServer.connectServer("client")
            Log.d(TAG, "connectServer() result :$result")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected() called with: name = $name")
        }

    }

    fun connectServer(view: View) {

        val intent = Intent("me.yifeiyuan.hf.aidl.Server.Action")
        intent.setPackage("me.yifeiyuan.hf.aidl")
        val result = bindService(intent, connection, BIND_AUTO_CREATE)
        Log.d(TAG, "bindService: $result")

        val intent2 = Intent()
        intent2.component = ComponentName("me.yifeiyuan.hf.aidl", "me.yifeiyuan.hf.aidl.Server")
        val result2 = bindService(intent2, connection, BIND_AUTO_CREATE)
        Log.d(TAG, "bindService: $result2")

    }

    fun getUserInfo(view: View) {
        val account = remoteServer.getAccountByName("")
        Log.d(TAG, "getAccountByName account= $account")
    }

    fun getAccounts(view: View) {
        val accounts = remoteServer.accounts
        Log.d(TAG, "accounts = $accounts")
    }

    fun testIn(view: View) {
        val account = Account().apply {
            name = "testIn"
        }
        Log.d(TAG, "testIn before= $account")
        remoteServer.testIn(account)
        Log.d(TAG, "testIn after= $account")
    }

    fun testOut(view: View) {
        val account = Account().apply {
            name = "testOut"
        }
        Log.d(TAG, "testOut before= $account")
        remoteServer.testOut(account)
        Log.d(TAG, "testOut after= $account")
    }

    fun testInout(view: View) {
        val account = Account().apply {
            name = "testInout"
        }
        Log.d(TAG, "testInout before= $account")
        remoteServer.testInout(account)
        Log.d(TAG, "testInout after= $account")
    }

    fun testOneway(view: View) {
        val account = Account().apply {
            name = "testOneway"
        }
        Log.d(TAG, "testOneway before= $account")
        remoteServer.testOneway(account)
        Log.d(TAG, "testOneway after= $account")
    }


}