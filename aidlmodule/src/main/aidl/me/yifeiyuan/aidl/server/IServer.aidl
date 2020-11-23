package me.yifeiyuan.aidl.server;

import me.yifeiyuan.aidl.server.Account;

interface IServer {

    boolean connectServer(String token);

    Account getAccountByName(String name);

    List<Account> getAccounts();
}