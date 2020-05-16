package com.ali.alisimulate.entity;

/**
 * key是前端显示的文案，value是当这个数据作为搜索条件的时候给后端传的值
 */
public class KeyValue {
    private String value;

    public KeyValue(String value)  {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
