package com.nekkies.wllppr.Activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nekkies.wllppr.API.APICallService;
import com.nekkies.wllppr.BottomStuff;
import com.nekkies.wllppr.GlideApp;
import com.nekkies.wllppr.ParcelablePhotosArray;
import com.nekkies.wllppr.R;
import com.nekkies.wllppr.RetrofitModels.Photo;
import com.nekkies.wllppr.RetrofitModels.Search;
import com.nekkies.wllppr.RetrofitModels.Unsplash;
import com.nekkies.wllppr.SwipePhotoPagerAdapter;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;


public class PhotoPreview extends AppCompatActivity {
    private int loaded_pages;

    public static final int ACTION_SET_AS_WALLPAPER = 0;
    public static final int ACTION_DOWNLOAD = 1;


    private final int CURATED = 1;
    private final int NEW = 0;
    private final int SEARCH = 2;
    private final int COLLECTION = 3;

    public static final int PER_PAGE = 30;

    int photo_display_initial_position;
    int selected_photo_index;
    int type;

    private String search_query;
    private Integer collection_id;

    private APICallService apiCallService;

    CircleImageView author_picture;
    Toolbar toolbar;
    ViewPager swipe_pager;

    SwipePhotoPagerAdapter adapter;

    private List<String> all_ids;
    private List<String> all_links;
    private List<String> all_username;
    private List<String> all_user_profile_picture;
    private List<String> all_createdAt;
    private List<String> all_user_real_name;
    private List<String> all_user_unsplash_links;
    private Photo detailed_photo;

    private CoordinatorLayout parentlayout;

    private TextView author_username;
    private TextView author_name;
    private TextView creation_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_photo_preview);


        //Get Intent Parcelable photo
        Intent intent = getIntent();
        String sort_by;
        Bundle bundle = intent.getBundleExtra("BUNDLE_WRAPPER");

        ParcelablePhotosArray parcelablePhotosArray = bundle.getParcelable("ALL_PHOTOS");
        all_ids = parcelablePhotosArray.ids;
        all_links = parcelablePhotosArray.link;

        all_username = parcelablePhotosArray.username;
        all_user_profile_picture = parcelablePhotosArray.profile_pic;
        all_createdAt = parcelablePhotosArray.createdAt;
        all_user_real_name = parcelablePhotosArray.real_user_name;
        all_user_unsplash_links = parcelablePhotosArray.user_unsplash_links;

        loaded_pages = bundle.getInt("LOADED_PAGES", 0);
        photo_display_initial_position = bundle.getInt("PHOTO_INDEX", 0);
        type = bundle.getInt("TYPE",0);
        search_query = bundle.getString("SEARCH_QUERY");
        if (type == COLLECTION){
            collection_id = bundle.getInt("COLLECTION_ID",0);
        }
        sort_by = bundle.getString("ORDER_BY");
        selected_photo_index = photo_display_initial_position;

        //Bind Views
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        author_picture = (CircleImageView) findViewById(R.id.user_picture);

         author_username = (TextView) findViewById(R.id.author_username);
         author_name = (TextView) findViewById(R.id.author_name);
         creation_date = (TextView) findViewById(R.id.creation_date);

        swipe_pager = (ViewPager) findViewById(R.id.photo_swipe_viewpager);
        parentlayout = (CoordinatorLayout) findViewById(R.id.photopreview_coordinator);


        //Setup Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        //Setup API
        apiCallService = new APICallService(sort_by);

        //setup ViewPager to swipe images
        adapter = new SwipePhotoPagerAdapter(this, all_ids, all_links);


        swipe_pager.setOffscreenPageLimit(0);
        swipe_pager.setAdapter(adapter);
        swipe_pager.setCurrentItem(photo_display_initial_position);
        initialise_photo(photo_display_initial_position);
        swipe_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selected_photo_index = i;
                if (i + 1 == ((loaded_pages * 30) - 1)) {
                    if (type == SEARCH){
                        loadMoreByQuery(search_query, loaded_pages + 1,PER_PAGE);
                    }else{
                        loadMorePhotos(loaded_pages + 1);
                    }
                }
                initialise_photo(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        author_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_user_unsplash_link();
            }
        });
        author_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_user_unsplash_link();
            }
        });
        author_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_user_unsplash_link();
            }
        });
    }



    private void open_user_unsplash_link(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(all_user_unsplash_links.get(selected_photo_index) +
                "?utm_source=WLLPPR&utm_medium=referral"));
        startActivity(browserIntent);

    }

    private void loadMoreByQuery(String query, int page, int per_page) {
        APICallService.OnSearchListener listener = new APICallService.OnSearchListener() {
            @Override
            public void onSuccess(Call<Search> call, Response<Search> response) {

                addLoadedPhotos(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Snackbar.make(parentlayout, "Unable to get more photos", Snackbar.LENGTH_LONG).show();
            }
        };
        apiCallService.searchPhotos(query, page, per_page, listener);
    }

    private void loadMorePhotos(int page) {
        APICallService.OnGetPhotosListener listener = new APICallService.OnGetPhotosListener() {
            @Override
            public void onSuccess(Call<List<Unsplash>> call, Response<List<Unsplash>> response) {
                addLoadedPhotos(response.body());
            }

            @Override
            public void onFailure(Call<List<Unsplash>> call, Throwable t) {
                Snackbar.make(parentlayout, "Unable to get images", Snackbar.LENGTH_LONG).show();
            }
        };
        apiCallService.getPhotos(type, page, PER_PAGE, listener, collection_id);

    }

    private void addLoadedPhotos(List<Unsplash> photos){
        for (Unsplash photo : photos) {
            all_ids.add(photo.getId());
            all_user_real_name.add(photo.getUser().getFirstName() +
                    " " + photo.getUser().getLastName());
            all_links.add(photo.getUrls().getSmall());
            all_createdAt.add(photo.getCreatedAt());
            all_user_profile_picture.add(photo.getUser().getProfileImage().getMedium());
            all_username.add(photo.getUser().getUsername());

        }
        adapter.setPhotos_links(all_links);
        adapter.notifyDataSetChanged();
        loaded_pages++;
    }

    void initialise_photo(int position) {
        GlideApp.with(PhotoPreview.this)
                .load(all_user_profile_picture.get(position))
                .into(author_picture);




        creation_date.setText(PrettifyDate(all_createdAt.get(position)));
        author_username.setText(all_username.get(position));
        author_name.setText("@" + all_user_real_name.get(position));
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

    @Override
    public void onBackPressed() {
        sendBackPosition();
        finish();
    }

    void sendBackPosition(){
        Intent result_intent = new Intent();
        result_intent.putExtra("PREVIEW_LOADED_PAGES",loaded_pages);
        result_intent.putExtra("PREVIEW_ITEM_POSITION",selected_photo_index);
        setResult(RESULT_OK,result_intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.downAction:
                actionImage(all_ids.get(selected_photo_index),ACTION_DOWNLOAD);
                return true;
            case R.id.infosAction:
                Bundle bundle = new Bundle();
                bundle.putString("INFO_DATE", PrettifyDate(all_createdAt.get(selected_photo_index)));
                bundle.putString("INFO_ID", all_ids.get(selected_photo_index));
                BottomStuff bottomStuff = new BottomStuff();
                bottomStuff.setArguments(bundle);
                bottomStuff.show(getSupportFragmentManager(), "123");
                return true;
            case R.id.wallpaperAction:
                actionImage(all_ids.get(selected_photo_index), ACTION_SET_AS_WALLPAPER);
                return true;
            case R.id.userlinkAction:
                open_user_unsplash_link();
                return true;
            case android.R.id.home:
                sendBackPosition();
                finish();


        }
        return super.onOptionsItemSelected(item);
    }

    void actionImage(String id, final int action) {

        APICallService.OnGetDetailsListener listener = new APICallService.OnGetDetailsListener() {
            @Override
            public void onSuccess(Call<Photo> call, Response<Photo> response) {
                    if (response.body() != null) {
                        detailed_photo = response.body();
                        downloadImage(selected_photo_index,action);
                    }
                }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Snackbar.make(parentlayout, "Unable to get image from the server", Snackbar.LENGTH_SHORT).show();
            }
        };
        apiCallService.getDetails(id,listener);
    }


    void downloadImage(final int position, final int action) {
        File picturesDir;
        File myDir;
        if (action == ACTION_DOWNLOAD) {
            picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            myDir = new File(picturesDir, "WLLPPR");
        } else {
            picturesDir = getExternalCacheDir();
            myDir = new File(picturesDir, "temp");
        }

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        final File file = new File(myDir, "/" +
                all_ids.get(position) + "_" +
                all_username.get(position) +
                ".jpeg");

        Snackbar.make(parentlayout, "Downloading high resolution image..", Snackbar.LENGTH_LONG).show();

        GlideApp.with(this)
                .asBitmap()
                .load(detailed_photo.getUrls().getRegular())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try (
                                FileOutputStream out = new FileOutputStream(file)) {
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            if (action == ACTION_DOWNLOAD) {
                                notifyGallery(file);
                            } else {
                                setAsWallpaper(file);
                            }

                            Snackbar.make(parentlayout, "Done", Snackbar.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Snackbar.make(parentlayout, "Unable to save image", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }


    void notifyGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

    void setAsWallpaper(File file) {
        Uri uri = Uri.fromFile(file);
        Intent xtent = new Intent(Intent.ACTION_ATTACH_DATA);
        xtent.setDataAndType(uri, "image/*");
        xtent.putExtra("mimetype", "image/*");
        xtent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(xtent, "Set as: "));
    }
}
