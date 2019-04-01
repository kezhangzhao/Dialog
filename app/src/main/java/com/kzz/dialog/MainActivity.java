package com.kzz.dialog;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kzz.dialoglibraries.DialogSetDateInterface;
import com.kzz.dialoglibraries.dialog.DialogCreate;
import com.kzz.dialoglibraries.popupWindow.PopupWindowBase;


public class MainActivity extends AppCompatActivity {

    private Button btDialog;
    private Button btPopupWindow;
    private Activity mActivity;

    private DialogCreate mDialogCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mActivity = this;
        btDialog = findViewById(R.id.bt_dialog);
        btPopupWindow = findViewById(R.id.bt_popup_window);
        btDialog.setOnClickListener(view -> showNoNetworkDialog());
        btPopupWindow.setOnClickListener(view -> showPopupWindow());
    }

    /**
     * 提交没有网络的时候弹窗
     */
    private void showNoNetworkDialog() {
        DialogCreate.Builder builder = new DialogCreate.Builder(mActivity);
        mDialogCreate = builder
                .setAddViewId(R.layout.dialog_contact_phone)
                .setIsHasCloseView(false)
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
}
