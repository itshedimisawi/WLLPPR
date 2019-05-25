package com.nekkies.wllppr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nekkies.wllppr.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTheme();
        setContentView(R.layout.activity_intro);

        Button getstartedbtn = (Button) findViewById(R.id.intro_bottomstuff);


        getstartedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIntroViewedSetting();
                finish();
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

    private void saveIntroViewedSetting(){
        SharedPreferences keyValues = this.getSharedPreferences("WLLPPR_SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();
        keyValuesEditor.putBoolean("INTRO_VIEWED", true);
        keyValuesEditor.apply();
    }
}
