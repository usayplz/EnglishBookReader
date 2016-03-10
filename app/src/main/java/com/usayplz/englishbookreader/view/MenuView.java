package com.usayplz.englishbookreader.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gregacucnik.EditableSeekBar;
import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.reading.ReadingMenuItem;
import com.usayplz.englishbookreader.utils.Log;


/**
 * Created by Sergei Kurikalov on 09/03/16.
 * u.sayplz@gmail.com
 */
public class MenuView extends DialogFragment {
    private static final String TAG = "menu_view";

    private IMenuView listener;
    private int page;
    private int maxPage;

    public MenuView() {
    }

    public void show(FragmentManager manager, int page, int maxPage, IMenuView listener) {
        super.show(manager, TAG);
        this.page = page;
        this.maxPage = maxPage;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_menu, null);

        ListView list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(new MenuAdapter());
        list.setOnItemClickListener((parent, view1, position, id) -> listener.onMenuItemClicked((int) id));

        Log.d("page: " + page + ", maxPage: " + maxPage);
        EditableSeekBar pagebar = (EditableSeekBar) view.findViewById(R.id.pagebar);
        pagebar.setOnEditableSeekBarChangeListener(new EditableSeekBar.OnEditableSeekBarChangeListener() {
            @Override
            public void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onEnteredValueTooHigh() {

            }

            @Override
            public void onEnteredValueTooLow() {

            }

            @Override
            public void onEditableSeekBarValueChanged(int value) {
                listener.onPageChanged(value);
            }
        });
        pagebar.setMaxValue(maxPage);
        pagebar.setValue(page);

        return new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setView(view)
                .create();
    }

    private class MenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return ReadingMenuItem.values().length;
        }

        @Override
        public ReadingMenuItem getItem(int position) {
            return ReadingMenuItem.values()[position];
        }

        @Override
        public long getItemId(int position) {
            return ReadingMenuItem.values()[position].id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReadingMenuItem item = ReadingMenuItem.values()[position];

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_list_item, parent, false);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setImageResource(item.icon);
            holder.name.setText(getContext().getString(item.name));

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
    }

    public interface IMenuView {
        void onPageChanged(int page);
        void onMenuItemClicked(int id);
    }
}