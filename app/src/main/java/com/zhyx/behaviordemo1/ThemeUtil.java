package com.zhyx.behaviordemo1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_LAYOUT_FLAGS;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;


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
     *
     * @hide
     */
    public static void setStatusBarTransparent(Activity activity, boolean dark, boolean transparentNav) {
        Window wd = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wd.clearFlags(FLAG_TRANSLUCENT_STATUS);
            wd.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//      SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布   局会被覆盖住
            int options = SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (dark && transparentNav) {
                //状态栏字体黑色 导航栏透明
                wd.getDecorView().setSystemUiVisibility(options | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                wd.setNavigationBarColor(Color.TRANSPARENT);
            } else if (dark && !transparentNav) {
                //状态栏字体黑色 导航栏不透明
                wd.getDecorView().setSystemUiVisibility(options | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                wd.setNavigationBarColor(Color.BLACK);
            } else if (!dark && transparentNav) {
                //状态栏字体白色 导航栏透明(透明表示contentView延展到导航栏布局)
                wd.getDecorView().setSystemUiVisibility(options | SYSTEM_UI_LAYOUT_FLAGS);
                wd.setNavigationBarColor(Color.TRANSPARENT);
            } else if (!dark && !transparentNav) {
                wd.getDecorView().setSystemUiVisibility(options);
                wd.setNavigationBarColor(Color.BLACK);
            }
            wd.setStatusBarColor(Color.TRANSPARENT);
        } else {
        }
    }
    public static void setStatusColor(Activity activity){
        Window wd = activity.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wd.setStatusBarColor(Color.WHITE);
        }
    }
    public static void setStatusColor(Activity activity, int color){
        Window wd = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wd.setStatusBarColor(color);
        }
    }
    private static class StatusError extends RuntimeException {
        public StatusError() {
        }

        public StatusError(String err) {
            System.out.println("异常：" + err);
        }
    }


    @SuppressLint("WrongConstant")
    public static void setStatusBarTransparent2(Activity activity) {
        WindowInsetsController windowInsetsController = activity.getWindow().getDecorView().getWindowInsetsController();
        assert windowInsetsController != null;
        windowInsetsController.setSystemBarsAppearance(8,8);
//        activity.getWindow().addFlags();
//        windowInsetsController.hide(WindowInsets.Type.statusBars());
    }
    public interface A extends WindowInsetsController {
        /**
         * Makes navigation bars become opaque with solid dark background and light foreground.
         * @hide
         */
        int b  = 0;
    }
}
