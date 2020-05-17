package com.ali.alisimulate.util;

import android.util.Log;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import java.util.List;
import java.util.Map;

/**
 * Author:关震
 * Date:2020/5/17 8:44
 * Description:SaveAndUploadAliUtil 保存并上传
 **/
public class SaveAndUploadAliUtil {
    public static void saveAndUpload(Map<String, ValueWrapper> reportData) {
        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
            @Override
            public void onSuccess(String resID, Object o) {
                Log.e("ProductActivity", "属性上报成功");
            }

            @Override
            public void onError(String resId, AError aError) {
                Log.e("ProductActivity", "属性上报失败");
            }
        });
    }

    public static void putBoolParam(boolean bool, Map<String, ValueWrapper> reportData, String indentify) {
        reportData.put(indentify, new ValueWrapper.BooleanValueWrapper(bool ? 1 : 0));
    }

    public static boolean getBoolValue(String indentify) {
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(indentify);
        if (propertyValue != null) {
            Integer value = ((ValueWrapper.BooleanValueWrapper) propertyValue).getValue();
            if (value != null && value == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void putList(String indentify, Map<String, ValueWrapper> reportData, List<ValueWrapper> list) {
        reportData.put(indentify, new ValueWrapper.ArrayValueWrapper(list));
    }

    public static List<ValueWrapper> getList(String indentify) {
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(indentify);
        if (propertyValue == null) {
            return null;
        } else {
            List<ValueWrapper> value = ((ValueWrapper.ArrayValueWrapper) propertyValue).getValue();
            return value;
        }
    }
}
