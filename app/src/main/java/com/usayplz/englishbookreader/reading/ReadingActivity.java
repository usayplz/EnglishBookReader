package com.usayplz.englishbookreader.reading;

import android.os.Bundle;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseActivity;

public class ReadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReadingFragment()).commit();
    }
}
