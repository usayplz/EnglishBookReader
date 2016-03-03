package com.usayplz.englishbookreader.reading;

import android.content.Intent;
import android.os.Bundle;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseActivity;
import com.usayplz.englishbookreader.libraly.LibraryActivity;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.utils.Log;

public class ReadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        // Check book is opened
        long bookId = new UserData(this).getBookId();
        Log.d("activity0: " + bookId);
        if (bookId > 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReadingFragment()).commit();
        } else {
            Intent intent = new Intent(this, LibraryActivity.class);
            intent.putExtra(UserData.APP_PREF_BOOK_ID, bookId);
            startActivity(intent);
            finish();
        }
    }
}
