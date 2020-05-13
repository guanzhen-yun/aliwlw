package com.ali.alisimulate.entity;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/13 17:42
 * Description:OrgDevice
 **/
public class OrgDevice {
    public int page;
    public int rows;
    public int total;
    public List<DeviceList> data;
    public class DeviceList {
        public int activeTime;
        public String bindingStatus;
        public String deviceComment;
        public String deviceId;
        public String deviceModel;
        public String deviceName;
        public String lastOnlineTime;
        public int num;
        public int leftTime;
        public String onlineStatus;
        public String productKey;
        public String productName;
        public String romVersion;
    }
}
