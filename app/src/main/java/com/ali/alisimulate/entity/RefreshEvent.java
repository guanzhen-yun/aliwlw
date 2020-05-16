package com.ali.alisimulate.entity;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;

/**
 * Author:关震
 * Date:2020/5/16 6:52
 * Description:RefreshEvent
 **/
public class RefreshEvent {
    public AMessage aMessage;

    public RefreshEvent(AMessage aMessage) {
        this.aMessage = aMessage;
    }


}
