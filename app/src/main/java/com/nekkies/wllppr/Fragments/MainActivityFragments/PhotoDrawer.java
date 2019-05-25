package com.nekkies.wllppr.Fragments.MainActivityFragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nekkies.wllppr.API.APICallService;
import com.nekkies.wllppr.API.APIService;
import com.nekkies.wllppr.API.ApiEndpoints;
import com.nekkies.wllppr.Activities.PhotoPreview;
import com.nekkies.wllppr.ParcelablePhotosArray;
import com.nekkies.wllppr.R;
import com.nekkies.wllppr.RecyclerAdapter;
import com.nekkies.wllppr.RetrofitModels.Search;
import com.nekkies.wllppr.RetrofitModels.Unsplash;
import com.nekkies.wllppr.Utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class PhotoDrawer extends Fragment {

    private static int drawer_style = 0;

    public static final int DRAWER_STYLE_STAGGERED = 0;
    public static final int DRAWER_STYLE_LINEAR = 1;
    public static final int PER_PAGE = 30;
    public static final int DONT_SCROLL = -1;

    public RecyclerView recyclerView;

    private RecyclerAdapter adapter;
    private ApiEndpoints apiservice;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView msg_txtview;
    private ProgressBar progressBar;

    private int loaded_pages = 0;
    private String search_query;
    private Integer collection_id = null;
    private String collection_title;

    private final int CURATED = 1;
    private final int NEW = 0;
    private final int SEARCH = 2;
    private final int COLLECTION = 3;

    private int type;
    private List<Unsplash> photos = new ArrayList<>();

    private String sort_by = "latest";

    private APICallService apiCallService;

    private CoordinatorLayout parentlayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_photos, container, false);
        //Bind Views
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.RelativeBoi);
        parentlayout = (CoordinatorLayout) view.findViewById(R.id.drawer_coordinator);

        progressBar = (ProgressBar) view.findViewById(R.id.photos_progressbar);
        msg_txtview = (TextView) view.findViewById(R.id.photos_msg);

        //Get data type new or curated
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sort_by = bundle.getString("ORDER_BY");
            type = bundle.getInt("TYPE");
            if (type == SEARCH) {
                search_query = bundle.getString("SEARCH_QUERY");
            } else if (type == COLLECTION) {
                collection_id = bundle.getInt("COLLECTION_ID", 0);
                collection_title = bundle.getString("COLLECTION_TITLE");

            }
        }


        //Setup RecyclerView
        setUpRecyclerView();
        //Setup API
        apiservice = APIService.getInstance();
        apiCallService = new APICallService(sort_by);

        //Get Photos

        loadstuff(1, DONT_SCROLL, collection_id);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.getRecycledViewPool().clear();
                adapter.notifyDataSetChanged();
                photos.clear();
                loadstuff(1, DONT_SCROLL, collection_id);
                loaded_pages = 0;
            }
        });


        return view;
    }

    private void loadstuff(int page, int scroll, Integer collection_id) {
        if (type == SEARCH) {
            searchByQuery(search_query, page, PER_PAGE, scroll);
        } else {
            loadPhotos(page, scroll, collection_id);
        }
    }

    private void searchByQuery(final String query, int page, int per_page, final int scroll) {
        APICallService.OnSearchListener listener = new APICallService.OnSearchListener() {
            @Override
            public void onSuccess(Call<Search> call, Response<Search> response) {

                progressBar.setVisibility(View.GONE);
                msg_txtview.setVisibility(View.GONE);

                if (response.body() != null) {
                    if (!response.body().getResults().isEmpty()) {

                        int insert_position = photos.size() + 1;
                        photos.addAll(response.body().getResults());

                        adapter.submitList(photos);
                        loaded_pages++;
                        adapter.notifyItemInserted(insert_position);
                    }
                    else
                    {

                        msg_txtview.setVisibility(View.VISIBLE);
                        msg_txtview.setText("No results found for '"+
                                query.substring(0,Math.min(query.length(),15))
                                +"..'");
                    }
                }
                swipeRefreshLayout.setRefreshing(false);

                if (scroll != DONT_SCROLL) {
                    recyclerView.smoothScrollToPosition(scroll);
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

                msg_txtview.setText("Can't get photosPlease check your network connections");
                progressBar.setVisibility(View.GONE);
                msg_txtview.setVisibility(View.VISIBLE);

                swipeRefreshLayout.setRefreshing(false);
            }
        };
        apiCallService.searchPhotos(query, page, per_page, listener);
    }

    private void loadPhotos(final int page, final int scroll, Integer collection_id) {
        APICallService.OnGetPhotosListener listener = new APICallService.OnGetPhotosListener() {
            @Override
            public void onSuccess(Call<List<Unsplash>> call, Response<List<Unsplash>> response) {

                progressBar.setVisibility(View.GONE);
                msg_txtview.setVisibility(View.GONE);
                int insert_position = photos.size() + 1;
                photos.addAll(response.body());

                adapter.submitList(photos);
                loaded_pages++;
                //adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(insert_position);
                swipeRefreshLayout.setRefreshing(false);

                if (scroll != DONT_SCROLL) {
                    recyclerView.smoothScrollToPosition(scroll);
                }
            }

            @Override
            public void onFailure(Call<List<Unsplash>> call, Throwable t) {

                msg_txtview.setText("Can't get photos\nPlease check your network connections");
                progressBar.setVisibility(View.GONE);
                msg_txtview.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        apiCallService.getPhotos(type, page, PER_PAGE, listener, collection_id);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 6969:
                if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                    int loaded_preview = data.getExtras().getInt("PREVIEW_LOADED_PAGES", 0);
                    int item_position_preview = data.getExtras().getInt("PREVIEW_ITEM_POSITION", DONT_SCROLL);

                    if (loaded_preview > loaded_pages) {
                        for (int i = loaded_pages + 1; i < loaded_preview; i++) {
                            loadstuff(i, (loaded_pages * 30) - 1, collection_id); //dont scroll rv as long we didn't reach preview loading level
                        }
                        loadstuff(loaded_preview, item_position_preview, collection_id);
                    } else {
                        recyclerView.smoothScrollToPosition(item_position_preview);
                    }
                }
                return;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private void setUpRecyclerView() {
        adapter = new RecyclerAdapter();
        adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        if (drawer_style == DRAWER_STYLE_LINEAR) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        }
        adapter.setOnBottomReachedListener(new RecyclerAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                loadstuff(loaded_pages + 1, DONT_SCROLL, collection_id);
            }
        });

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Unsplash unsplash, int position) {

                Intent photo_preview_intent = new Intent(getActivity(), PhotoPreview.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("ALL_PHOTOS",
                        CreateMinimalPhotosArray());
                bundle.putInt("PHOTO_INDEX", position);
                bundle.putInt("LOADED_PAGES", loaded_pages);
                bundle.putInt("TYPE", type);
                if (type == SEARCH) {
                    bundle.putString("SEARCH_QUERY", search_query);
                }
                bundle.putString("ORDER_BY", sort_by);
                photo_preview_intent.putExtra("BUNDLE_WRAPPER", bundle);

                startActivityForResult(photo_preview_intent, 6969);
            }
        });


    }

    //used to avoid TransactionTooLargeException when passing photos array to an activity
    private ParcelablePhotosArray CreateMinimalPhotosArray() {
        ParcelablePhotosArray parcelablePhotosArray = new ParcelablePhotosArray();

        for (Unsplash unsplash : photos) {
            parcelablePhotosArray.ids.add(unsplash.getId());
            parcelablePhotosArray.link.add(unsplash.getUrls().getSmall());
            parcelablePhotosArray.profile_pic.add(unsplash.getUser().getProfileImage().getMedium());

            parcelablePhotosArray.username.add(unsplash.getUser().getUsername());
            parcelablePhotosArray.real_user_name.add(unsplash.getUser().getFirstName() +
                    " " + unsplash.getUser().getLastName());
            parcelablePhotosArray.createdAt.add(unsplash.getCreatedAt());
            parcelablePhotosArray.user_unsplash_links.add(unsplash.getUser().getLinks().getHtml());

        }
        return parcelablePhotosArray;
    }


}
