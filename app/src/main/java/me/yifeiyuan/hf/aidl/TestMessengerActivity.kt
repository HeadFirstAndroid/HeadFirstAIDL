package me.yifeiyuan.hf.aidl

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.yifeiyuan.aidl.server.Account

/**
 * 测试 Messenger
 */
@SuppressLint("HandlerLeak")
class TestMessengerActivity : AppCompatActivity() {

    private val TAG = "TestMessengerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_messenger)
    }

    private val clientHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d(TAG, "handleMessage() called with: msg = $msg")
        }
    }

    private val clientMessenger = Messenger(clientHandler)

    private var serverMessenger: Messenger? = null
    private var bound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected() called with: name = $name, service = $service")
            serverMessenger = Messenger(service)
            bound = true
            Toast.makeText(this@TestMessengerActivity, "绑定成功", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serverMessenger = null
            bound = false
            Toast.makeText(this@TestMessengerActivity, "解除绑定", Toast.LENGTH_SHORT).show()
        }
    }

    fun bindMessengerService(view: View) {
        Intent(this, MessengerService::class.java).also {
            bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * 发送简单的消息
     */
    fun sendMessageToServer(view: View) {
        if (bound) {
            val msg = Message.obtain(null, 1)
            try {
                serverMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 发送一个实现了 Parcelable 的 Account 对象过去，然而出现了 class not found 错误。
     *
     * 调试了下发现加载 Account 的类是 BootClassLoader 而不是 PathClassLoader
     *
     * 试下下这样直接加载是可以的：
     * Class.forName("me.yifeiyuan.aidl.server.Account")
     *
     * 而下面的代码是不行的：
     * Class.forName("me.yifeiyuan.aidl.server.Account",false,classLoader.parent)
     *
     *
     * 需要把对象放在一个 Bundle里，然后服务端在解析之前设置 Bundle 的 classloader 为 PathClassLoader
     */
    fun sendParcelableObjToServer(view: View) {
        if (bound) {
            val msg = Message.obtain(null, 2)

            val account = Account().apply {
                name = "messenger-account"
                age = 23
            }

            msg.obj = Bundle().apply {
                putParcelable("account", account)
            }

            try {
                serverMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
                //E/Parcel: Class not found when unmarshalling: me.yifeiyuan.aidl.server.Account
                //    java.lang.ClassNotFoundException: me.yifeiyuan.aidl.server.Account

                //W/Binder: Caught a RuntimeException from the binder stub implementation.
                //    android.os.BadParcelableException: ClassNotFoundException when unmarshalling: me.yifeiyuan.aidl.server.Account
            }
        }
    }

    /**
     * 通过 Message.data = Bundle() 来传递对象
     * 不过在解析的时候还是需要设置 bundle.classloader
     */
    fun sendParcelableObjToServer2(view: View) {
        if (bound) {
            val msg = Message.obtain(null, 3)

            val account = Account().apply {
                name = "account-message.data"
                age = 23
            }

            msg.data = Bundle().apply {
                putParcelable("account", account)
            }

            try {
                serverMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 发送一个没有实现 Parcelable 的对象试试
     *
     * 会 crash.
     *
     * Caused by: java.lang.RuntimeException: Can't marshal non-Parcelable objects across processes.
     * at android.os.Message.writeToParcel(Message.java:639)
     * at android.os.IMessenger$Stub$Proxy.send(IMessenger.java:118)
     * at android.os.Messenger.send(Messenger.java:57)
     * at me.yifeiyuan.hf.aidl.TestMessengerActivity.sendUnParcelableObjToServer(TestMessengerActivity.kt:98)
     */
    fun sendNonParcelableObjToServer(view: View) {
        if (bound) {
            val msg = Message.obtain(null, 4)
            msg.obj = view
            try {
                serverMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 把客户端的 Messenger 注册到服务端
     */
    fun registerClientMessenger(view: View) {
        if (bound) {
            val msg = Message.obtain(null, 41)
            msg.replyTo = clientMessenger
            try {
                serverMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 让服务端发送一个消息给客户端，进行双向通信
     */
    fun sendBackToClient(view: View) {
        if (bound) {
            val msg = Message.obtain(null, 42)
            try {
                serverMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bound) {
            unbindService(serviceConnection)
        }
    }
}