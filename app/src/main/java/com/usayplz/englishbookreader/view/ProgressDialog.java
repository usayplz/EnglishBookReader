package com.usayplz.englishbookreader.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.usayplz.englishbookreader.R;

/**
 * Created by Sergei Kurikalov on 21/02/16.
 * u.sayplz@gmail.com
 */
public class ProgressDialog extends DialogFragment {
    private static final String TAG = ProgressDialog.class.getName();
    private String message;

    public void show(FragmentManager manager, String message) {
        super.show(manager, TAG);
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_progress, null);

        TextView messageView = (TextView) view.findViewById(R.id.message);
        messageView.setText(message);

        return new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setCancelable(false)
                .setView(view)
                .create();
    }
}
