package me.yifeiyuan.hf.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import me.yifeiyuan.aidl.server.Account
import me.yifeiyuan.aidl.server.IServer
import me.yifeiyuan.aidl.server.ParcelableTest

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

        override fun getAccountByName(name: String?): Account {
            return Account("程序亦非猿",66).also {
                Log.d(TAG, "getAccountByName() called : $it")
            }
        }

        override fun getAccounts(): MutableList<Account> {
            val a1 = Account("Fitz", 1)
            val a2 = Account("程序亦非猿",66)
            return mutableListOf(a1, a2)
        }

        override fun testIn(account: Account?) {
            Log.d(TAG, "testIn() called with: account = $account")
            account?.name = "testIn modified name"
        }

        override fun testOut(account: Account?) {
            Log.d(TAG, "testOut() called with: account = $account")
            account?.name = "testOut modified name"
        }

        override fun testInout(account: Account?) {
            Log.d(TAG, "testInout() called with: account = $account")
            account?.name = "testInout modified name"
        }

        override fun testOneway(account : Account) {
            Log.d(TAG, "testOneway() called with: account = $account")
            Thread.sleep(2000)
            account?.name = "testOneway modified name"
        }

        override fun testParcelable(test: ParcelableTest?) {
            Log.d(TAG, "testParcelable() called with: test = $test")
            val classV = test?.classValue;
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