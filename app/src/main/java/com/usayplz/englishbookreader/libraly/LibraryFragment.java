package com.usayplz.englishbookreader.libraly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseFragment;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.reading.ReadingActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public class LibraryFragment extends BaseFragment implements LibraryView, ShelfAdapter.IAdapterListener {
    @Bind(R.id.shelf) RecyclerView shelfView;

    private LibraryPresenter presenter;
    private ShelfAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Views
        adapter = new ShelfAdapter(this);

        shelfView.setLayoutManager(new LinearLayoutManager(getContext()));
        shelfView.setHasFixedSize(true);
        shelfView.setItemAnimator(new DefaultItemAnimator());
        shelfView.setAdapter(adapter);

        // presenter
        presenter = new LibraryPresenter();
        presenter.attachView(this);
        presenter.getContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.detachView();
    }

    @Override
    public void showContent(List<Book> books) {
        adapter.setBooks(books);
    }

    @Override
    public void showBook(long id) {
        Intent intent = new Intent(getActivity(), ReadingActivity.class);
        intent.putExtra(UserData.APP_PREF_BOOK_ID, id);
        startActivity(intent);
    }

    @Override
    public void onBookClicked(int position) {
        presenter.loadBook(adapter.getBook(position));
    }
}
