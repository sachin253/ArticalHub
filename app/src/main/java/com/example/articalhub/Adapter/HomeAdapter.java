package com.example.articalhub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.articalhub.Model.Post;
import com.example.articalhub.R;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{

    private List<Post> post;
    private Context context = null;

    public HomeAdapter(@Nullable Context context ,@Nullable List<Post> post)
    {
        this.context = context;
        this.post = post;

    }


    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);

        return new HomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder parent, int position)
    {

        final Post u =post.get(position);
        parent.Title.setText(u.getTitle());
        parent.Content.setText(u.getContent());
        Glide.with(context).load(u.getImageURL()).into(parent.Img);

    }

    @Override
    public int getItemCount() {
        return post.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView Title;
        public TextView Content;
        private ImageView Img;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            Title=itemView.findViewById(R.id.ArticleTitleTvRVItem);
            Content=itemView.findViewById(R.id.RVArticleContent);
            Img=itemView.findViewById(R.id.ArticleImageViewId);

        }
    }
}
