package com.duxl.mobileframe.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

/**
 * Created by duxl on 2016/08/24.
 */
public class AlertDialog {

    private Context mContext;
    private android.app.AlertDialog.Builder builder;
    private android.app.AlertDialog mAlertDialog;

    public AlertDialog(Context context) {
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new android.app.AlertDialog.Builder(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            builder = new android.app.AlertDialog.Builder(mContext);
        }
        builder.setCancelable(false);

    }

    public void setTitle(CharSequence title) {
        builder.setTitle(title);
    }

    public void setTitle(int titleId) {
        builder.setTitle(titleId);
    }

    public void setMessage(CharSequence message) {
        builder.setMessage(message);
    }

    public void setMessage(int messageId) {
        builder.setMessage(messageId);
    }

    public AlertDialog setPositiveButton(CharSequence text, final OnClickListener listener) {
        builder.setPositiveButton(text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.onClick(AlertDialog.this);
                }
            }
        });
        return this;
    }

    public AlertDialog setPositiveButton(int textId, final OnClickListener listener) {
        builder.setPositiveButton(textId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.onClick(AlertDialog.this);
                }
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(CharSequence text, final OnClickListener listener) {
        builder.setNegativeButton(text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.onClick(AlertDialog.this);
                }
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(int textId, final OnClickListener listener) {
        builder.setNegativeButton(textId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.onClick(AlertDialog.this);
                }
            }
        });
        return this;
    }

    public void show() {
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    public void dismiss() {
        if(mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }


    public interface OnClickListener {
        void onClick(AlertDialog dialog);
    }
}
