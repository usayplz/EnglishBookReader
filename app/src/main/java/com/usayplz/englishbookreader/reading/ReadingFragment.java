package com.usayplz.englishbookreader.reading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseFragment;
import com.usayplz.englishbookreader.libraly.LibraryActivity;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookSettings;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesActivity;
import com.usayplz.englishbookreader.utils.FileUtils;
import com.usayplz.englishbookreader.view.EBookView;
import com.usayplz.englishbookreader.view.ProgressDialog;

import java.io.File;

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
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // TODO book from savedInstanceState
        Book book = new Book();
//        String filePath = "/mnt/sdcard/Download/johnny.epub";
//        String filePath = "/storage/sdcard/Download/Adamov_G_Izgnanie_VladiykiI.epub";
        String filePath = "/mnt/sdcard/Download/Adamov_G_Izgnanie_VladiykiI.epub";
        File fileBook = new File(filePath);
        File dirBook = FileUtils.concatToFile(getContext().getFilesDir().getPath(), fileBook.getName() + fileBook.length());
        book.setType(BookType.EPUB);
        book.setFile(fileBook.getPath());
        // TODO Define OPS dir
        book.setDir(dirBook.getPath());// + File.separator + "OPS";
        book.setTitle("Johnny Mnemonic");
        book.setAuthor("Unknown");
        book.setChapter(1);
        book.setMaxChapter(58);
        book.setPage(0);
        book.setActive(true);

        // Views
        bookView.setListener(this);
        leftView.setOnClickListener(v -> onPrevious());
        rightView.setOnClickListener(v -> onNext());

        registerForContextMenu(bottomView);
        bottomView.setOnClickListener(v -> getActivity().openContextMenu(v));

        // presenter
        presenter = new ReadingPresenter(book);
        presenter.attachView(this);
        presenter.getContent();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        menu.setHeaderTitle(R.string.menu_title);
        for (ReadingMenuItem item : ReadingMenuItem.values()) {
            menu.add(item.group, item.id, item.order, item.name);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        ReadingMenuItem readingMenuItem = ReadingMenuItem.byId(item.getItemId());
        if (readingMenuItem == null) return false;

        switch (readingMenuItem) {
            case SETTINGS:
                Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                startActivityForResult(intent, PreferencesActivity.SETTINGS_CHANGED_REQUEST);
                return true;
            case LIBRARY:
                startActivity(new Intent(getActivity(), LibraryActivity.class));
                return true;
            case EXIT:
                getActivity().finish();
                System.exit(0);
                return true;
        }

        return false;
    }

    @Override
    public void showContent(BookSettings bookSettings) {
        applySettings(bookSettings.getSettings());
        bookView.loadContent(bookSettings);
    }

    private void applySettings(Settings settings) {
        mainView.setBackgroundColor(settings.getBackgroundColor());
        bookView.setBackgroundColor(settings.getBackgroundColor());

        rightView.getLayoutParams().width = settings.getMarginRight();
        bottomView.getLayoutParams().height = settings.getMarginBottom();
        leftView.getLayoutParams().width = settings.getMarginLeft();
        topView.getLayoutParams().height = settings.getMarginTop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.detachView();
    }

    @Override
    public void setPage(int page) {
        bookView.setPage(page);
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
    public void onGetPageCount(int pagecount) {
        presenter.setPageCount(pagecount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PreferencesActivity.SETTINGS_CHANGED_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.getContent();
        }
    }
}
