package com.kzz.dialoglibraries.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kzz.dialoglibraries.DialogSetDateInterface;
import com.kzz.dialoglibraries.R;

/**
 * 自定义底部弹窗（从手机端底部弹起）
 * Created by kezhangzhao on 2018/3/28.
 */

@SuppressLint("ValidFragment")
public class DialogFragmentBottom extends DialogFragment {
    private Activity mActivity;
    private LinearLayout llAddTextView;
    private View inflaterView;
    private int addViewId;//插入的view的id
    private DialogSetDateInterface dialogSetDateInterface;//设置：插入布局中的控件、数据更改显示的接口
    private LinearLayout ll_cancel;//取消按钮的布局
    private boolean isVisitCancel;//是否显示取消按钮,默认情况是显示
    private View.OnClickListener cancelListener;//取消按钮监听事件，默认情况是只取消dialog
    private TextView tv_cancel;//取消按钮控件

    /**
     * 构造方法
     *
     * @param builder 创建者
     */
    public DialogFragmentBottom(Builder builder) {
        this.dialogSetDateInterface = builder.dialogSetDateInterface;
        this.addViewId = builder.addViewId;
        this.mActivity = builder.mActivity;
        this.isVisitCancel = builder.isVisitCancel;
        this.cancelListener = builder.cancelListener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        //去掉dialog的标题，需要在setContentView()之前
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //设置dialog的位置在底部
            lp.gravity = Gravity.BOTTOM;
            //设置dialog的动画
            lp.windowAnimations = R.style.BottomDialogAnimation;
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable());
        }
        //获取底部弹窗的view，默认带有取消按钮
        final View view = inflater.inflate(R.layout.dialog_bottomdialog_base, null);
        ll_cancel = view.findViewById(R.id.ll_cancel);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        llAddTextView = view.findViewById(R.id.ll_add_textview);
        //将传进来的弹窗layout布局，加载到基础view中去。
        View inflaterView = LayoutInflater.from(mActivity).inflate(addViewId, llAddTextView, false);
        this.inflaterView = inflaterView;
        llAddTextView.addView(inflaterView);

        initDate();//初始化：数据、监听、布局

        return view;
    }

    /**
     * 设置回调，inflaterView用来对传进来的弹窗layout布局进行控件的操作,
     * 初始化监听，取消按钮是否显示
     */
    public void initDate() {
        //设置回调，inflaterView用来对传进来的弹窗layout布局进行控件的操作
        if (dialogSetDateInterface != null)
            dialogSetDateInterface.setDate(inflaterView);
        //初始化监听，取消按钮是否显示
        if (cancelListener != null) {
            tv_cancel.setOnClickListener(cancelListener);
        } else {
            tv_cancel.setOnClickListener(new MyCancelListener());
        }
        if (isVisitCancel) {
            ll_cancel.setVisibility(View.VISIBLE);
        } else {
            ll_cancel.setVisibility(View.GONE);
        }
    }


    class MyCancelListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isDestroy(mActivity))
        super.show(manager, tag);
    }

    /**
     * 使用单例模式弹出
     */
    public void showSingle(@NonNull FragmentManager manager, @Nullable String tag) {
        DialogFragmentBottomSingleUtils.getInstance().showDialogMsg(this,manager,tag);
    }

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

    /**
     * builder创建者
     */
    public static class Builder {

        private Activity mActivity;
        private int addViewId;//插入的view的id
        private DialogSetDateInterface dialogSetDateInterface;//设置：插入布局中的控件、数据更改显示的接口
        private boolean isVisitCancel = true;//是否显示取消按钮,默认是显示的。
        private View.OnClickListener cancelListener;//取消按钮监听事件

        public Builder(Activity mActivity) {
            this.mActivity = mActivity;
        }

        /**
         * 设置：插入的弹窗viewId
         *
         * @param addViewId addViewId
         * @return Builder
         */
        public Builder setAddViewId(int addViewId) {
            this.addViewId = addViewId;
            return this;
        }

        /**
         * 插入的弹窗view，进行设置控件的数据回调接口，
         *
         * @param dialogSetDateInterface DialogSetDateInterface
         * @return Builder
         */
        public Builder setDialogSetDateInterface(DialogSetDateInterface dialogSetDateInterface) {
            this.dialogSetDateInterface = dialogSetDateInterface;
            return this;
        }

        /**
         * 设置是否显示取消按钮
         *
         * @param isVisit isVisit
         * @return Builder
         */
        public Builder setIsVisitCancel(Boolean isVisit) {
            this.isVisitCancel = isVisit;
            return this;
        }

        /**
         * 设置取消按钮监听事件
         *
         * @param listener OnClickListener
         * @return Builder
         */
        public Builder setCancelListener(View.OnClickListener listener) {
            this.cancelListener = listener;
            return this;
        }

        /**
         * 创建一个DialogCreate对象。里面有build创建者的属性值。
         *
         * @return DialogFragmentBottom
         */
        public DialogFragmentBottom build() {
            return new DialogFragmentBottom(this);
        }
    }
}
