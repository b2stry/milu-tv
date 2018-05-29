package com.shallowan.milutv.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shallowan.milutv.R;


/**
 * Created by ShallowAn.
 */


public class TransParentNoDimDialog {

    protected Activity activity;
    protected Dialog dialog;

    public TransParentNoDimDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog_nodim);
    }

    public void setContentView(View view) {
        dialog.setContentView(view);
    }

    public void setWidthAndHeight(int width, int height) {
        Window win = dialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        if (params != null) {
            params.width = width;//设置x坐标
            params.height = height;//设置y坐标
            win.setAttributes(params);
        }
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }
}
