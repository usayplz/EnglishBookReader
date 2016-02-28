package com.usayplz.englishbookreader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usayplz.englishbookreader.R;

import butterknife.ButterKnife;

/**
 * Created by Sergei Kurikalov on 25/02/16.
 * u.sayplz@gmail.com
 */
public class MenuView extends AlertDialog implements View.OnClickListener {
    private Context context;
    private View.OnClickListener listener;

    public MenuView(Context context, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = factory.inflate(R.layout.view_menu, null);
        this.setView(view);
        this.setCancelable(true);
        this.setTitle(null);
        this.setButton(BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> {
            dialog.cancel();
        });

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, context.getResources().getDisplayMetrics());
        this.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.findById(view, R.id.menu_settings).setOnClickListener(this);
        ButterKnife.findById(view, R.id.menu_library).setOnClickListener(this);
        ButterKnife.findById(view, R.id.menu_nightmode).setOnClickListener(this);
        ButterKnife.findById(view, R.id.menu_exit).setOnClickListener(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
