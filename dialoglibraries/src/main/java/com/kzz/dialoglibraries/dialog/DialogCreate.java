package com.kzz.dialoglibraries.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kzz.dialoglibraries.DialogSetDateInterface;
import com.kzz.dialoglibraries.R;

/**
 * 自定义dialog
 * <p>
 * Created by zhangzhao.ke on 2018/6/7.
 */

public class DialogCreate extends Dialog {

    private Context mContext;//上下文
    private int addViewId;//插入的view的id
    private View inflaterView;//插入的view
    private DialogSetDateInterface dialogSetDateInterface;//设置：插入布局中的控件、数据更改显示的接口
    private ImageView ivClose;//关闭按钮
    private boolean isHasCloseView;//是否显示下面的关闭按钮
    private boolean isCloseDialogInSide;//点击弹窗内面是否关闭弹窗
    private boolean isBackKeyCancelable;////点击物理返回键是否可以关闭弹窗。
    private View.OnClickListener closeViewOnClickListener;//下面的关闭按钮监听
    private boolean isTranslucent;//是否半透明:true表示是半透明，fales表示全透明背景。
    private int transparency;
    private LinearLayout ll_dialog_view;
    private RelativeLayout rlAllDialogLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
     * @param isHasCloseView   是否要底部的关闭按钮
     */
    public DialogCreate(@NonNull Context context, int addViewId, boolean isHasCloseView, DialogSetDateInterface setDateInterface) {
        super(context, R.style.AlertDialog);
        this.mContext = context;
        this.addViewId = addViewId;
        this.dialogSetDateInterface = setDateInterface;
        this.isHasCloseView = isHasCloseView;
    }

    /**
     * 构造方法
     *
     * @param context 上下文环境
     * @param builder 创建者对象
     */
    private DialogCreate(@NonNull Context context, Builder builder) {
        super(context, builder.style);
        this.mContext = context;
        this.isHasCloseView = builder.isHasCloseView;
        this.isTranslucent = builder.isTranslucent;
        this.addViewId = builder.addViewId;
        this.dialogSetDateInterface = builder.dialogSetDateInterface;
        this.closeViewOnClickListener = builder.closeViewOnClickListener;
        this.transparency = builder.transparency;
        this.isCloseDialogInSide = builder.isCloseDialogInSide;
        this.isBackKeyCancelable = builder.isBackKeyCancelable;
    }

    /**
     * 初始化控件，自定义布局插入到dialog中
     */
    private void initView() {
        if (getWindow() != null) {
            View view = getWindow().getLayoutInflater().inflate(R.layout.dialog_textlist_final_dismiss, null);
            ivClose = view.findViewById(R.id.iv_close);
            LinearLayout lladdview = view.findViewById(R.id.ll_add_textview);
            ll_dialog_view = view.findViewById(R.id.ll_dialog_view);
            rlAllDialogLayout = view.findViewById(R.id.rl_all_dialog_layout);
            if (!isCloseDialogInSide) {
                lladdview.setOnClickListener(v -> {

                });
            }
            View inflaterView = LayoutInflater.from(mContext).inflate(addViewId, lladdview, false);
            this.inflaterView = inflaterView;
            lladdview.addView(inflaterView);
            setContentView(view);
        }
    }

    /**
     * 设置new dialogSetDateInterface接口中的数据
     * inflaterView 插入的布局view 对象。
     */
    private void initDate() {
        if (isHasCloseView) {
            ivClose.setVisibility(View.VISIBLE);
        } else {
            ivClose.setVisibility(View.GONE);
        }
        dialogSetDateInterface.setDate(inflaterView);
        ll_dialog_view.getBackground().setAlpha(transparency);
    }

    /**
     * 初始化关闭按钮监听
     */
    private void initEvent() {
        if (closeViewOnClickListener != null) {
            ivClose.setOnClickListener(closeViewOnClickListener);
            rlAllDialogLayout.setOnClickListener(closeViewOnClickListener);
        } else {
            ivClose.setOnClickListener(new MyOnClickListener());
            rlAllDialogLayout.setOnClickListener(new MyOnClickListener());
        }
        setCancelable(isBackKeyCancelable);
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
     * @param dialog   弹窗对象
     */
    public void myShow(Activity activity, DialogCreate dialog) {
        show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = display.getWidth(); //设置宽度
            dialog.getWindow().setAttributes(lp);
        }
    }

    /**
     * 使用单例模式弹出
     */
    public void showSingle() {
        DialogCreateSingleUtils.getInstance().showDialogMsg(this);
    }


    /**
     * builder创建者
     */
    public static class Builder {
        private boolean isHasCloseView = true;//是否显示下面的关闭按钮
        private int addViewId;//插入的view的id
        private DialogSetDateInterface dialogSetDateInterface;//设置：插入布局中的控件、数据更改显示的接口
        private View.OnClickListener closeViewOnClickListener;//下面的关闭按钮监听
        private boolean isTranslucent;//是否半透明
        private Context mContext;//上下文
        private int style = R.style.AlertDialog;//半透明，透明指数：0.35;
        private int transparency;//透明度0~255透明度值 ，0为完全透明，255为不透明
        private boolean isCloseDialogInSide;//点击弹窗内面是否关闭弹窗
        private boolean isBackKeyCancelable = true;//点击物理返回键是否可以关闭弹窗。

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置：是否显示下面的关闭按钮，默认是显示
         *
         * @param isHasCloseView 是否显示下面的关闭按钮
         * @return 返回布尔值
         */
        public Builder setIsHasCloseView(boolean isHasCloseView) {
            this.isHasCloseView = isHasCloseView;
            return this;
        }

        /**
         * 设置：是否半透明 ，默认是半透明
         *
         * @param isTranslucent 是否半透明
         * @return 返回布尔值
         */
        public Builder setIsTranslucent(boolean isTranslucent) {
            this.isTranslucent = isTranslucent;
            if (isTranslucent) {//是否半透明
                this.style = R.style.AlertDialog;//半透明，透明指数：0.35
            } else {
                this.style = R.style.AlertDialog2;//全透明，透明指数：0
            }
            return this;
        }

        /**
         * 设置：插入的弹窗viewId
         *
         * @param addViewId 插入的弹窗viewId
         * @return Builder
         */
        public Builder setAddViewId(int addViewId) {
            this.addViewId = addViewId;
            return this;
        }

        /**
         * 插入的弹窗view，进行设置控件的数据回调接口，
         *
         * @param dialogSetDateInterface 回调
         * @return 返回Builder
         */
        public Builder setDialogSetDateInterface(DialogSetDateInterface dialogSetDateInterface) {
            this.dialogSetDateInterface = dialogSetDateInterface;
            return this;
        }

        /**
         * 关闭按钮的监听事件：自定义一个监听
         *
         * @param closeViewOnClickListener 自定义一个关闭监听
         * @return Builder
         */
        public Builder setCloseViewOnClickListener(View.OnClickListener closeViewOnClickListener) {
            this.closeViewOnClickListener = closeViewOnClickListener;
            return this;
        }

        /**
         * 设置透明度：0~255透明度值 ，0为完全透明，255为不透明
         * 建议设置透明值为：150
         *
         * @param transparency 设置透明度
         * @return 返回Builder
         */
        public Builder setTransparency(int transparency) {
            this.transparency = transparency;//0~255透明度值 ，0为完全透明，255为不透明
            setIsTranslucent(false);//把最外一层的半透明属性去掉，设置成全透明。
            return this;
        }

        /**
         * 设置弹窗内部点击是否能够关闭弹窗
         *
         * @param isClose boolean
         * @return 返回Builder
         */
        public Builder setIsCloseDialogInSide(boolean isClose) {
            this.isCloseDialogInSide = isClose;
            return this;
        }

        /**
         * 设置点击物理返回键是否可以关闭弹窗
         *
         * @param isClose boolean
         * @return 返回Builder
         */
        public Builder setIsBackKeyCancelable(boolean isClose) {
            this.isBackKeyCancelable = isClose;
            return this;
        }


        /**
         * 创建一个DialogCreate对象。里面有build创建者的属性值。
         *
         * @return 返回弹窗对象
         */
        public DialogCreate build() {
            return new DialogCreate(mContext, this);
        }
    }
}
