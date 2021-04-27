package com.zhyx.behaviordemo1.meituan;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * 状态栏工具箱
 *
 * @author 玉米
 * @time 2020.6.11
 */
public class ThemeUtil {

    /**
     * 状态栏沉浸式-透明底色，白色字体
     * 布局在system顶层开始
     * 只针对sdk>=21
     *
     * @param activity
     * @param dark
     * @param transparentNav
     */
    public static void setStatusBarTransparent(Activity activity, boolean dark, boolean transparentNav) {
        Window wd = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wd.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            wd.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布   局会被覆盖住
            int options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (dark && transparentNav) {
                //状态栏字体黑色 导航栏透明
                wd.getDecorView().setSystemUiVisibility(options | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                wd.setNavigationBarColor(Color.TRANSPARENT);
            } else if (dark && !transparentNav) {
                //状态栏字体黑色 导航栏不透明
                wd.getDecorView().setSystemUiVisibility(options | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                wd.setNavigationBarColor(Color.WHITE);
            } else if (!dark && transparentNav) {
                //状态栏字体白色 导航栏透明(透明表示contentView延展到导航栏布局)
                wd.getDecorView().setSystemUiVisibility(options | View.SYSTEM_UI_LAYOUT_FLAGS);
                wd.setNavigationBarColor(Color.TRANSPARENT);
            } else if (!dark && !transparentNav) {
                wd.getDecorView().setSystemUiVisibility(options);
                wd.setNavigationBarColor(Color.WHITE);
            }
            wd.setStatusBarColor(Color.TRANSPARENT);
        } else {
//            throw new StatusError("当前sdk版本小于21,无法使用此状态栏修改!");
        }
    }

    private static class StatusError extends RuntimeException {
        public StatusError() {
        }

        public StatusError(String err) {
            System.out.println("异常：" + err);
        }
    }
}
