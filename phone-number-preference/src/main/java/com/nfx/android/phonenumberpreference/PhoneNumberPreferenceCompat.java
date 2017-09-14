package com.nfx.android.phonenumberpreference;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;

/**
 * NFX Development
 * Created by nick on 29/01/17.
 */
public class PhoneNumberPreferenceCompat extends Preference implements
        PreferenceControllerDelegate.ViewStateListener, PersistValueListener {

    private PreferenceControllerDelegate controllerDelegate;

    public PhoneNumberPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public PhoneNumberPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public PhoneNumberPreferenceCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PhoneNumberPreferenceCompat(Context context) {
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
    public void onBindViewHolder(PreferenceViewHolder viewRoot) {
        super.onBindViewHolder(viewRoot);
        controllerDelegate.onBind(viewRoot.itemView);
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
