package com.kzz.dialoglibraries.popupWindow;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kzz.dialoglibraries.DialogPopuSetDateInterface;
import com.kzz.dialoglibraries.DialogSetDateInterface;
import com.kzz.dialoglibraries.DialogShowingInterface;
import com.kzz.dialoglibraries.R;
import com.kzz.dialoglibraries.utils.ActivityUtils;


/**
 * Created by Administrator on 2016/8/9.
 */
public class PopupWindowBase {

    protected PopupWindow popupWindow;
    protected Activity mActivity;
    private View parentView;
    private int addViewId;
    private int backgroundResource = R.color.blace_66000000;

    protected DialogPopuSetDateInterface callback;
    protected DialogShowingInterface showingInterface;

    public DialogPopuSetDateInterface getCallback() {
        return callback;
    }

    public void setCallback(DialogPopuSetDateInterface callback) {
        this.callback = callback;
    }

    public DialogShowingInterface getShowingInterface() {
        return showingInterface;
    }

    public void setShowingInterface(DialogShowingInterface showingInterface) {
        this.showingInterface = showingInterface;
    }

    public int getBackgroundResource() {
        return backgroundResource;
    }

    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    /**
     * 构造方法
     *
     * @param activity   Activity
     * @param parentView 显示在哪个view的下面
     * @param addViewId  插入显示的view
     */
    public PopupWindowBase(Activity activity, View parentView, int addViewId) {
        this.mActivity = activity;
        this.parentView = parentView;
        this.addViewId = addViewId;
    }

    public void create() {

        View baseView = LayoutInflater.from(mActivity).inflate(R.layout.popup_window_base, null);
        popupWindow = new PopupWindow(baseView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LinearLayout llBaseView = baseView.findViewById(R.id.ll_base_view);
        View inflaterView = LayoutInflater.from(mActivity).inflate(addViewId, llBaseView, false);
        if (getCallback() != null)
            getCallback().setDate(inflaterView,popupWindow);
        llBaseView.addView(inflaterView);
        llBaseView.setBackgroundResource(getBackgroundResource());
        llBaseView.setOnClickListener(v -> dismiss());
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(() -> {
            if (getShowingInterface() != null)
                getShowingInterface().setShowing(false);
        });
        if (!ActivityUtils.isDestroy(mActivity))
        showAsDropDown(parentView, 0, 0);

    }

    /**
     * @param parentView 哪个view的下面弹出
     * @param xoff       x轴偏移
     * @param yoff       y轴偏移
     */
    public void showAsDropDown(final View parentView, final int xoff, final int yoff) {
        if (popupWindow != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                Rect visibleFrame = new Rect();
                parentView.getGlobalVisibleRect(visibleFrame);
                int height = parentView.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                popupWindow.setHeight(height);
                popupWindow.showAsDropDown(parentView, xoff, yoff);
            } else {
                popupWindow.showAsDropDown(parentView, xoff, yoff);
            }
        }
        if (getShowingInterface() != null)
            getShowingInterface().setShowing(true);
    }


    /**
     * 关闭
     */
    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

}
