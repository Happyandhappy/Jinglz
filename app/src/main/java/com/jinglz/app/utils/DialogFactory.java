package com.jinglz.app.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.jinglz.app.R;
import com.jinglz.app.ui.signup.OnYearPickedListener;

public final class DialogFactory {


    /**
     * Call this method to show simple alert message with specified context, title and message.
     * it will use simple alertDialog to show specified message with {@code title}.
     *
     * @param context Context for the dialog running in.
     * @param title String contains title for alertDialog
     * @param message String message to display
     * @return Dialog to display
     */
    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    /**
     * Call this method to show simple alert message with specified context, titleResource and messageResource.
     * it will use simple alertDialog to show specified message with {@code title}.
     *
     * @param context Context for the dialog running in.
     * @param titleResource for fetching title string from titleResource number
     * @param messageResource for fetching message string from messageResource number
     * @return Dialog to display
     */
    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                                         context.getString(titleResource),
                                         context.getString(messageResource));
    }

    /**
     * Call this method to show simple error message with specified context and message.
     * it will use simple alertDialog to show specified message with
     * {@code context.getString(R.string.dialog_error_title)}.
     *
     * @param context Context for the dialog running in.
     * @param message String message to display
     * @return Dialog to display
     */
    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    /**
     * Call this method to show progress dialog with specified message.
     *
     * @param context Context for the ProgressDialog running in.
     * @param message String value contains message to display
     * @return ProgressDialog to display
     */
    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    /**
     * Call this method to show year picker dialog on specified context with OnYearPickedListener.
     * it uses {@link NumberPicker} to show year picker. and Return AlertDialog.
     *
     * @param context Context for the dialog running in.
     * @param listener listen on postive button click
     * @return Dialog to display
     */
    public static Dialog createYearPickerDialog(Context context, OnYearPickedListener listener) {
        NumberPicker picker = new NumberPicker(context);
        picker.setMinValue(1900);
        picker.setMaxValue(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        picker.setValue(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        picker.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new AlertDialog.Builder(context)
                .setTitle(R.string.text_birth_year)
                .setView(picker)
                .setPositiveButton(R.string.action_select, (dialog1, which) -> listener.onPicked(picker.getValue()))
                .create();
    }
}
