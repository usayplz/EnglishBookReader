package com.usayplz.englishbookreader.libraly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.utils.Strings;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sergei Kurikalov on 02/03/16.
 * u.sayplz@gmail.com
 */
public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.ViewHolder> {
    private List<Book> books;
    private ShelfAdapterListener listener;

    public ShelfAdapter(ShelfAdapterListener listener) {
        this.books = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shelf_list_item, parent, false);
        return new ViewHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = books.get(position);
        String pathCoverImage = book.getCoverImage();
        if (!Strings.isEmpty(pathCoverImage)) {
            try {
                Bitmap bm = BitmapFactory.decodeFile(pathCoverImage);
                holder.cover.setImageBitmap(bm);
            } catch (Exception e) {
                Log.d("Cannot load cover image: " + e.getMessage());
            }
        }
        holder.title.setText(book.getTitle());
        holder.authors.setText(book.getAuthor());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public Book getBook(int position) {
        return books.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cover) ImageView cover;
        @Bind(R.id.title) TextView title;
        @Bind(R.id.authors) TextView authors;

        public ViewHolder(View itemView, ShelfAdapterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShelfClicked(this.getAdapterPosition());
                }
            });
        }
    }

    public interface ShelfAdapterListener {
        void onShelfClicked(int position);
    }
}