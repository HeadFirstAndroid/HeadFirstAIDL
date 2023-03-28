package me.yifeiyuan.hf.clientapp

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.yifeiyuan.aidl.server.Account
import me.yifeiyuan.aidl.server.Callback
import me.yifeiyuan.aidl.server.IServer
import me.yifeiyuan.aidl.server.ParcelableTest
import java.io.ByteArrayInputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateFactory


/**
 * AIDL 的客户端实现
 * 测试 AIDL 接口
 */
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

            try {
                var result = remoteServer.connectServer("client", object : Callback.Stub() {
                    override fun onTransact(
                        code: Int,
                        data: Parcel,
                        reply: Parcel?,
                        flags: Int
                    ): Boolean {
                        Log.d(
                            TAG,
                            "onTransact() called with calling info: ${Binder.getCallingPid()},${Binder.getCallingUid()},${Binder.getCallingUserHandle()},"
                        )
                        return super.onTransact(code, data, reply, flags)
                    }

                    override fun onCallback(data: String?) {
                        Log.d(
                            TAG,
                            "onCallback() called with calling info: ${Binder.getCallingPid()},${Binder.getCallingUid()},${Binder.getCallingUserHandle()},"
                        )

                        Log.d(
                            TAG,
                            "onCallback() called with my info: ${Process.myPid()},${Process.myUid()},${Process.myTid()},"
                        )
                        //me.yifeiyuan.hf.aidl
                        val targetPkg = getPackageNameByUid(Binder.getCallingUid())
                        Log.d(
                            TAG,
                            "onCallback() called with calling getPackagesForUid: ${targetPkg},"
                        )

                        val fingerprint = getFingerprint(this@MainActivity, targetPkg ?: "")
                        Log.d(TAG, "onCallback() called with calling fingerprint: ${fingerprint},")
                    }
                })
                Log.d(TAG, "connectServer() result :$result")
                Toast.makeText(this@MainActivity, "链接成功", Toast.LENGTH_SHORT).show()
            } catch (e: RemoteException) {
                e.printStackTrace()
            } catch (e: DeadObjectException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected() called with: name = $name")
        }
    }

    fun PackageManager.getPackageNameByUid(uid: Int = Binder.getCallingUid()): String {
        return getPackagesForUid(uid)?.get(0) ?: getNameForUid(uid) ?: ""
    }

    fun Context.getPackageNameByUid(uid: Int = Binder.getCallingUid()): String {
        return packageManager.getPackageNameByUid(uid)
    }

    /**
     * 获取目标应用的签名指纹
     *
     * @param aContext Context
     * @param aPkgName 目标应用的包名
     * @return 签名指纹
     */
    fun getFingerprint(aContext: Context, aPkgName: String): String? {
        val cert: Certificate? = getCertificate(aContext, aPkgName)
        var fingerprint: String? = null
        try {
            if (cert != null) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-1")
//                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
//                val md: MessageDigest = MessageDigest.getInstance("MD5")
//                md.reset()
                md.update(cert.getEncoded())
                fingerprint = toHexString(md.digest(), false)
            }
        } catch (aE: NoSuchAlgorithmException) {
            aE.printStackTrace()
        } catch (aE: CertificateEncodingException) {
            aE.printStackTrace()
        }
        return fingerprint
    }

    private fun getCertificate(aContext: Context, aPkgName: String): Certificate? {
        val pm = aContext.packageManager
        var bais: ByteArrayInputStream? = null
        try {
            @SuppressLint("PackageManagerGetSignatures") val pi =
                pm.getPackageInfo(aPkgName, PackageManager.GET_SIGNATURES)
            bais = ByteArrayInputStream(pi.signatures[0].toByteArray())
            val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
            return cf.generateCertificate(bais)
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            if (null != bais) {
                try {
                    bais.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }


    /**
     * 转换为十六进制字符数据
     *
     * @param aBytes     待处理的数据
     * @param aUpperCase 输出是否大写
     * @return 十六进制字符数据
     */
    fun toHexString(aBytes: ByteArray, aUpperCase: Boolean): String {
        val hexString = StringBuilder()
        for (b in aBytes) {
            var str = Integer.toHexString(0xFF and b.toInt())
            if (aUpperCase) {
                str = str.toUpperCase()
            }
            if (str.length == 1) {
                hexString.append("0")
            }
            hexString.append(str)
        }
        return hexString.toString()
    }

    fun connectServer(view: View) {

//        val intent = Intent("me.yifeiyuan.hf.aidl.Server.Action")
        val intent = Intent("me.yifeiyuan.hf.aidl.Server.Action")
        intent.setPackage("me.yifeiyuan.hf.aidl")
        val result = bindService(intent, connection, BIND_AUTO_CREATE)
        Log.d(TAG, "bindService: $result")

//        val intent2 = Intent()
//        intent2.component = ComponentName("me.yifeiyuan.hf.aidl", "me.yifeiyuan.hf.aidl.Server")
//        val result2 = bindService(intent2, connection, BIND_AUTO_CREATE)
//        Log.d(TAG, "bindService by ComponentName: $result2")
    }

    fun disconnectServer(view: View) {
        unbindService(connection)
        Log.d(TAG, "disconnectServer")
    }

    fun getUserInfo(view: View) {
        try {
            val account = remoteServer.getAccountByName("")
            Log.d(TAG, "getAccountByName account= $account")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun testParcelable(view: View) {
        try {
            remoteServer.testParcelable(ParcelableTest(111, ParcelableTest::class.java))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
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

        Thread.sleep(3000)
        Log.d(TAG, "testOneway after 3000 = $account")
    }

    //D/Server: testThread() called with thread : Binder:31793_1
    fun testThreadOnMainThread(v: View) {
        remoteServer.testThread()
    }

    //D/Server: testThread() called with thread : Binder:31793_1
    fun testThreadOnAsyncThread(v: View) {
        Thread() {
            remoteServer.testThread()
        }.start()
    }

}