package com.project.azantime.api;

import com.project.azantime.models.PrayerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrayerApi {

    @GET("timings")
    Call<PrayerResponse> getPrayerTimes(
            @Query("latitude") double lat,
            @Query("longitude") double lon,
            @Query("method") int method
    );
}
