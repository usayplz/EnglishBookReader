package com.usayplz.englishbookreader.libraly;

import android.os.Bundle;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseActivity;

/**
 * Created by Sergei Kurikalov on 02/03/16.
 * u.sayplz@gmail.com
 */
public class LibraryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new LibraryFragment()).commit();
    }
}
