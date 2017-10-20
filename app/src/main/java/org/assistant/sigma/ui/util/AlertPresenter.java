package org.assistant.sigma.ui.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import org.assistant.sigma.R;

/**
 * Created by giovanni on 19/10/17.
 *
 */
public class AlertPresenter {

    public static void error(Context mContext, @StringRes int titleId,
                             @StringRes int messageId) {
        error(
                mContext,
                titleId != 0 ? mContext.getString(titleId) : null,
                mContext.getString(messageId)
        );
    }

    public static void error(Context mContext, @Nullable String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (title != null) {
            builder.setTitle(title);
        }

        builder.setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void confirm(Context mContext, @StringRes int titleId, @StringRes int messageId,
                               @StringRes int okBtnTextId, DialogInterface.OnClickListener onConfirmListener) {
        confirm(
                mContext,
                titleId != 0 ? mContext.getString(titleId): null,
                mContext.getString(messageId),
                mContext.getString(okBtnTextId),
                onConfirmListener
        );
    }

    public static void confirm(Context mContext, @Nullable String title, String message,
                               String okBtnText, DialogInterface.OnClickListener onConfirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (title != null) {
            builder.setTitle(title);
        }

        builder.setMessage(message)
                .setPositiveButton(okBtnText, onConfirmListener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
