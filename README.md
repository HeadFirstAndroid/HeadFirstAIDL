# HeadFirstAIDL

学一下 AIDL + Messenger + Binder

## 工程结构

- aidlmodule： 定义了 AIDL 接口 IServer，同时被客户端和服务端依赖；
- app： 服务端 app，提供 IServer 的实现，具体是 Server 类；
- clientapp： 客户端 App，调用 IServer 提供的服务；


## 运行测试

AIDL 测试：安装 app 并运行，再安装 clientapp 并运行。

Messenger 测试：直接运行 app 就行，进入 Messenger界面，先绑定服务再测试。

## 遇到的问题

targetSdkVersion 30 的时候死活绑定不了 app 的 Server，改成 targetSdkVersion 28 就好了，30 又改啥了？？？