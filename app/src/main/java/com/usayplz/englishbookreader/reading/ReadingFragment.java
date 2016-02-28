package com.usayplz.englishbookreader.reading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseFragment;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookSettings;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesActivity;
import com.usayplz.englishbookreader.view.EBookView;
import com.usayplz.englishbookreader.view.MenuView;
import com.usayplz.englishbookreader.view.ProgressDialog;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */

public class ReadingFragment extends BaseFragment implements ReadingView, EBookView.EBookListener {
    @Bind(R.id.book) EBookView bookView;
    @Bind(R.id.left) View leftView;
    @Bind(R.id.right) View rightView;
    @Bind(R.id.top) View topView;
    @Bind(R.id.bottom) ImageView bottomView;
    @Bind(R.id.main) RelativeLayout mainView;


    private ReadingPresenter presenter;
    private AlertDialog progressDialog;
    private MenuView menuView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // TODO book from savedInstanceState
        Book book = new Book(getContext());

        // presenter
        presenter = new ReadingPresenter(book);
        presenter.attachView(this);
        presenter.getContent();

        setUpViews();
    }

    private void setUpViews() {
        // Views
        bookView.setListener(this);
        leftView.setOnClickListener(v -> onPrevious());
        rightView.setOnClickListener(v -> onNext());
        bottomView.setOnClickListener(v -> showMenu());

        // Menu™
        menuView = new MenuView(getActivity(), v -> {
            menuView.cancel();

            switch (v.getId()) {
                case R.id.menu_settings:
                    Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                    startActivityForResult(intent, PreferencesActivity.SETTINGS_CHANGED_REQUEST);
                    break;
                case R.id.menu_library:
                    break;
                case R.id.menu_nightmode:
                    break;
                case R.id.menu_exit:
                    getActivity().finish();
                    System.exit(0);
                    break;
            }
        });
    }

    private void showMenu() {
        menuView.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.detachView();
    }

    @Override
    public void showContent(BookSettings bookSettings) {
        applySettings(bookSettings.getSettings());
        bookView.loadContent(bookSettings);
    }

    private void applySettings(Settings settings) {
        mainView.setBackgroundColor(settings.getBackgroundColor());

        leftView.getLayoutParams().width = settings.getMarginLeft();
        rightView.getLayoutParams().width = settings.getMarginRight();
        topView.getLayoutParams().height = settings.getMarginTop();
        bottomView.getLayoutParams().height= settings.getMarginBottom();
    }

    @Override
    public void showError(int error) {
        Toast.makeText(getContext(), R.string.error_open_book, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading(int message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity(), getString(message));
        }
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    public void setPage(int page) {
        bookView.setPage(page);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void onTextSelected(String word) {
        Toast.makeText(getContext(), word, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNext() {
        presenter.nextPage();
    }

    @Override
    public void onPrevious() {
        presenter.previousPage();
    }

    @Override
    public void onGetPageCounts(int pagecount) {
        presenter.setPageCounts(pagecount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PreferencesActivity.SETTINGS_CHANGED_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.getContent();
        }
    }
}
