package com.ali.alisimulate.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * key是前端显示的文案，value是当这个数据作为搜索条件的时候给后端传的值
 */
public class KeyValue implements Parcelable {
    private String key;
    private String value;
    private boolean isSelectAll;
    private boolean isChecked;

    public KeyValue(String key, String value)  {
        this.key = key;
        this.value = value;
    }

    protected KeyValue(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<KeyValue> CREATOR = new Creator<KeyValue>() {
        @Override
        public KeyValue createFromParcel(Parcel in) {
            return new KeyValue(in);
        }

        @Override
        public KeyValue[] newArray(int size) {
            return new KeyValue[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isSelectAll() {
        return isSelectAll;
    }

    public void setSelectAll(boolean selectAll) {
        isSelectAll = selectAll;
    }
}
