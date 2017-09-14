package com.nfx.android.phonenumberpreference;

import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * NFX Development
 * Created by nick on 29/01/17.
 */
public class PhoneNumberPreference extends Preference implements
        PreferenceControllerDelegate.ViewStateListener , PersistValueListener {

    private PreferenceControllerDelegate controllerDelegate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PhoneNumberPreference(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public PhoneNumberPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public PhoneNumberPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PhoneNumberPreference(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        setLayoutResource(R.layout.phone_number_view_layout);

        controllerDelegate = new PreferenceControllerDelegate(getContext());

        controllerDelegate.setViewStateListener(this);
        controllerDelegate.setPersistValueListener(this);

        controllerDelegate.loadValuesFromXml(attrs);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        controllerDelegate.onBind(view);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        String phoneNumber = controllerDelegate.getPhoneNumber();

        controllerDelegate.setPhoneNumber(getPersistedString(phoneNumber));
        controllerDelegate.persistValues();
    }

    @Override
    public boolean persistString(String value) {
        return super.persistString(value);
    }

    public void setDialogStyle(int dialogStyle) {
        controllerDelegate.setDialogStyle(dialogStyle);
    }
}
