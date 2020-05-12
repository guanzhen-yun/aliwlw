package com.ali.alisimulate.entity;

import java.io.Serializable;

/**
 * Author:关震
 * Date:2020/5/12 17:55
 * Description:RegistDeviceResult
 **/
public class RegistDeviceResult implements Serializable {
    public String certSN;
    public String certificate;
    public String deviceName;
    public String deviceSecret;
    public String privateKey;
    public String productKey;
    public int status;
}
