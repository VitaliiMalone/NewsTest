package com.example.user.newstest;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder> {

    private List<News> news;

    public RecyclerViewAdapter(List<News> news) {
        this.news = news;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.NewsViewHolder holder, int position) {
        holder.sourceName.setText(news.get(position).getSourceName());
        holder.title.setText(news.get(position).getTitle());
        holder.description.setText(news.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView sourceName;
        TextView title;
        TextView description;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            sourceName = itemView.findViewById(R.id.news_source_name);
            title = itemView.findViewById(R.id.news_title);
            description = itemView.findViewById(R.id.news_description);
        }
    }

    public void clear() {
        news.clear();
    }

    public void addAll(List<News> news) {
        this.news.addAll(news);
    }

}
