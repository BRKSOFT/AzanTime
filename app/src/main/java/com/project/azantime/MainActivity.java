package com.project.azantime;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.azantime.api.PrayerApi;
import com.project.azantime.models.PrayerResponse;
import com.project.azantime.models.Timings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView txtFajr;
    private TextView txtDhuhr;
    private TextView txtAsr;
    private TextView txtMaghrib;
    private TextView txtIsha;
    private ProgressBar progressBar;
    private TextView txtLoading;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFajr = findViewById(R.id.txtFajr);
        txtDhuhr = findViewById(R.id.txtDhuhr);
        txtAsr = findViewById(R.id.txtAsr);
        txtMaghrib = findViewById(R.id.txtMaghrib);
        txtIsha = findViewById(R.id.txtIsha);
        progressBar = findViewById(R.id.progressBar);
        txtLoading = findViewById(R.id.txtLoading);
        txtError = findViewById(R.id.txtError);

        getPrayerTimes();
    }

    private void getPrayerTimes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.aladhan.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PrayerApi api = retrofit.create(PrayerApi.class);
        // Çorlu/Tekirdağ: 41.1592, 27.8000
        Call<PrayerResponse> call = api.getPrayerTimes(41.1592, 27.8000, 13);

        call.enqueue(new Callback<PrayerResponse>() {
            @Override
            public void onResponse(Call<PrayerResponse> call, Response<PrayerResponse> response) {

                progressBar.setVisibility(View.GONE);
                txtLoading.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Timings t = response.body().data.timings;
                    txtError.setVisibility(View.GONE);

                    txtFajr.setText("İmsak: " + t.fajr);
                    txtDhuhr.setText("Öğle: " + t.dhuhr);
                    txtAsr.setText("İkindi: " + t.asr);
                    txtMaghrib.setText("Akşam: " + t.maghrib);
                    txtIsha.setText("Yatsı: " + t.isha);
                } else {
                    txtError.setVisibility(View.VISIBLE);
                    txtError.setText("API yanıt hatası: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PrayerResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                txtLoading.setVisibility(View.GONE);
                txtError.setVisibility(View.VISIBLE);
                txtError.setText("Bağlantı hatası: " + t.getMessage());
                t.printStackTrace();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        txtLoading.setVisibility(View.VISIBLE);
    }
}
