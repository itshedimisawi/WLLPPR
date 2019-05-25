package com.nekkies.wllppr.API;

import com.nekkies.wllppr.RetrofitModels.Collection;
import com.nekkies.wllppr.RetrofitModels.Photo;
import com.nekkies.wllppr.RetrofitModels.Search;
import com.nekkies.wllppr.RetrofitModels.Unsplash;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpoints {
    public static final String BASE_URL =
            "https://api.unsplash.com";
    public static final String CLIENT_ID =
            "API_KEY_GOES_HERE";


    @GET("photos")
    Call<List<Unsplash>> getPhotos(@Query("client_id") String Client_id,
                                   @Query("page") int page,
                                   @Query("per_page") int per_page,
                                   @Query("order_by") String sort_by);

    @GET("photos/curated")
    Call<List<Unsplash>> getCuratedPhotos(@Query("client_id") String Client_id,
                                          @Query("page") int page,
                                          @Query("per_page") int per_page,
                                          @Query("order_by") String sort_by);

    @GET("photos/{id}")
    Call<Photo> getPhotoDetails(@Path("id") String photo_id,
                                @Query("client_id") String Client_id,
                                @Query("order_by") String sort_by);


    @GET("search/photos")
    Call<Search> searchPhotos(@Query("client_id") String Client_id,
                              @Query("query") String query,
                              @Query("page") int page,
                              @Query("per_page") int per_page,
                              @Query("order_by") String sort_by);

    @GET("collections")
    Call<List<Collection>> getCollections(@Query("client_id") String Client_id,
                                          @Query("page") int page,
                                          @Query("per_page") int per_page,
                                          @Query("order_by") String sort_by);

    @GET("/collections/{id}/photos")
    Call<List<Unsplash>> getCollectionPhotos(@Path("id") int collection_id,
                                             @Query("client_id") String Client_id,
                                             @Query("page") int page,
                                             @Query("per_page") int per_page,
                                             @Query("order_by") String sort_by);

}
