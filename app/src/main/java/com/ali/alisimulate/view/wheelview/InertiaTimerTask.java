// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

package com.ali.alisimulate.view.wheelview;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {

    final float velocityY;
    final WheelView wheelView;
    float a;

    InertiaTimerTask(WheelView wheelview, float velocityY) {
        super();
        wheelView = wheelview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                a = velocityY > 0.0F ? 2000F : -2000F;
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            wheelView.cancelFuture();
            wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        wheelView.totalScrollY -= i;
        if (!wheelView.isLoop) {
            float itemHeight = wheelView.getItemHeightOuter();
            if (wheelView.totalScrollY <= (int) ((-wheelView.initPosition) * itemHeight)) {
                a = 40F;
                wheelView.totalScrollY = (int) ((-wheelView.initPosition) * itemHeight);
            } else if (wheelView.totalScrollY >= (int) ((wheelView.items.size() - 1 - wheelView.initPosition) * itemHeight)) {
                wheelView.totalScrollY = (int) ((wheelView.items.size() - 1 - wheelView.initPosition) * itemHeight);
                a = -40F;
            }
        }
        a = a < 0.0F ? a + 20F : a - 20F;
        wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
