package com.nekkies.wllppr.API;


import com.nekkies.wllppr.RetrofitModels.Collection;
import com.nekkies.wllppr.RetrofitModels.Photo;
import com.nekkies.wllppr.RetrofitModels.Search;
import com.nekkies.wllppr.RetrofitModels.Unsplash;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICallService {

    private final int CURATED = 1;
    private final int NEW = 0;

    private final int SEARCH = 2;
    private final int COLLECTION = 3;

    private String  sort_by;

    public APICallService(String sort_by) {
        this.sort_by = sort_by;
    }

    public void getDetails(String photo_id, final OnGetDetailsListener onGetDetailsListener) {
        ApiEndpoints apiservice = APIService.getInstance();
        Call<Photo> call = apiservice.getPhotoDetails(photo_id, apiservice.CLIENT_ID,sort_by);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    onGetDetailsListener.onSuccess(call, response);
                    return;
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                onGetDetailsListener.onFailure(call, t);
            }
        });
    }

    public void searchPhotos(String search_query, int page, int PER_PAGE,
                             final OnSearchListener onSearchListener) {
        ApiEndpoints apiservice = APIService.getInstance();
        Call<Search> call = apiservice.searchPhotos(apiservice.CLIENT_ID, search_query, page, PER_PAGE,sort_by);
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if (response.isSuccessful()) {
                    onSearchListener.onSuccess(call, response);
                    return;
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                onSearchListener.onFailure(call, t);
            }
        });
    }

    public void getPhotos(int type, int page, int PER_PAGE, final OnGetPhotosListener onGetPhotosListener, Integer collection_id) {

        ApiEndpoints apiservice = APIService.getInstance();
        Call<List<Unsplash>> call;
        if (type == CURATED) {
            call = apiservice.getCuratedPhotos(apiservice.CLIENT_ID, page, PER_PAGE,sort_by);
        } else if(type==NEW){
            call = apiservice.getPhotos(apiservice.CLIENT_ID, page, PER_PAGE,sort_by);
        }else{
            call = apiservice.getCollectionPhotos(collection_id, apiservice.CLIENT_ID, page, PER_PAGE,sort_by);
        }
        call.enqueue(new Callback<List<Unsplash>>() {
            @Override
            public void onResponse(Call<List<Unsplash>> call, Response<List<Unsplash>> response) {
                if (response.isSuccessful()) {
                    onGetPhotosListener.onSuccess(call, response);
                    return;
                }


            }

            @Override
            public void onFailure(Call<List<Unsplash>> call, Throwable t) {
                onGetPhotosListener.onFailure(call, t);
            }
        });
    }

    public void getCollections(int page, int PER_PAGE, final OnGetCollectionsListener onGetCollectionsListener){
        ApiEndpoints apiservice = APIService.getInstance();
        Call<List<Collection>> call = apiservice.getCollections(ApiEndpoints.CLIENT_ID, page,PER_PAGE,sort_by);
        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                if(response.isSuccessful()){
                    onGetCollectionsListener.onSuccess(call,response);
                }
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                onGetCollectionsListener.onFailure(call,t);
            }
        });
    }


    public interface OnGetCollectionsListener {
        void onSuccess(Call<List<Collection>> call, Response<List<Collection>> response);

        void onFailure(Call<List<Collection>> call, Throwable t);
    }

    public interface OnGetDetailsListener {
        void onSuccess(Call<Photo> call, Response<Photo> response);

        void onFailure(Call<Photo> call, Throwable t);
    }

    public interface OnSearchListener {
        void onSuccess(Call<Search> call, Response<Search> response);

        void onFailure(Call<Search> call, Throwable t);
    }

    public interface OnGetPhotosListener {
        void onSuccess(Call<List<Unsplash>> call, Response<List<Unsplash>> response);

        void onFailure(Call<List<Unsplash>> call, Throwable t);
    }


}
