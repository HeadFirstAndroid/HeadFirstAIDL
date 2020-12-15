package me.yifeiyuan.hf.aidlmodule

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 程序亦非猿 on 2020/11/20.
 */
class Holder() :Parcelable{
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Holder> {
        override fun createFromParcel(parcel: Parcel): Holder {
            return Holder(parcel)
        }

        override fun newArray(size: Int): Array<Holder?> {
            return arrayOfNulls(size)
        }
    }
}