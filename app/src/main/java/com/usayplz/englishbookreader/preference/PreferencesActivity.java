package com.usayplz.englishbookreader.preference;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.widget.Toast;

import com.lb.material_preferences_library.PreferenceActivity;
import com.lb.material_preferences_library.custom_preferences.EditTextPreference;
import com.lb.material_preferences_library.custom_preferences.ListPreference;
import com.usayplz.englishbookreader.R;

/**
 * Created by Sergei Kurikalov on 09/02/16.
 * u.sayplz@gmail.com
 */
public class PreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    public static final int REQUEST_SETTINGS_CHANGED = 1002;
    private boolean isChanged;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isChanged = false;
        findPreference(getString(R.string.pref_font_size)).setOnPreferenceChangeListener(this);
        findPreference(getString(R.string.pref_margin)).setOnPreferenceChangeListener(this);
        initSummary(getPreferenceScreen());
    }

    @Override
    protected int getPreferencesXmlId() {
        return R.xml.preferences;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        isChanged = true;
        Preference pref = findPreference(key);
        updatePrefSummary(findPreference(key));

    }

    private void initSummary(Preference preference) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                initSummary(preferenceGroup.getPreference(i));
            }
        } else {
            updatePrefSummary(preference);
        }
    }

    private void updatePrefSummary(Preference preference) {
        if (preference instanceof ListPreference) {
            ListPreference listPref = (ListPreference) preference;
            preference.setSummary(listPref.getEntry());
        }
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) preference;
            preference.setSummary(editTextPref.getText());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(getString(R.string.pref_font_size))) {
            try {
                int newIntValue = Integer.valueOf(newValue.toString());
                if (newIntValue < 10 || newIntValue > 30) {
                    Toast.makeText(this, R.string.error_limit_font_size, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        if (preference.getKey().equals(getString(R.string.pref_margin))) {
            try {
                int newIntValue = Integer.valueOf(newValue.toString());
                if (newIntValue < 5 || newIntValue > 70) {
                    Toast.makeText(this, R.string.error_limit_margin, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        if (isChanged) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }

        super.finish();
    }
}
