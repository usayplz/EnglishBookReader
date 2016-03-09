package com.usayplz.englishbookreader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gregacucnik.EditableSeekBar;
import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.reading.ReadingMenuItem;


/**
 * Created by Sergei Kurikalov on 09/03/16.
 * u.sayplz@gmail.com
 */
public class MenuView extends AlertDialog implements AdapterView.OnItemClickListener {
    private final IMenuView listener;
    private Context context;
    private int page;
    private int maxPage;
    private boolean firstChange = true;


    public MenuView(Context context, int page, int maxPage, IMenuView listener) {
        super(context);
        this.context = context;
        this.maxPage = maxPage;
        this.page = page;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.view_menu, null);
        this.setView(view);
        this.setCancelable(true);
        this.setTitle(null);

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.80);
        this.getWindow().setLayout(width, -2);

        ListView list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(new MenuAdapter());
        list.setOnItemClickListener(this);

        EditableSeekBar pagebar = (EditableSeekBar) view.findViewById(R.id.pagebar);
        pagebar.setValue(page);
        pagebar.setMaxValue(maxPage);
        pagebar.setOnEditableSeekBarChangeListener(new EditableSeekBar.OnEditableSeekBarChangeListener() {
            @Override
            public void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (firstChange) {
                    firstChange = false;
                } else {
                    listener.onPageChanged(progress);
                }
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

            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onMenuItemClicked((int) id);
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
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_list_item, parent, false);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setImageResource(item.icon);
            holder.name.setText(context.getString(item.name));

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