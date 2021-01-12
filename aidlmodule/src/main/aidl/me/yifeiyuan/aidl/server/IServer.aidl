package me.yifeiyuan.aidl.server;

import me.yifeiyuan.aidl.server.Account;
import me.yifeiyuan.aidl.server.ParcelableTest;


//对应的实现是 Server
interface IServer {

    boolean connectServer(String token);

    Account getAccountByName(String name);

    List<Account> getAccounts();

    //in out inout 只会对函数参数有用，不会对返回值有效；   'me.yifeiyuan.aidl.server.Account' can be an out type, so you must declare it as in, out, or inout.
    void testIn(in Account account);

    void testOut(out Account account);

    void testInout(inout Account account);

    //oneway 修饰的方法不能有返回值，否则会报错：oneway method 'testOneway' cannot return a value
//    oneway Account testOneway();

    //oneway 还不能有 out的参数，oneway method 'testOneway' cannot have out parameters
//    oneway void testOneway(out Account account);

    oneway void testOneway(in Account accunt);

    void testParcelable(in ParcelableTest test);

    //测试一下线程情况
    void testThread();

}