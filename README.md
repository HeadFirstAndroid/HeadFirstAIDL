# HeadFirstAIDL

Head First AIDL  Binder RPC

## AIDL

aidlmodule 定义了 AIDL 接口 IServer
app 服务端 app 提供 IServer 的实现，具体是 Server 类
clientapp 客户端 App，调用 IServer 提供的服务

测试：安装 app 并运行，再安装 clientapp 并运行。




## 坑爹

targetSdkVersion 30 的时候死活绑定不了 app 的 Server，改成 targetSdkVersion 28 就好了，30 又改啥了？？？