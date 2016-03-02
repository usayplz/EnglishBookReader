package com.usayplz.englishbookreader.libraly;

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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public class LibralyFragment extends BaseFragment implements LibralyView {
    @Bind(R.id.shelf) RecyclerView shelfView;

    private LibralyPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Views
        shelfView.setLayoutManager(new LinearLayoutManager(getContext()));
        shelfView.setHasFixedSize(true);
        shelfView.setItemAnimator(new DefaultItemAnimator());
        shelfView.setAdapter(null);

        // presenter
        presenter = new LibralyPresenter();
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
    public void showContent() {

    }
}
