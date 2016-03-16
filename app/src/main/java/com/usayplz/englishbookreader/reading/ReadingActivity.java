package com.usayplz.englishbookreader.reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseActivity;
import com.usayplz.englishbookreader.libraly.LibraryActivity;
import com.usayplz.englishbookreader.preference.UserData;

public class ReadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        // Check book is opened
        long bookId = new UserData(this).getBookId();
        if (bookId > 0) {
            setFragment(new ReadingFragment());
        } else {
            Intent intent = new Intent(this, LibraryActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
