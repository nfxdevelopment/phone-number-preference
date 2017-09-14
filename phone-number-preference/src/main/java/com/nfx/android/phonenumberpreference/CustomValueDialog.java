package com.nfx.android.phonenumberpreference;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * NFX Development
 * Created by Nick on 21.01.16
 */
class CustomValueDialog {
    private final String phoneNumber;
    private Dialog dialog;
    private EditText phoneNumberEditText;
    private PersistValueListener persistValueListener;

    CustomValueDialog(Context context, int theme, String phoneNumber) {
        this.phoneNumber = phoneNumber;

        init(new AlertDialog.Builder(context, theme));
    }

    private void init(AlertDialog.Builder dialogBuilder) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(
                dialogBuilder.getContext()).inflate(R.layout.phone_number_dialog_layout, null);
        dialog = dialogBuilder.setView(dialogView).create();

        phoneNumberEditText = dialogView.findViewById(R.id.phone_number_edit_text);

        phoneNumberEditText.setText(phoneNumber);

        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        LinearLayout colorView = dialogView.findViewById(R.id.dialog_color_area);
        colorView.setBackgroundColor(fetchAccentColor(dialogBuilder.getContext()));

        Button applyButton = dialogView.findViewById(R.id.btn_apply);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryApply();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }

    CustomValueDialog setPersistValueListener(PersistValueListener listener) {
        persistValueListener = listener;
        return this;
    }

    void show() {
        dialog.show();
    }

    private void tryApply() {
        String phoneNumber = phoneNumberEditText.getText().toString();

        String normalizedPhoneNumber = normalizeNumber(phoneNumber);

        if(PhoneNumberUtils.isGlobalPhoneNumber(normalizedPhoneNumber)) {
            if(persistValueListener != null) {
                persistValueListener.persistString(normalizedPhoneNumber);
                dialog.dismiss();
            }
        } else {
            notifyWrongInput();
        }
    }

    public static String normalizeNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int len = phoneNumber.length();
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            // Character.digit() supports ASCII and Unicode digits (fullwidth, Arabic-Indic, etc.)
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                sb.append(digit);
            } else if (sb.length() == 0 && c == '+') {
                sb.append(c);
            } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                return normalizeNumber(PhoneNumberUtils.convertKeypadLettersToDigits(phoneNumber));
            }
        }
        return sb.toString();
    }

    private void notifyWrongInput() {
        phoneNumberEditText.setError("Phone number invalid");
    }
}
