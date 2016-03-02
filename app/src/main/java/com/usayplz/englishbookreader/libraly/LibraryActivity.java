package com.usayplz.englishbookreader.libraly;

import android.content.Intent;
import android.os.Bundle;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseActivity;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.reading.ReadingActivity;

/**
 * Created by Sergei Kurikalov on 02/03/16.
 * u.sayplz@gmail.com
 */
public class LibraryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Check book is opened
        long bookId = new UserData(this).getBookId();
        if (bookId > 0) {
            Intent intent = new Intent(this, ReadingActivity.class);
            intent.putExtra(UserData.APP_PREF_BOOK_ID, bookId);
            startActivity(intent);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new LibralyFragment()).commit();
        }
    }
}
