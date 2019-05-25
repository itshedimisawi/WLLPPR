package com.nekkies.wllppr;

import android.os.Parcel;
import android.os.Parcelable;

import com.nekkies.wllppr.RetrofitModels.Unsplash;

import java.util.ArrayList;
import java.util.List;

public class ParcelablePhotosArray  implements Parcelable {
    public List<String> ids = new ArrayList<>();
    public List<String> link = new ArrayList<>();
    public List<String> username = new ArrayList<>();
    public List<String> profile_pic = new ArrayList<>();
    public List<String> createdAt = new ArrayList<>();
    public List<String> real_user_name = new ArrayList<>();
    public List<String> user_unsplash_links = new ArrayList<>();

    public ParcelablePhotosArray(){

    }

    protected ParcelablePhotosArray(Parcel in) {
        ids = in.createStringArrayList();
        link = in.createStringArrayList();
        username = in.createStringArrayList();
        profile_pic = in.createStringArrayList();
        createdAt = in.createStringArrayList();
        real_user_name = in.createStringArrayList();
        user_unsplash_links = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(ids);
        dest.writeStringList(link);
        dest.writeStringList(username);
        dest.writeStringList(profile_pic);
        dest.writeStringList(createdAt);
        dest.writeStringList(real_user_name);
        dest.writeStringList(user_unsplash_links);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelablePhotosArray> CREATOR = new Creator<ParcelablePhotosArray>() {
        @Override
        public ParcelablePhotosArray createFromParcel(Parcel in) {
            return new ParcelablePhotosArray(in);
        }

        @Override
        public ParcelablePhotosArray[] newArray(int size) {
            return new ParcelablePhotosArray[size];
        }
    };
}
