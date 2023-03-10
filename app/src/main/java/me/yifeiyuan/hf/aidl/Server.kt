package me.yifeiyuan.hf.aidl

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.Process
import android.text.TextUtils
import android.util.Log
import me.yifeiyuan.aidl.server.Account
import me.yifeiyuan.aidl.server.Callback
import me.yifeiyuan.aidl.server.IServer
import me.yifeiyuan.aidl.server.ParcelableTest
import java.security.Permission

//IServer 的服务端实现
class Server : Service() {

    private val TAG = "Server"

    var callback: Callback? = null

    private val server: IServer = object : IServer.Stub() {

        override fun connectServer(token: String?, cb: Callback?): Boolean {
            Log.d(TAG, "connectServer() called with: token = $token")

            val permission = checkCallingPermission("com.taobao.taobao.storage.WRITE")
            Log.d(TAG, "connectServer: permission = $permission")

            Log.d(
                TAG,
                "connectServer() called with calling info: ${Binder.getCallingPid()},${Binder.getCallingUid()},${Binder.getCallingUserHandle()},"
            )

            Log.d(
                TAG,
                "connectServer() called with my info: ${Process.myPid()},${Process.myUid()},${Process.myTid()},"
            )

            var callerPackageName: String? = null
            packageManager.getPackagesForUid(Binder.getCallingUid())?.forEach {
                Log.d(TAG, "connectServer() called with pkgs: ${it},")
                callerPackageName = it
            }

            Log.d(TAG, "connectServer() called with getNameForUid: ${packageManager.getNameForUid(Binder.getCallingUid())},")

            if (TextUtils.isEmpty(callerPackageName) || whitePackageNameList.indexOf(callerPackageName) < 0) {
                // 非法访问
                Log.d(TAG, "connectServer() called callerPackageName 非法")
//            return null
            }

            callback = cb
            callback?.onCallback("callback data")
            if (token.equals("client")) {
                return true
            }
            return false
        }

        override fun getAccountByName(name: String?): Account {
            return Account("程序亦非猿", 66).also {
                Log.d(TAG, "getAccountByName() called : $it")
            }
        }

        override fun getAccounts(): MutableList<Account> {
            val a1 = Account("Fitz", 1)
            val a2 = Account("程序亦非猿", 66)
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

        override fun testOneway(account: Account) {
            Log.d(TAG, "testOneway() called with: account = $account")
            Thread.sleep(2000)
            account?.name = "testOneway modified name"
        }

        override fun testParcelable(test: ParcelableTest?) {
            Log.d(TAG, "testParcelable() called with: test = $test")
            val classV = test?.classValue;
        }

        //D/Server: testThread() called with thread : Binder:3159_4
        //D/Server: testThread() called with thread : Binder:3159_4
        override fun testThread() {
            Log.d(TAG, "testThread() called with thread : ${Thread.currentThread().name}")
        }
    }
    val whitePackageNameList = mutableListOf<String>("me.yifeiyuan.hf.aidl","me.yifeiyuan.hf.clientapp")

    override fun onBind(intent: Intent): IBinder? {

        Log.d(
            TAG,
            "onBind() called with calling info: ${Binder.getCallingPid()},${Binder.getCallingUid()},${Binder.getCallingUserHandle()},"
        )

        Log.d(
            TAG,
            "onBind() called with my info: ${Process.myPid()},${Process.myUid()},${Process.myTid()},"
        )

        if (Binder.getCallingUid() == Process.myUid()) {
            Log.d(TAG, "onBind() called uid 合法")
        }

        if (Binder.getCallingPid() == Process.myPid()) {
            Log.d(TAG, "onBind() called pid 合法")
        }

        if (packageManager.checkSignatures(Binder.getCallingUid(), Process.myUid()) == PackageManager.SIGNATURE_MATCH) {
            Log.d(TAG, "onBind() called 签名信息 合法")
        }

        var callerPackageName: String? = null
        packageManager.getPackagesForUid(Binder.getCallingUid())?.forEach {
            Log.d(TAG, "onBind() called with pkgs: ${it},")
            callerPackageName = it
        }

        Log.d(TAG, "onBind() called with getNameForUid: ${packageManager.getNameForUid(Binder.getCallingUid())},")

        if (TextUtils.isEmpty(callerPackageName) || whitePackageNameList.indexOf(callerPackageName) < 0) {
            // 非法访问
            Log.d(TAG, "onBind() called callerPackageName 非法")
//            return null
        }

        if (Binder.getCallingPid() != Process.myPid()) {
            Log.d(TAG, "onBind() called pid 非法")
//            return null
        }

        //onbind 的时候不是
        // 权限校验
        val permission = checkCallingOrSelfPermission("com.taobao.taobao.storage.WRITE")
        Log.d(TAG, "onBind: permission = $permission")
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onBind() called 权限非法")
//            return null
        }

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