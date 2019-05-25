package com.nekkies.wllppr;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nekkies.wllppr.API.APICallService;
import com.nekkies.wllppr.RetrofitModels.Photo;

import retrofit2.Call;
import retrofit2.Response;

public class BottomStuff extends BottomSheetDialogFragment {
    TextView info_resolution;
    TextView info_date;
    TextView info_exif_camera_model;
    TextView info_exif_exposure_time;
    TextView info_exif_aperture;
    TextView info_exif_focal_length;
    TextView info_exif_iso;
    TextView info_views;
    TextView info_downloads;

    private String pretty_date;
    private String photo_id;

    private Photo detailed_photo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.bottom_info_sheet,container, false);

        info_resolution = (TextView) v.findViewById(R.id.info_resolution);
        info_date = (TextView) v.findViewById(R.id.info_date);

        info_exif_camera_model = (TextView) v.findViewById(R.id.info_exif_camera_model);
        info_exif_exposure_time = (TextView) v.findViewById(R.id.info_exif_exposure_time);
        info_exif_aperture = (TextView) v.findViewById(R.id.info_exif_aperture);
        info_exif_focal_length = (TextView) v.findViewById(R.id.info_exif_focal_length);
        info_exif_iso = (TextView) v.findViewById(R.id.info_exif_iso);

        info_views = (TextView) v.findViewById(R.id.info_views);
        info_downloads = (TextView) v.findViewById(R.id.info_downloads);

        Bundle bundle = this.getArguments();
        pretty_date = bundle.getString("INFO_DATE");
        photo_id = bundle.getString("INFO_ID");

        APICallService apiCallService = new APICallService(null);
        APICallService.OnGetDetailsListener listener = new APICallService.OnGetDetailsListener() {
            @Override
            public void onSuccess(Call<Photo> call, Response<Photo> response) {
                detailed_photo = response.body();
                setViews();
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Toast.makeText(getActivity(), "fuck", Toast.LENGTH_SHORT).show();
            }
        };
        apiCallService.getDetails(photo_id, listener);
        return v;
    }


    void setViews(){

        info_resolution.setText("Resolution: " + String.valueOf(detailed_photo.getWidth()) + "x" +
                String.valueOf(detailed_photo.getHeight()));
        info_date.setText("Uploaded: " + pretty_date);

        info_exif_camera_model.setText("Camera: " + detailed_photo.getExif().getMake() + " " +
                detailed_photo.getExif().getModel());
        info_exif_aperture.setText("Aperature: " + detailed_photo.getExif().getAperture());
        info_exif_exposure_time.setText("Exposure time: " + detailed_photo.getExif().getExposureTime());
        info_exif_focal_length.setText("Focal length: " + detailed_photo.getExif().getFocalLength());
        info_exif_iso.setText("ISO: " + detailed_photo.getExif().getIso());

        info_views.setText("Views: " + String.valueOf(detailed_photo.getViews()));
        info_downloads.setText("Downlaods: " + String.valueOf(detailed_photo.getDownloads()));
    }
}
