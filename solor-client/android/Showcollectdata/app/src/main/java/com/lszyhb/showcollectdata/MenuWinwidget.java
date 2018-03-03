package com.lszyhb.showcollectdata;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;


/**
 * Created by kkk8199 on 12/28/17.
 */

public class MenuWinwidget {

    private static WindowManager mWindowManager = null;
    private static Context mContext = null;
    public static Boolean isShown = false;
    private static MyTreewidget mytree;

    /**
     * 显示弹出框
     *
     * @param context
     */
    public static void showMenuWindow(final Context context,MyTreewidget mytreevalue) {
        if (isShown) {
          Log.i("kkk8199", "return cause already shown");
            return;
        }

        isShown = true;
        mytree=mytreevalue;

        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        // 设置flag

        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = 720;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.RIGHT;

        mWindowManager.addView(mytree, params);

    }

    /**
     * 隐藏弹出框
     */
    public void hidePopupWindow() {

        if (isShown && null != mytree) {
            mWindowManager.removeView(mytree);
            isShown = false;
        }

    }

}

