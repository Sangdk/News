package com.t3h.newsproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.newsproject.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private LayoutInflater inflater;
    private List<News> data;
    private ItemClickListener itemClickListener;

    public NewsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<News> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<News> getData() {
        return data;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.item_news, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, final int position) {
        News item = data.get(position);
        holder.bindData(item);

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(position);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.onItemLongClickListener(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        private ImageView imgNews;
        private TextView txtTitle, txtDesc, txtDate;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.image_news);
            txtTitle = itemView.findViewById(R.id.text_title);
            txtDesc = itemView.findViewById(R.id.text_desc);
            txtDate = itemView.findViewById(R.id.text_pub_date);

        }

        public void bindData(News item) {
            txtTitle.setText(item.getTitle());
            txtDesc.setText(item.getDesc());
            txtDate.setText(item.getDate());

            Glide.with(imgNews).load(item.getImg()).into(imgNews);
        }
    }

    public interface ItemClickListener {
        void onItemClickListener(int position);

        void onItemLongClickListener(int position);
    }
}
