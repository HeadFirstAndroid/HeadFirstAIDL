package me.yifeiyuan.aidl.server

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 程序亦非猿 on 2020/11/20.
 */
class Account(
    var name: String?, var age: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Account(name=$name, age=$age)"
    }

}