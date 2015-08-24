package com.pierce.importnew.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pierce.importnew.R;
import com.pierce.importnew.models.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Author: pierce
 * Date: 2015/8/20
 */
public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.ViewHolder> {
    private final String TAG="recyclerviewadapter";
    private List<Post> data;
    private int position;
    private Context mContext;
    private int mItemLayout;
    private OnRecyclerViewItemClickedListener listener;


    public interface OnRecyclerViewItemClickedListener{
        public void onRecyclerViewItemClicked(int position,Object o);
    }
    public void setOnItemClickedListener(OnRecyclerViewItemClickedListener listener){
        this.listener=listener;
    }
    public PostListRecyclerViewAdapter(Context context,int layout,List data){
        this.data=data;
        this.mContext=context;
        this.mItemLayout=layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(mItemLayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        this.position=position;
        Post post=data.get(position);
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());
        holder.author.setText(post.getAuthor());
        holder.date.setText(post.getCreate_at());
        Glide.with(mContext)
                .load(post.getCover())
                .into(holder.img);
        //Log.i(TAG,post.getTitle().isEmpty()?TAG:post.getTitle());
        holder.item.setTag(data.get(position));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position, v.getTag());
            }
        });

    }
    public void myNotify(List data){
        this.data=data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.item_view)
        public CardView item;
        @Bind(R.id.item_post_title)
        public TextView title;
        @Bind(R.id.item_post_description)
        public TextView description;
        @Bind(R.id.item_post_user_name)
        public TextView author;
        @Bind(R.id.item_post_date)
        public TextView date;
        @Bind(R.id.item_post_img)
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
