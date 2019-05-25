package com.nekkies.wllppr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.nekkies.wllppr.BottomStuff;
import com.nekkies.wllppr.R;


public class UserSettingsActivity extends AppCompatActivity {
    private SharedPreferences keyValues;
    private CoordinatorLayout containerLayout;
    private boolean darkTheme;

    private boolean settingsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTheme();
        setContentView(R.layout.activity_user_settings);


        setSupportActionBar((Toolbar) findViewById(R.id.SettingsToolbar));
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        keyValues = this.getSharedPreferences("WLLPPR_SETTINGS", Context.MODE_PRIVATE);
        readSettings();
        containerLayout = (CoordinatorLayout) findViewById(R.id.settings_coordinator);
        SwitchMaterial themeSwitchBtn = (SwitchMaterial) findViewById(R.id.settings_darktheme);
        LinearLayout ClearSearchesBtn = (LinearLayout) findViewById(R.id.settings_clear_suggestions);

        themeSwitchBtn.setChecked(darkTheme);

        themeSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchOnDarkTheme(isChecked);
            }
        });

        ClearSearchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearches();
            }
        });
    }
    private void setActivityTheme(){
        SharedPreferences keyValues = this.getSharedPreferences("WLLPPR_SETTINGS", Context.MODE_PRIVATE);
        if (!keyValues.contains("WLLPPR_THEME")) {
            setTheme(R.style.AppAlternativeTheme);
        } else {
            if(keyValues.getBoolean("WLLPPR_THEME", false)){
                setTheme(R.style.AppAlternativeTheme);
            }else{
                setTheme(R.style.AppTheme);
            }
        }
    }

    private void readSettings() {

        if (!keyValues.contains("WLLPPR_THEME")) {
            darkTheme = true;
        } else {
            darkTheme = keyValues.getBoolean("WLLPPR_THEME", false);
        }
    }

    private void switchOnDarkTheme(boolean setDark) {

        SharedPreferences.Editor keyValuesEditor = keyValues.edit();
        keyValuesEditor.putBoolean("WLLPPR_THEME", setDark);
        keyValuesEditor.apply();

        recreate();

    }

    private void clearSearches() {

        SharedPreferences suggestions_keyValues = this.getSharedPreferences("SUGGESTION_LIST", Context.MODE_PRIVATE);
        SharedPreferences.Editor keyValuesEditor = suggestions_keyValues.edit();

        keyValuesEditor.clear();
        keyValuesEditor.apply();

        Snackbar.make(containerLayout, "Search suggestions cleared", Snackbar.LENGTH_SHORT).show();
    }
    void applySettings(){
        Intent result_intent = new Intent();
        //result_intent.putExtra("SETTINGS_RECREATE", settingsChanged);
        setResult(RESULT_OK, result_intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        applySettings();
        finish();
    }
}
