package com.usayplz.englishbookreader.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.model.Chapter;

import java.util.List;

/**
 * Created by Sergei Kurikalov on 16/03/16.
 * u.sayplz@gmail.com
 */
public class ChapterView extends DialogFragment {
    private static final String TAG = ChapterView.class.getName();
    private int currentChapter;
    private IChapterListener listener;
    private List<Chapter> chapters;

    public ChapterView() {
    }

    public void show(FragmentManager manager, int currentChapter, List<Chapter> chapters, IChapterListener listener) {
        super.show(manager, TAG);
        this.currentChapter = currentChapter;
        this.listener = listener;
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setAdapter(new ChapterAdapter(), (dialog1, which) -> {
                })
                .create();

        dialog.getListView().setDivider(new ColorDrawable(Color.GRAY));
        dialog.getListView().setDividerHeight(1);
        dialog.getListView().setOnItemClickListener((parent, view, position, id) -> {
            if (listener != null) {
                listener.onChapterChanged(chapters.get(position));
            }
            dismiss();
        });

        return dialog;
    }

    private class ChapterAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return chapters.size();
        }

        @Override
        public Chapter getItem(int position) {
            return chapters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return chapters.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Chapter item = chapters.get(position);

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.chapter_list_item, parent, false);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(item.getName());

            if (item.getId() == currentChapter) {
                holder.name.setTextColor(getContext().getResources().getColor(R.color.accent));
            } else {
                holder.name.setTextColor(getContext().getResources().getColor(R.color.primary_text));
            }

            return convertView;
        }
    }

    static class ViewHolder {
        TextView name;
    }

    public interface IChapterListener {
        void onChapterChanged(Chapter chapter);
    }
}
