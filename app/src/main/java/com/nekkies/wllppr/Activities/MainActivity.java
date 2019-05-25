package com.nekkies.wllppr.Activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.nekkies.wllppr.Fragments.MainActivityFragments.Collections;
import com.nekkies.wllppr.Fragments.MainActivityFragments.PhotoDrawer;
import com.nekkies.wllppr.R;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final int CURATED = 1;
    private final int NEW = 0;
    private final int SEARCH = 2;

    public static final String FRAGMENT_TAG_COLLECTION = "SINGLE_COLLECTION";
    public static final String FRAGMENT_TAG_COLLECTION_PHOTOS = "COLLECTIONS";
    public static final String FRAGMENT_TAG_PHOTOS = "TAG_PHOTOS";
    public static final String FRAGMENT_TAG_SEARCH = "SEARCH_PHOTOS";


    private int sort = 0; //0 by default cuz default order is 'latest'

    private MaterialSearchBar materialSearchBar;
    private List<String> sortOptions = new ArrayList<>();  // here is list

    private BottomNavigationView navigationView;
    private PhotoDrawer photoDrawer;
    private Bundle photoDrawerArgs;

    //private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTheme();
        startIntro();
        setContentView(R.layout.activity_main);


        sortOptions.add("Latest");
        sortOptions.add("Oldest");
        sortOptions.add("Popular");

        setupBottomNavigationView();
        setupupMaterialSearchBar();


        startDrawer();


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2121:
                recreate();
                loadSearchSuggestionsFromDisk();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setActivityTheme() {
        SharedPreferences keyValues = this.getSharedPreferences("WLLPPR_SETTINGS", Context.MODE_PRIVATE);
        if (!keyValues.contains("WLLPPR_THEME")) {
            setTheme(R.style.AppAlternativeTheme);
        } else {
            if (keyValues.getBoolean("WLLPPR_THEME", false)) {
                setTheme(R.style.AppAlternativeTheme);
            } else {
                setTheme(R.style.AppTheme);
            }
        }
    }

    private void startSettings() {
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivityForResult(intent, 2121);
    }

    private void startIntro() {

        SharedPreferences keyValues = this.getSharedPreferences("WLLPPR_SETTINGS", Context.MODE_PRIVATE);

        if ((keyValues.contains("INTRO_VIEWED") && !keyValues.getBoolean("INTRO_VIEWED", false) ||
                (!keyValues.contains("INTRO_VIEWED")))) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }

    private void supportdev() {
    }

    private void startDrawer() {
        photoDrawer = new PhotoDrawer();
        photoDrawerArgs = new Bundle();
        photoDrawerArgs.putInt("TYPE", NEW);
        photoDrawer.setArguments(photoDrawerArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, photoDrawer, FRAGMENT_TAG_PHOTOS).commit();
    }

    private void setupupMaterialSearchBar() {

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.material_searchbar);
        materialSearchBar.setCardViewElevation(0);
        materialSearchBar.setPlaceHolder("New photos");
        materialSearchBar.setNavButtonEnabled(false);
        materialSearchBar.setMaxSuggestionCount(3);
        materialSearchBar.inflateMenu(R.menu.main_menu);
        materialSearchBar.setTextHighlightColor(getResources().getColor(R.color.colorAccent));


        loadSearchSuggestionsFromDisk();


        materialSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_sort:
                        showRadioButtonDialog();


                }
                return false;
            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (enabled) {
                    navigationView.setVisibility(View.GONE);
                } else {
                    navigationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                confirmSearch(text);
                saveSearchDataToDisk(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_BACK:
                        materialSearchBar.disableSearch();
                        return;
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        //drawer.openDrawer();
                }
            }

        });


        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                confirmSearch(materialSearchBar.getLastSuggestions().get(position).toString());
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {
                List suggestions = materialSearchBar.getLastSuggestions();
                suggestions.remove(position);
                materialSearchBar.updateLastSuggestions(suggestions);
            }
        });
    }

    private void confirmSearch(CharSequence text) {
        photoDrawer = new PhotoDrawer();
        photoDrawerArgs = new Bundle();
        photoDrawerArgs.putInt("TYPE", SEARCH);
        photoDrawerArgs.putString("SEARCH_QUERY", text.toString());
        photoDrawerArgs.putString("ORDER_BY", sortOptions.get(sort));
        photoDrawer.setArguments(photoDrawerArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, photoDrawer, FRAGMENT_TAG_SEARCH).commit();
        materialSearchBar.disableSearch();
        materialSearchBar.setText(text.toString());
        materialSearchBar.setPlaceHolder(text);
    }

    private void setupBottomNavigationView() {

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                sort = 0;
                switch (menuItem.getItemId()) {
                    case R.id.navFeed:
                        navfeed();
                        return true;
                    case R.id.navTrending:
                        navtrending();
                        return true;
                    case R.id.navCollections:
                        navcollection();
                        return true;
                    case R.id.navSettings:
                        startSettings();
                        return false;
                    /*case R.id.navSupport:
                        supportdev();*/
                    default:
                        return false;
                }
            }
        });
    }


    private void navcollection() {

        Collections collections = new Collections();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, collections, FRAGMENT_TAG_COLLECTION).commit();
        materialSearchBar.setPlaceHolder("Collections");
    }

    private void navfeed() {
        photoDrawer = new PhotoDrawer();
        photoDrawerArgs = new Bundle();

        photoDrawerArgs.putInt("TYPE", NEW);
        photoDrawerArgs.putString("ORDER_BY", sortOptions.get(sort));

        photoDrawer.setArguments(photoDrawerArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, photoDrawer, FRAGMENT_TAG_PHOTOS).commit();

        materialSearchBar.setPlaceHolder("New photos");
    }

    private void navtrending() {
        photoDrawer = new PhotoDrawer();
        photoDrawerArgs = new Bundle();

        photoDrawerArgs.putInt("TYPE", CURATED);
        photoDrawerArgs.putString("ORDER_BY", sortOptions.get(sort));

        photoDrawer.setArguments(photoDrawerArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, photoDrawer, FRAGMENT_TAG_PHOTOS).commit();

        materialSearchBar.setPlaceHolder("Trending");

    }

    private void sortCurrentFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.main_container).getTag() == FRAGMENT_TAG_PHOTOS) {
            photoDrawer = new PhotoDrawer();
            photoDrawerArgs.putString("ORDER_BY", sortOptions.get(sort));
            photoDrawer.setArguments(photoDrawerArgs);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, photoDrawer,
                    getSupportFragmentManager().findFragmentById(R.id.main_container).getTag())
                    .detach(photoDrawer).attach(photoDrawer).commit();
        }

    }

    private void showRadioButtonDialog() {


        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sort_menu_dialog);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btn_setSort = (Button) dialog.findViewById(R.id.button_setsort);
        btn_setSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = rg.indexOfChild((RadioButton) dialog.findViewById(rg.getCheckedRadioButtonId()));
                sortCurrentFragment();
                dialog.dismiss();
            }
        });

        for (int i = 0; i < sortOptions.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(sortOptions.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {

                    }
                }
            }
        });
        rg.check(rg.getChildAt(sort).getId());
        dialog.show();

    }

    private void saveSearchDataToDisk(String append_query) {
        List<String> suggestions = materialSearchBar.getLastSuggestions();
        suggestions.add(0,append_query);
        Map<String, String> suggestionsHashMap = new HashMap<String, String>();

        int i = 0;
        for (String suggession : suggestions) {
            suggestionsHashMap.put("SUGGESTION_" + String.valueOf(i), suggession);  //exp: <SUGGESTION_7,cars>
            i++;
        }

        SharedPreferences keyValues = this.getSharedPreferences("SUGGESTION_LIST", Context.MODE_PRIVATE);
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();
        keyValuesEditor.clear(); //Remove old values before putting new ones

        for (String s : suggestionsHashMap.keySet()) {
            // use the name as the key, and the icon as the value
            keyValuesEditor.putString(s, suggestionsHashMap.get(s));
        }

        keyValuesEditor.apply();
    }

    private void loadSearchSuggestionsFromDisk() {
        materialSearchBar.clearSuggestions();
        List<String> searchSuggestions = new ArrayList<>();

        SharedPreferences keyValues = this.getSharedPreferences("SUGGESTION_LIST", Context.MODE_PRIVATE);
        int i = 0;
        while (keyValues.contains("SUGGESTION_" + String.valueOf(i))) {
            searchSuggestions.add(keyValues.getString("SUGGESTION_" + String.valueOf(i), "fuck"));
            i++;
        }
        if (!searchSuggestions.isEmpty()) {
            materialSearchBar.setLastSuggestions(searchSuggestions);
        }else{
        }
    }

    @Override
    protected void onDestroy() {

        File picturesDir = getExternalCacheDir();
        File myDir = new File(picturesDir, "temp");
        //deleteDir(myDir);
        super.onDestroy();
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
