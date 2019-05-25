package com.nekkies.wllppr.RetrofitModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("urls")
    @Expose
    private Urls urls;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("categories")
    @Expose
    private List<Object> categories = new ArrayList<Object>();
    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;
    @SerializedName("sponsored_by")
    @Expose
    private Object sponsoredBy;
    @SerializedName("sponsored_impressions_id")
    @Expose
    private Object sponsoredImpressionsId;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("liked_by_user")
    @Expose
    private Boolean likedByUser;
    @SerializedName("current_user_collections")
    @Expose
    private List<Object> currentUserCollections = new ArrayList<Object>();
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("exif")
    @Expose
    private Exif exif;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("downloads")
    @Expose
    private Integer downloads;

    protected Photo(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            width = null;
        } else {
            width = in.readInt();
        }
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readInt();
        }
        color = in.readString();
        description = in.readString();
        urls = in.readParcelable(Urls.class.getClassLoader());
        links = in.readParcelable(Links.class.getClassLoader());
        byte tmpSponsored = in.readByte();
        sponsored = tmpSponsored == 0 ? null : tmpSponsored == 1;
        if (in.readByte() == 0) {
            likes = null;
        } else {
            likes = in.readInt();
        }
        byte tmpLikedByUser = in.readByte();
        likedByUser = tmpLikedByUser == 0 ? null : tmpLikedByUser == 1;
        slug = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        exif = in.readParcelable(Exif.class.getClassLoader());
        if (in.readByte() == 0) {
            views = null;
        } else {
            views = in.readInt();
        }
        if (in.readByte() == 0) {
            downloads = null;
        } else {
            downloads = in.readInt();
        }
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public Object getSponsoredBy() {
        return sponsoredBy;
    }

    public void setSponsoredBy(Object sponsoredBy) {
        this.sponsoredBy = sponsoredBy;
    }

    public Object getSponsoredImpressionsId() {
        return sponsoredImpressionsId;
    }

    public void setSponsoredImpressionsId(Object sponsoredImpressionsId) {
        this.sponsoredImpressionsId = sponsoredImpressionsId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public List<Object> getCurrentUserCollections() {
        return currentUserCollections;
    }

    public void setCurrentUserCollections(List<Object> currentUserCollections) {
        this.currentUserCollections = currentUserCollections;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exif getExif() {
        return exif;
    }

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        if (width == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(width);
        }
        if (height == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(height);
        }
        parcel.writeString(color);
        parcel.writeString(description);
        parcel.writeParcelable(urls, i);
        parcel.writeParcelable(links, i);
        parcel.writeByte((byte) (sponsored == null ? 0 : sponsored ? 1 : 2));
        if (likes == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(likes);
        }
        parcel.writeByte((byte) (likedByUser == null ? 0 : likedByUser ? 1 : 2));
        parcel.writeString(slug);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(exif, i);
        if (views == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(views);
        }
        if (downloads == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(downloads);
        }
    }
}
