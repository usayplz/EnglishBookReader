package com.usayplz.englishbookreader.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.usayplz.englishbookreader.view.ProgressDialog;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public class BaseFragment extends Fragment implements BaseView {
    private ProgressDialog progressDialog;

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showError(int error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading(int message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog();
        }

        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }

        progressDialog.show(getActivity().getSupportFragmentManager(), getString(message));
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
