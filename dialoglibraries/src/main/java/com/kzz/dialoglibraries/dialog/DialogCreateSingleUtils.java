package com.kzz.dialoglibraries.dialog;

/**
 * author : zhangzhao.ke
 * time   : 2018/12/10
 * desc   : 防止弹出多个dialog，使用单例模式。
 */

public class DialogCreateSingleUtils {

    private static DialogCreateSingleUtils instance = null;
    private DialogCreate mDialogCreate;

    public static DialogCreateSingleUtils getInstance() {
        if (null == instance) {
            synchronized (DialogCreateSingleUtils.class) {
                if (instance == null) {
                    instance = new DialogCreateSingleUtils();
                }
            }
        }
        return instance;
    }
    void showDialogMsg(DialogCreate dialog) {
        if (mDialogCreate == null || !mDialogCreate.isShowing())
            mDialogCreate = dialog;
        mDialogCreate.show();
    }
}
