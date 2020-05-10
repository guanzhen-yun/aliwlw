package com.ali.alisimulate.entity;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.dm.api.DeviceInfo;

import java.util.List;

/*
 *设备信息
 */

public class DeviceInfoData extends DeviceInfo {

    /**
     * 与网关关联的子设备信息
     * 后续网关测试demo 会 添加子设备 删除子设备 建立 topo关系 子设备上下线等
     */
    public List<DeviceInfo> subDevice = null;
    public String password = null;
    public String username = null;
    public String clientId = null;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
