// IRemoteServer.aidl
package me.yifeiyuan.hf.aidl;

// Declare any non-default types here with import statements
import me.yifeiyuan.hf.aidl.ICallback;

interface IRemoteServer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

     void init(ICallback callback);

     void request();
}