package me.yifeiyuan.aidl.server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 程序亦非猿 on 2020/12/24.
 */
public class ParcelableTest implements Parcelable {

    public int intValue;
    public Class<?> classValue;

    public ParcelableTest(int intValue, Class<?> classValue) {
        this.intValue = intValue;
        this.classValue = classValue;
    }

    protected ParcelableTest(Parcel in) {
        intValue = in.readInt();
        Object value = in.readValue(getClass().getClassLoader());
        if (value instanceof Class<?>){
            classValue = (Class<?>) value;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(intValue);
        dest.writeValue(classValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableTest> CREATOR = new Creator<ParcelableTest>() {
        @Override
        public ParcelableTest createFromParcel(Parcel in) {
            return new ParcelableTest(in);
        }

        @Override
        public ParcelableTest[] newArray(int size) {
            return new ParcelableTest[size];
        }
    };

    @Override
    public String toString() {
        return "ParcelableTest{" +
                "intValue=" + intValue +
                ", classValue=" + classValue +
                '}';
    }
}
