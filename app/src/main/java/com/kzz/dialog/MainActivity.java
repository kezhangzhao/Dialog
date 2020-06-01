package com.kzz.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kzz.dialoglibraries.DialogSetDateInterface;
import com.kzz.dialoglibraries.dialog.DialogCreate;
import com.kzz.dialoglibraries.dialog.DialogFragmentBottom;
import com.kzz.dialoglibraries.popupWindow.PopupWindowBase;
import com.kzz.dialoglibraries.utils.ScreenUtils;


public class MainActivity extends AppCompatActivity {

    private Button btDialog;
    private Button btPopupWindow;
    private Button btDialogBottom;
    private Activity mActivity;

    private DialogCreate mDialogCreate;
    private DialogFragmentBottom mDialogBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mActivity = this;
        btDialog = findViewById(R.id.bt_dialog);
        btPopupWindow = findViewById(R.id.bt_popup_window);
        btDialogBottom = findViewById(R.id.bt_dialog_bottom);
        btDialog.setOnClickListener(view -> showNoNetworkDialog());
        btPopupWindow.setOnClickListener(view -> showPopupWindow());
        btDialogBottom.setOnClickListener(view -> showDialogBottom());
    }

    /**
     * 提交没有网络的时候弹窗
     */
    private void showNoNetworkDialog() {
        DialogCreate.Builder builder = new DialogCreate.Builder(mActivity);
        mDialogCreate = builder
                .setAddViewId(R.layout.dialog_contact_phone)
                .setIsHasCloseView(false)
                .setDialogWidth((float) (ScreenUtils.getScreenWidth(this) - ScreenUtils.dp2px(this, 60)) / ScreenUtils.getScreenWidth(this))
//                .setTransparency(150)//默认：全屏dialog，如果设置这个透明度值后，上面的标题栏则不会被dialog挡住。
                .setDialogSetDateInterface(inflaterView -> {
                    TextView tvMsg = inflaterView.findViewById(R.id.tv_dialog_msg);
                    TextView tvCancel = inflaterView.findViewById(R.id.tv_cancel);
                    TextView tvConfirm = inflaterView.findViewById(R.id.tv_confirm);
                    tvMsg.setText("当前无网络，将自动保存！\n请待有网络后提交。");
                    tvConfirm.setOnClickListener(v -> {
                        mDialogCreate.dismiss();
                        Toast.makeText(this, "确认", Toast.LENGTH_SHORT).show();
                    });
                    tvCancel.setOnClickListener(v -> {
                        mDialogCreate.dismiss();
                        Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                    });
                })
                .build();
        mDialogCreate.showSingle();
    }

    private void showDialogBottom() {
        DialogFragmentBottom.Builder builder = new DialogFragmentBottom.Builder(mActivity);
        mDialogBottom = builder
                .setAddViewId(R.layout.dialog_contact_phone)
                .setIsVisitCancel(false)
//                .setTransparency(150)//默认：全屏dialog，如果设置这个透明度值后，上面的标题栏则不会被dialog挡住。
                .setDialogSetDateInterface(inflaterView -> {
                    TextView tvMsg = inflaterView.findViewById(R.id.tv_dialog_msg);
                    TextView tvCancel = inflaterView.findViewById(R.id.tv_cancel);
                    TextView tvConfirm = inflaterView.findViewById(R.id.tv_confirm);
                    tvMsg.setText("当前无网络，将自动保存！\n请待有网络后提交。");
                    tvConfirm.setOnClickListener(v -> {
                        mDialogCreate.dismiss();
                        Toast.makeText(this, "确认", Toast.LENGTH_SHORT).show();
                    });
                    tvCancel.setOnClickListener(v -> {
                        mDialogCreate.dismiss();
                        Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                    });
                })
                .build();
        mDialogBottom.showSingle(getSupportFragmentManager(), "MainActivity");
    }

    /**
     * 显示弹出窗口
     */
    private void showPopupWindow() {
        PopupWindowBase popupWindowBase = new PopupWindowBase(mActivity, btPopupWindow, R.layout.pop_seletct);
        DialogSetDateInterface dialogSetDateInterface = inflaterView -> {
            TextView tv001 = inflaterView.findViewById(R.id.tv_001);
            TextView tv002 = inflaterView.findViewById(R.id.tv_002);
            TextView tv003 = inflaterView.findViewById(R.id.tv_003);
            tv001.setOnClickListener(view -> Toast.makeText(mActivity, "tv001", Toast.LENGTH_SHORT).show());
            tv002.setOnClickListener(view -> Toast.makeText(mActivity, "tv002", Toast.LENGTH_SHORT).show());
            tv003.setOnClickListener(view -> Toast.makeText(mActivity, "tv003", Toast.LENGTH_SHORT).show());

        };
//        popupWindowBase.setBackgroundResource(R.color.blace_00000000);
        popupWindowBase.setShowingInterface(isShowing -> {
            if (isShowing) {
                Toast.makeText(mActivity, "显示PopupWindow", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "关闭PopupWindow", Toast.LENGTH_SHORT).show();
            }
        });
        popupWindowBase.setCallback(dialogSetDateInterface);
        popupWindowBase.create();
    }

    /**
     * 获得屏幕宽度
     *
     * @param context context
     * @return 屏幕宽度
     */
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null)
            wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
