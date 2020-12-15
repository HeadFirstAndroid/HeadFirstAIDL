package me.yifeiyuan.hf.aidlmodule;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 程序亦非猿 on 2020/12/14.
 */
@Deprecated
public class AccountInJava implements Parcelable {

    private String name;
    private int age;

    public AccountInJava(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected AccountInJava(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<AccountInJava> CREATOR = new Creator<AccountInJava>() {

        @Override
        public AccountInJava createFromParcel(Parcel in) {
            return new AccountInJava(in);
        }

        @Override
        public AccountInJava[] newArray(int size) {
            return new AccountInJava[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }
}
