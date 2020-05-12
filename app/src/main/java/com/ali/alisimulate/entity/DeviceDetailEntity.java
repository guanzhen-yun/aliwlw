package com.ali.alisimulate.entity;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/12 13:45
 * Description:DeviceDetailEntity
 **/
public class DeviceDetailEntity {
public int activeTime;
    public String bindingStatus;
    public String brandName;
    public String deviceComment;
    public String deviceId;
    public String deviceModel;
    public String deviceName;
    public String deviceSecret;
    public String deviceTypeName;
    public int expiredFitting;
    public int faultStatus;
    public List<FittingDetail> fittingDetails;
    public int lastOnlineTime;
    public String macAddress;
    public int nonOriginalFitting;
    public String onlineStatus;
    public String productCompany;
    public String productKey;
    public String productLine;
    public String producter;
    public String region;
    public String romVersion;
    public String userName;

    public class FittingDetail {
        public String brandName;
        public String fittingId;
        public String fittingName;
        public int installTime;
        public String lifeStatus;
        public int numBoundDevice;
    }
}
