package com.nfx.android.phonenumberpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * NFX Development
 * Created by nick on 29/01/17.
 */
class PreferenceControllerDelegate implements View.OnClickListener  {

    private static final String DEFAULT_PHONE_NUMBER = "";
    private static final int DEFAULT_DIALOG_STYLE = R.style.Phone_Number_Dialog_Default;
    private final Context context;
    private int dialogStyle;
    //controller stuff
    private String phoneNumber = DEFAULT_PHONE_NUMBER;
    private ViewStateListener viewStateListener;
    private PersistValueListener persistValueListener;
    private boolean isEnabled;
    // views
    private TextView phoneNumberView;

    PreferenceControllerDelegate(Context context) {
        this.context = context;
    }

    void setPersistValueListener(PersistValueListener persistValueListener) {
        this.persistValueListener = persistValueListener;
    }

    void setViewStateListener(ViewStateListener viewStateListener) {
        this.viewStateListener = viewStateListener;
    }

    void loadValuesFromXml(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PhoneNumberPreference);
            try {
                String phoneNumber =
                        a.getString(R.styleable.PhoneNumberPreference_pnp_default_phone_number);
                if(phoneNumber != null) {
                    this.phoneNumber = phoneNumber;
                }

                isEnabled =
                        a.getBoolean(R.styleable.PhoneNumberPreference_pnp_view_enabled, true);
                dialogStyle = DEFAULT_DIALOG_STYLE;
            }
            finally {
                a.recycle();
            }
        }
    }

    void onBind(View view) {
        phoneNumberView = view.findViewById(R.id.phone_number);
        setPhoneNumberTextView(phoneNumber);

        view.setOnClickListener(this);

        setEnabled(isEnabled());
    }

    private boolean isEnabled() {
        if(viewStateListener != null) {
            return viewStateListener.isEnabled();
        }
        else return isEnabled;
    }

    private void setEnabled(boolean enabled) {
        isEnabled = enabled;

        if(viewStateListener != null) {
            viewStateListener.setEnabled(enabled);
        }
    }

    void persistValues() {
        if(persistValueListener != null) {
            persistValueListener.persistString(phoneNumber);
        }
    }

    void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            phoneNumber = "";
        }

        this.phoneNumber = phoneNumber;

        if (persistValueListener != null) {
            persistValueListener.persistString(phoneNumber);
        }
    }

    void setDialogStyle(int dialogStyle) {
        this.dialogStyle = dialogStyle;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    interface ViewStateListener {
        boolean isEnabled();

        void setEnabled(boolean enabled);
    }

    private void setPhoneNumberTextView(String phoneNumber) {
        String formattedPhoneNumber;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            formattedPhoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
        } else {
            //noinspection deprecation
            formattedPhoneNumber = PhoneNumberUtils.formatNumber(phoneNumber);
        }
        phoneNumberView.setText(formattedPhoneNumber);
    }

    @Override
    public void onClick(final View v) {
        new CustomValueDialog(context, dialogStyle, phoneNumber)
                .setPersistValueListener(new PersistValueListener() {
                    @Override
                    public boolean persistString(String value) {
                        setPhoneNumber(value);

                        setPhoneNumberTextView(phoneNumber);
                        return true;
                    }
                })
                .show();
    }
}