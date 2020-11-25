package me.yifeiyuan.aidl.server

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 程序亦非猿 on 2020/11/20.
 */
class Account @JvmOverloads constructor(
    var name: String? = "default name", var age: Int = 1
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun toString(): String {
        return "Account(name=$name, age=$age)" + super.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun readFromParcel(reply: Parcel) {
        name = reply.readString().toString()
        age = reply.readInt()
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }

}