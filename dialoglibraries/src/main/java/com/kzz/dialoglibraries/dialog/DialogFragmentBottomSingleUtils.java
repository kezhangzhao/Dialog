package com.kzz.dialoglibraries.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

/**
 * author : zhangzhao.ke
 * time   : 2020/5/19
 * desc   : 防止弹出多个dialog，使用单例模式。
 */
public class DialogFragmentBottomSingleUtils {

    private static DialogFragmentBottomSingleUtils instance = null;
    private DialogFragmentBottom mDialogFragmentBottom;

    public static DialogFragmentBottomSingleUtils getInstance() {
        if (null == instance) {
            synchronized (DialogFragmentBottomSingleUtils.class) {
                if (instance == null) {
                    instance = new DialogFragmentBottomSingleUtils();
                }
            }
        }
        return instance;
    }
    void showDialogMsg(DialogFragmentBottom dialog, @NonNull FragmentManager manager, @Nullable String tag) {
        if (mDialogFragmentBottom == null || mDialogFragmentBottom.getDialog()==null
                ||!mDialogFragmentBottom.getDialog().isShowing()) {
            mDialogFragmentBottom = dialog;
            mDialogFragmentBottom.show(manager, tag);
        }
    }

}
