package com.usayplz.englishbookreader;

import android.os.Bundle;

import com.usayplz.englishbookreader.base.BaseActivity;
import com.usayplz.englishbookreader.reading.ReadingFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReadingFragment()).commit();
    }
}
