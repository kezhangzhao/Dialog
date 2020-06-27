package com.kzz.dialoglibraries.utils;

import android.app.Activity;
import android.os.Build;

public class ActivityUtils {
    /**
     * 判断Activity是否Destroy
     *
     * @param mActivity Activity
     * @return true:已销毁
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}
