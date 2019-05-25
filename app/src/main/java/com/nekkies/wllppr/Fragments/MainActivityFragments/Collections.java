package com.nekkies.wllppr.Fragments.MainActivityFragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nekkies.wllppr.API.APICallService;
import com.nekkies.wllppr.CollectionsRecyclerAdapter;
import com.nekkies.wllppr.R;
import com.nekkies.wllppr.RetrofitModels.Collection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Collections extends Fragment {


    public static final String FRAGMENT_TAG_COLLECTION_PHOTOS = "COLLECTIONS";

    private final int COLLECTION = 3;
    private PhotoDrawer collection_photoDrawer;

    private APICallService apiCallService;
    RecyclerView recyclerView;
    private TextView msg_txtview;
    private ProgressBar progressBar;
    CollectionsRecyclerAdapter adapter;
    int loaded_pages = 0;
    private List<Collection> collections = new ArrayList<>();

    private  APICallService.OnGetCollectionsListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_collections, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.RelativeCollections);

        progressBar = (ProgressBar) v.findViewById(R.id.collections_progressbar);
        msg_txtview = (TextView) v.findViewById(R.id.collections_msg);
        setUpRecyclerView();


        apiCallService = new APICallService(null);
        listener = new APICallService.OnGetCollectionsListener() {
            @Override
            public void onSuccess(Call<List<Collection>> call, Response<List<Collection>> response) {
                progressBar.setVisibility(View.GONE);
                msg_txtview.setVisibility(View.GONE);

                collections.addAll(response.body());
                adapter.submitList(collections);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                msg_txtview.setText("Can't get collections\nPlease check your network connections");
                progressBar.setVisibility(View.GONE);
                msg_txtview.setVisibility(View.VISIBLE);
            }
        };
        apiCallService.getCollections(1,30,listener);
        loaded_pages++;


        return v;
    }




    private void setUpRecyclerView() {
        adapter = new CollectionsRecyclerAdapter();
        adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnBottomReachedListener(new CollectionsRecyclerAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                loaded_pages++;
                apiCallService.getCollections(loaded_pages,10,listener);
            }
        });

        adapter.setOnItemClickListener(new CollectionsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Collection collection, int position) {
                collection_photoDrawer = new PhotoDrawer();
                Bundle collection_args = new Bundle();
                collection_args.putInt("TYPE", COLLECTION);
                collection_args.putInt("COLLECTION_ID", collection.getId());
                collection_args.putString("COLLECTION_TITLE", collection.getTitle());
                collection_photoDrawer.setArguments(collection_args);
                getFragmentManager().
                        beginTransaction()
                        .add(R.id.main_container, collection_photoDrawer,FRAGMENT_TAG_COLLECTION_PHOTOS)
                        .addToBackStack(null)
                        .commit();
            }
        });


    }
}
