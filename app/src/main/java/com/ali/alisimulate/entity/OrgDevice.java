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
        public long activeTime;
        public String bindingStatus;
        public String deviceComment;
        public String deviceId;
        public String deviceModel;
        public String deviceName;
        public String lastOnlineTime;
        public int num;
        public long leftTime;
        public String onlineStatus;
        public String productKey;
        public String productName;
        public String romVersion;
        public String brandName;
        public String deviceSecret;
        public boolean isCheck;
    }
}
