// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

package com.ali.alisimulate.view.wheelview;

import java.util.TimerTask;

final class SmoothScrollTimerTask extends TimerTask {

    final WheelView wheelView;
    int realTotalOffset;
    int realOffset;
    int offset;

    SmoothScrollTimerTask(WheelView wheelview, int offset) {
        wheelView = wheelview;
        this.offset = offset;
        realTotalOffset = Integer.MAX_VALUE;
        realOffset = 0;
    }

    @Override
    public final void run() {
        if (realTotalOffset == Integer.MAX_VALUE) {
            realTotalOffset = offset;
        }
        realOffset = (int) (realTotalOffset * 0.1F);

        if (realOffset == 0) {
            realOffset = realTotalOffset < 0 ? -1 : 1;
        }
        if (Math.abs(realTotalOffset) <= 0) {
            wheelView.cancelFuture();
            wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
        } else {
            wheelView.totalScrollY += realOffset;
            wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
            realTotalOffset -= realOffset;
        }
    }
}
