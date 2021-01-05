
package android.os;

import android.os.Message;

/** @hide */
//oneway
 interface IMessenger {
   oneway void send(in Message msg);
}
