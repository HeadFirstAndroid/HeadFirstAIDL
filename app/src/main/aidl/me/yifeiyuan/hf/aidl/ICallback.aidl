// ICallback.aidl
package me.yifeiyuan.hf.aidl;

// Declare any non-default types here with import statements

interface ICallback {

    void onSuccess(int code,String msg);

    void onError(int errorCode,String errorMsg);
}