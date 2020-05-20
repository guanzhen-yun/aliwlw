package com.ali.alisimulate.util;

import android.text.TextUtils;
import android.util.Log;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.entity.SaveLocalEntity;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public static void saveAndUpload(Map<String, ValueWrapper> reportData,  OnUploadSuccessListener onUploadSuccessListener) {
        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
            @Override
            public void onSuccess(String resID, Object o) {
                Log.e("ProductActivity", "属性上报成功");
                onUploadSuccessListener.onUnloadSuccess();
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
            if(propertyValue instanceof ValueWrapper.IntValueWrapper) {
                Integer value = ((ValueWrapper.IntValueWrapper) propertyValue).getValue();
                return value != null && value == 1;
            } else if(propertyValue instanceof ValueWrapper.BooleanValueWrapper) {
                Integer value = ((ValueWrapper.BooleanValueWrapper) propertyValue).getValue();
                return value != null && value == 1;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void putEnumParam(int valueKey, Map<String, ValueWrapper> reportData, String indentify) {
        reportData.put(indentify, new ValueWrapper.EnumValueWrapper(valueKey));
    }

    public static void putIntParam(int value, Map<String, ValueWrapper> reportData, String indentify) {
        reportData.put(indentify, new ValueWrapper.IntValueWrapper(value));
    }

    public static void putStringParam(String value, Map<String, ValueWrapper> reportData, String indentify) {
        reportData.put(indentify, new ValueWrapper.StringValueWrapper(value));
    }

    public static void putDoubleParam(double value, Map<String, ValueWrapper> reportData, String indentify) {
        reportData.put(indentify, new ValueWrapper.DoubleValueWrapper(value));
    }


    public static Integer getEnumVal(String indentify) {
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(indentify);
        if (propertyValue != null) {
            Integer value = ((ValueWrapper.EnumValueWrapper) propertyValue).getValue();
            return value;
        } else {
            return null;
        }
    }

    public static Integer getIntVal(String indentify) {
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(indentify);

        if (propertyValue != null) {
            Integer value = ((ValueWrapper.IntValueWrapper) propertyValue).getValue();
            return value;
        } else {
            return null;
        }
    }

    public static String getEnumValue(Property propertyStatus, Integer value) {
        EnumSpec specs = (EnumSpec) propertyStatus.getDataType().getSpecs();
        Set<String> strings = specs.keySet();
        List<String> listValue = new ArrayList<>();
        for (String string : strings) {
            listValue.add(specs.get(string));
        }
        String v = listValue.get(value);
        return v;
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

    public static void saveBoolean(boolean isBool, String indentify) {
        save(TmpConstant.TYPE_VALUE_BOOLEAN, String.valueOf(isBool), indentify);
    }

    public static void saveDouble(double d, String indentify) {
        save(TmpConstant.TYPE_VALUE_DOUBLE, String.valueOf(d), indentify);
    }

    public static void saveInt(int i, String indentify) {
        save(TmpConstant.TYPE_VALUE_INTEGER, String.valueOf(i), indentify);
    }

    public static void saveString(String str, String indentify) {
        save(TmpConstant.TYPE_VALUE_STRING, String.valueOf(str), indentify);
    }

    public static void saveEnum(int enumKey, String indentify) {
        save(TmpConstant.TYPE_VALUE_ENUM, String.valueOf(enumKey), indentify);
    }

    public static void save(String dataType, String localStr, String indentify) {
        String str = SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_LOCAN_ALI);
        Gson gson = new Gson();
        ArrayList<SaveLocalEntity> saveLocalEntities = null;
        if (!TextUtils.isEmpty(str)) {
            saveLocalEntities = gson.fromJson(str, new TypeToken<List<SaveLocalEntity>>() {
            }.getType());
            int index = -1;
            for (int i = 0; i < saveLocalEntities.size(); i++) {
                if (saveLocalEntities.get(i).indentify.equals(indentify)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                saveLocalEntities.remove(index);
            }
        } else {
            saveLocalEntities = new ArrayList<>();
        }
        SaveLocalEntity entity = new SaveLocalEntity();
        entity.indentify = indentify;
        entity.dataType = dataType;
        entity.localStr = localStr;
        saveLocalEntities.add(entity);
        String s = gson.toJson(saveLocalEntities);
        SharedPreferencesUtils.save(MyApp.getApp(), Constants.KEY_LOCAN_ALI, s);
    }

    public static ArrayList<SaveLocalEntity> getAliList() {
        String str = SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_LOCAN_ALI);
        if (!TextUtils.isEmpty(str)) {
            ArrayList<SaveLocalEntity> saveLocalEntities = new Gson().fromJson(str, new TypeToken<List<SaveLocalEntity>>() {
            }.getType());
            return saveLocalEntities;
        } else {
            return null;
        }
    }

    private static void addMap(Map<String, ValueWrapper> value1, String selectWeek, String time) {
        value1.put("Timer", new ValueWrapper.StringValueWrapper(selectWeek + ";" + time));
        value1.put("Enable", new ValueWrapper.BooleanValueWrapper(1));
    }

    public static void upLoadLocalData(OnUploadSuccessListener onUploadSuccessListener) {
        String weekO = SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_OPEN_WEEK);
        String weekC = SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_CLOSE_WEEK);
        String timeO = SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_OPEN_TIME);
        String timeC = SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_CLOSE_TIME);

        Map<String, ValueWrapper> reportData = new HashMap<>();
        if (!TextUtils.isEmpty(timeO) || !TextUtils.isEmpty(timeC)) {
            List<ValueWrapper> localTimer = new ArrayList<>();
            if (!TextUtils.isEmpty(timeO)) {
                Map<String, ValueWrapper> value1 = new HashMap<>();
                addMap(value1, weekO, timeO);
                ValueWrapper.StructValueWrapper structValueWrapper = new ValueWrapper.StructValueWrapper();
                structValueWrapper.setValue(value1);
                localTimer.add(structValueWrapper);
            } else {
                Map<String, ValueWrapper> value2 = new HashMap<>();
                ValueWrapper.StructValueWrapper structValueWrapper2 = new ValueWrapper.StructValueWrapper();
                structValueWrapper2.setValue(value2);
                localTimer.add(structValueWrapper2);
            }

            if (!TextUtils.isEmpty(timeC)) {
                Map<String, ValueWrapper> value1 = new HashMap<>();
                addMap(value1, weekC, timeC);
                ValueWrapper.StructValueWrapper structValueWrapper = new ValueWrapper.StructValueWrapper();
                structValueWrapper.setValue(value1);
                localTimer.add(structValueWrapper);
            } else {
                Map<String, ValueWrapper> value2 = new HashMap<>();
                ValueWrapper.StructValueWrapper structValueWrapper2 = new ValueWrapper.StructValueWrapper();
                structValueWrapper2.setValue(value2);
                localTimer.add(structValueWrapper2);
            }
            SaveAndUploadAliUtil.putList("LocalTimer", reportData, localTimer);
        }

        ArrayList<SaveLocalEntity> aliList = SaveAndUploadAliUtil.getAliList();
        if (aliList != null && aliList.size() > 0) {
            for (SaveLocalEntity entity : aliList) {
                if (TmpConstant.TYPE_VALUE_DOUBLE.equals(entity.dataType)) {
                    putDoubleParam(Double.parseDouble(entity.localStr), reportData, entity.indentify);
                } else if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(entity.dataType)) {
                    putBoolParam(("true".equals(entity.localStr) ? true : false), reportData, entity.indentify);
                } else if (TmpConstant.TYPE_VALUE_ENUM.equals(entity.dataType)) {
                    putEnumParam(Integer.parseInt(entity.localStr), reportData, entity.indentify);
                } else if (TmpConstant.TYPE_VALUE_STRING.equals(entity.dataType)) {
                    putStringParam(entity.localStr, reportData, entity.indentify);
                } else if (TmpConstant.TYPE_VALUE_INTEGER.equals(entity.dataType)) {
                    putIntParam(Integer.parseInt(entity.localStr), reportData, entity.indentify);
                }
            }
        }

        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
            @Override
            public void onSuccess(String resID, Object o) {
                Log.e("ProductActivity", "属性上报成功");
                if (onUploadSuccessListener != null) {
                    onUploadSuccessListener.onUnloadSuccess();
                }
            }

            @Override
            public void onError(String resId, AError aError) {
                Log.e("ProductActivity", "属性上报失败");
                if (onUploadSuccessListener != null) {
                    onUploadSuccessListener.onUnloadSuccess();
                }
            }
        });
    }

    public interface OnUploadSuccessListener {
        void onUnloadSuccess();
    }
}
