package com.nekkies.wllppr;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nekkies.wllppr.RetrofitModels.Unsplash;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;


public class RecyclerAdapter extends ListAdapter<Unsplash, RecyclerAdapter.Holder> {

    private OnItemClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;
    private Context context;
    public RecyclerAdapter() {
        super(DIFF_CALLBACK);
    }



    public static final  DiffUtil.ItemCallback<Unsplash> DIFF_CALLBACK = new DiffUtil.ItemCallback<Unsplash>() {
        @Override
        public boolean areItemsTheSame(Unsplash oldItem, Unsplash newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Unsplash oldItem, Unsplash newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };

    public Unsplash getItemAt(int position){
        return getItem(position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
        context = parent.getContext();
        return new Holder(v);
    }

    @Override
    public long getItemId(int position) {
        //generate an id
        int id = 0;
        for (char i : getItem(position).getId().toCharArray()){
            id += (int) i;
        }

        return id;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor(getItem(position).getColor()));
        //drawable.setCornerRadius(20);

        GlideApp.with(context)
                .load(getItem(position).getUrls().getSmall())
                .placeholder(drawable)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(holder.photo);



        float AR = (float) getItem(position).getWidth() / getItem(position).getHeight();
        holder.photo.setAspectRatio(AR);

        if(position == getItemCount()-2){
            onBottomReachedListener.onBottomReached(position);
        }

    }



    public class Holder extends RecyclerView.ViewHolder {

        public DynamicHeightNetworkImageView photo;
        public RelativeLayout linearLayout;


        public Holder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo_thumb);
            linearLayout = itemView.findViewById(R.id.container);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null && getAdapterPosition()!= NO_POSITION){
                        try {
                            listener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                        }catch (IndexOutOfBoundsException e){
                            //do nothing about it
                        }
                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Unsplash unsplash, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnBottomReachedListener{
        void onBottomReached(int position);
    }
    public void setOnBottomReachedListener(OnBottomReachedListener listener){
        onBottomReachedListener = listener;
    }
}

