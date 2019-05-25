package com.nekkies.wllppr;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nekkies.wllppr.RetrofitModels.Collection;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class CollectionsRecyclerAdapter extends ListAdapter<Collection, CollectionsRecyclerAdapter.CollectionHolder> {

    private CollectionsRecyclerAdapter.OnItemClickListener listener;
    private CollectionsRecyclerAdapter.OnBottomReachedListener onBottomReachedListener;
    private Context context;

    public CollectionsRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_single_collection, viewGroup, false);
        context = viewGroup.getContext();
        return new CollectionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionHolder collectionHolder, int i) {
        collectionHolder.title.setText(getItem(i).getTitle());
        collectionHolder.user_name.setText(getItem(i).getUser().getFirstName() + " " +
                getItem(i).getUser().getLastName());
        collectionHolder.username.setText("@" + getItem(i).getUser().getUsername());

        collectionHolder.createdat.setText(PrettifyDate(getItem(i).getPublishedAt()));

        GlideApp.with(context)
                .load(getItem(i).getUser().getProfileImage().getSmall())
                .into(collectionHolder.userpicture);

        if (i == getItemCount() - 1) {
            onBottomReachedListener.onBottomReached(i);
        }
    }

    public static final DiffUtil.ItemCallback<Collection> DIFF_CALLBACK = new DiffUtil.ItemCallback<Collection>() {
        @Override
        public boolean areItemsTheSame(Collection oldItem, Collection newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Collection oldItem, Collection newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };


    public class CollectionHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView user_name;
        public TextView username;
        public TextView createdat;
        public CircleImageView userpicture;
        public RelativeLayout container;

        public CollectionHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.collection_title);
            this.user_name = itemView.findViewById(R.id.collection_user);
            this.username = itemView.findViewById(R.id.collection_username);
            this.createdat = itemView.findViewById(R.id.collection_createdat);
            this.container = itemView.findViewById(R.id.collection_container);
            this.userpicture = itemView.findViewById(R.id.collection_user_picture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != NO_POSITION) {
                        listener.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }

    private String PrettifyDate(String DateString) {

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .parse(DateString);
        } catch (ParseException e) {
            date = new Date();
        }
        PrettyTime p = new PrettyTime();
        return p.format(date);


    }
    public interface OnItemClickListener {
        void onItemClick(Collection collection, int position);
    }

    public void setOnItemClickListener(CollectionsRecyclerAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }

    public void setOnBottomReachedListener(CollectionsRecyclerAdapter.OnBottomReachedListener listener) {
        onBottomReachedListener = listener;
    }
}
