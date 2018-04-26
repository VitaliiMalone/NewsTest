package com.example.user.newstest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.newstest.R;
import com.example.user.newstest.model.News;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder> {

    private News news;
    private Context context;

    public RecyclerViewAdapter(Context context, News news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.NewsViewHolder holder, final int position) {

        String sourceName = news.getArticles().get(position).getSource().getName();
        String title = news.getArticles().get(position).getTitle();
        String description = news.getArticles().get(position).getDescription();
        String imageUrl = news.getArticles().get(position).getUrlToImage();
        final String articleUrl = news.getArticles().get(position).getUrl();

        if (sourceName == null || sourceName.isEmpty())
            sourceName = context.getResources().getString(R.string.no_source);
        if (title == null || title.isEmpty())
            title = context.getResources().getString(R.string.no_title);
        if (description == null || description.isEmpty())
            description = context.getResources().getString(R.string.no_description);

        holder.sourceName.setText(sourceName);
        holder.title.setText(title);
        holder.description.setText(description);

        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(articleUrl));
            }
        });

}

    @Override
    public int getItemCount() {
        return news.getTotalResults();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_source_name)
        TextView sourceName;
        @BindView(R.id.news_title)
        TextView title;
        @BindView(R.id.news_description)
        TextView description;
        @BindView(R.id.news_image)
        ImageView image;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
