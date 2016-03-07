package com.usayplz.englishbookreader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.usayplz.englishbookreader.R;

/**
 * Created by Sergei Kurikalov on 21/02/16.
 * u.sayplz@gmail.com
 */
public class ProgressDialog extends AlertDialog {

    private Context context;
    private String message;

    public ProgressDialog(Context context, String message) {
        super(context);
        this.context = context;
        this.message = message;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.view_progress, null);
        this.setView(view);
        this.setCancelable(false);
        this.setTitle(null);
        TextView messageView = (TextView) view.findViewById(R.id.message);
        messageView.setText(message);

        super.onCreate(savedInstanceState);
    }
}
