package com.kzz.dialoglibraries.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kzz.dialoglibraries.DialogSetDateInterface;
import com.kzz.dialoglibraries.R;

/**
 * author : zhangzhao.ke
 * time   : 2018/09/12
 * desc   : 充满屏幕的dialog
 */

public class FullScreenDialog extends Dialog {

    private Context mContext;//上下文
    private int addViewId;//插入的view的id
    private View inflaterView;//插入的view
    private DialogSetDateInterface dialogSetDateInterface;//设置：插入布局中的控件、数据更改显示的接口
    private boolean isBackKeyCancelable;//点击物理返回键是否可以关闭弹窗。
    private boolean isCloseDialogInSide;//点击弹窗内面是否关闭弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyCompat();
        initView();
        initDate();
        initEvent();
    }

    /**
     * 构造方法
     *
     * @param context          上下文环境
     * @param addViewId        自定义的布局，将此布局插入到dialog中显示
     * @param setDateInterface 设置：插入布局中的控件、数据更改显示
     */
    public FullScreenDialog(@NonNull Context context, int addViewId, DialogSetDateInterface setDateInterface) {
        super(context, R.style.AlertDialog);
        applyCompat();
        this.mContext = context;
        this.addViewId = addViewId;
        this.dialogSetDateInterface = setDateInterface;
    }

    /**
     * 构造方法
     *
     * @param context 上下文环境
     * @param builder 创建者对象
     */
    private FullScreenDialog(@NonNull Context context, Builder builder) {
        super(context, R.style.AlertDialog);
        this.mContext = context;
        this.addViewId = builder.addViewId;
        this.dialogSetDateInterface = builder.dialogSetDateInterface;
        this.isBackKeyCancelable = builder.isBackKeyCancelable;
        this.isCloseDialogInSide = builder.isCloseDialogInSide;
    }

    /**
     * 初始化控件，自定义布局插入到dialog中
     */
    private void initView() {
        if (getWindow() != null) {
            View view = getWindow().getLayoutInflater().inflate(R.layout.linear_layout, null);
            LinearLayout llAddView = view.findViewById(R.id.ll_add_view);
            if (isCloseDialogInSide)
                llAddView.setOnClickListener(v -> dismiss());
            View inflaterView = LayoutInflater.from(mContext).inflate(addViewId, llAddView, false);
            this.inflaterView = inflaterView;
            llAddView.addView(inflaterView);
            setContentView(view);
        }
    }

    /**
     * 设置new dialogSetDateInterface接口中的数据
     * inflaterView 插入的布局view 对象。
     */
    private void initDate() {
        dialogSetDateInterface.setDate(inflaterView);
    }

    /**
     * 初始化关闭按钮监听
     */
    private void initEvent() {
        setCancelable(isBackKeyCancelable);
    }

    /**
     * 解决被系统任务栏被挡住
     */
    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (getWindow() != null)
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 默认关闭按钮监听事件
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    /**
     * 充满屏幕显示dialog
     * show:显示dialog（自定义透明度的得用这个方法，不能用show,不然屏幕不能充满）
     *
     * @param activity 当前窗口
     */
    public void myShow(Activity activity) {
        show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if (getWindow() != null) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = display.getWidth(); //设置宽度
            lp.height = ViewGroup.LayoutParams.FILL_PARENT;
            getWindow().setAttributes(lp);
        }
    }


    /**
     * builder创建者
     */
    public static class Builder {
        private int addViewId;//插入的view的id
        private DialogSetDateInterface dialogSetDateInterface;//设置：插入布局中的控件、数据更改显示的接口
        private Context mContext;//上下文
        private boolean isBackKeyCancelable = true;//点击物理返回键是否可以关闭弹窗。
        private boolean isCloseDialogInSide;//点击弹窗内面是否关闭弹窗

        public Builder(Context context) {
            this.mContext = context;
        }


        /**
         * 设置：插入的弹窗viewId
         *
         * @param addViewId 插入的弹窗viewId
         * @return Builder
         */
        public FullScreenDialog.Builder setAddViewId(int addViewId) {
            this.addViewId = addViewId;
            return this;
        }

        /**
         * 插入的弹窗view，进行设置控件的数据回调接口，
         *
         * @param dialogSetDateInterface 回调
         * @return 返回Builder
         */
        public FullScreenDialog.Builder setDialogSetDateInterface(DialogSetDateInterface dialogSetDateInterface) {
            this.dialogSetDateInterface = dialogSetDateInterface;
            return this;
        }

        /**
         * 设置点击物理返回键是否可以关闭弹窗
         *
         * @param isClose boolean
         * @return 返回Builder
         */
        public FullScreenDialog.Builder setIsBackKeyCancelable(boolean isClose) {
            this.isBackKeyCancelable = isClose;
            return this;
        }

        /**
         * 设置弹窗内部点击是否能够关闭弹窗
         *
         * @param isClose boolean
         * @return 返回Builder
         */
        public FullScreenDialog.Builder setIsCloseDialogInSide(boolean isClose) {
            this.isCloseDialogInSide = isClose;
            return this;
        }

        /**
         * 创建一个MyDialog对象。里面有build创建者的属性值。
         *
         * @return 返回弹窗对象
         */
        public FullScreenDialog build() {
            return new FullScreenDialog(mContext, this);
        }
    }
}
