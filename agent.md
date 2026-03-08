# 📱 Ezan Vakti Prototip Android Uygulaması (Tekirdağ)

Bu doküman **Android Studio (Java)** kullanılarak geliştirilecek **basit bir prototip uygulamayı** anlatır.

Amaç:

- Tek ekranlı bir uygulama oluşturmak
- Tekirdağ için **güncel namaz vakitlerini API üzerinden çekmek**
- Ana ekranda gösterilmesini sağlamak

Bu proje **basitten ileri seviyeye gidecek Ezan Vakti uygulamasının ilk adımıdır.**

---

# 1. Proje Özeti

**Uygulama adı**

```
EzanVaktiPrototype
```

**Teknolojiler**

```
Android Studio
Java
Retrofit (API için)
Gson
```

**Minimum SDK**

```
API 24 (Android 7)
```

---

# 2. Uygulama Özellikleri

Bu prototipte sadece aşağıdaki özellikler bulunacaktır:

- Tek ana ekran
- Tekirdağ için namaz vakitlerini API'den çekme
- Güncel vakitleri ekrana yazdırma
- Basit bir UI

---

# 3. Kullanılacak API

Namaz vakitleri için:

```
https://api.aladhan.com/v1/timings
```

### Tekirdağ Koordinatları

```
Latitude: 40.978
Longitude: 27.511
```

### API Örneği

```
https://api.aladhan.com/v1/timings?latitude=40.978&longitude=27.511&method=13
```

---

# 4. Proje Yapısı

```
com.ezanvakti.prototype

MainActivity.java
api
   PrayerApi.java
models
   PrayerResponse.java
   Timings.java
```

---

# 5. Android İzinleri

`AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

---

# 6. Gerekli Kütüphaneler

`build.gradle`

```gradle
dependencies {

implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

}
```

---

# 7. Ana Ekran Tasarımı

`activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>

<LinearLayout
xmlns:android="http://schemas.android.com/apk/res/android"
android:orientation="vertical"
android:padding="24dp"
android:layout_width="match_parent"
android:layout_height="match_parent">

<TextView
android:id="@+id/txtCity"
android:text="Tekirdağ"
android:textSize="24sp"
android:textStyle="bold"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>

<TextView
android:id="@+id/txtFajr"
android:text="İmsak:"
android:textSize="18sp"
android:layout_marginTop="20dp"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>

<TextView
android:id="@+id/txtDhuhr"
android:text="Öğle:"
android:textSize="18sp"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>

<TextView
android:id="@+id/txtAsr"
android:text="İkindi:"
android:textSize="18sp"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>

<TextView
android:id="@+id/txtMaghrib"
android:text="Akşam:"
android:textSize="18sp"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>

<TextView
android:id="@+id/txtIsha"
android:text="Yatsı:"
android:textSize="18sp"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>

</LinearLayout>
```

---

# 8. API Interface

`PrayerApi.java`

```java
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
```

---

# 9. Model Sınıfları

## Timings.java

```java
public class Timings {

public String Fajr;
public String Dhuhr;
public String Asr;
public String Maghrib;
public String Isha;

}
```

---

## PrayerResponse.java

```java
public class PrayerResponse {

public Data data;

public class Data {

public Timings timings;

}

}
```

---

# 10. MainActivity

`MainActivity.java`

```java
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

TextView fajr,dhuhr,asr,maghrib,isha;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

fajr = findViewById(R.id.txtFajr);
dhuhr = findViewById(R.id.txtDhuhr);
asr = findViewById(R.id.txtAsr);
maghrib = findViewById(R.id.txtMaghrib);
isha = findViewById(R.id.txtIsha);

getPrayerTimes();

}

private void getPrayerTimes(){

Retrofit retrofit = new Retrofit.Builder()
.baseUrl("https://api.aladhan.com/v1/")
.addConverterFactory(GsonConverterFactory.create())
.build();

PrayerApi api = retrofit.create(PrayerApi.class);

Call<PrayerResponse> call =
api.getPrayerTimes(40.978,27.511,13);

call.enqueue(new Callback<PrayerResponse>() {
@Override
public void onResponse(Call<PrayerResponse> call, Response<PrayerResponse> response) {

if(response.isSuccessful()){

Timings t = response.body().data.timings;

fajr.setText("İmsak: " + t.Fajr);
dhuhr.setText("Öğle: " + t.Dhuhr);
asr.setText("İkindi: " + t.Asr);
maghrib.setText("Akşam: " + t.Maghrib);
isha.setText("Yatsı: " + t.Isha);

}

}

@Override
public void onFailure(Call<PrayerResponse> call, Throwable t) {

t.printStackTrace();

}
});

}

}
```

---

# 11. Uygulama Görünümü

```
Tekirdağ

İmsak: 05:45
Öğle: 13:12
İkindi: 16:25
Akşam: 19:02
Yatsı: 20:20
```

---

# 12. Prototip Geliştirme Planı

### Aşama 1 (Bu doküman)

- Tek ekran
- API'den vakit çekme
- Ekrana yazdırma

### Aşama 2

- Sonraki namaza kalan süre
- UI iyileştirme
- CardView tasarım

### Aşama 3

- Konumdan şehir bulma
- Otomatik namaz vakti

### Aşama 4

- Bildirim sistemi
- Ezan alarmı

---

# 13. Sonraki Geliştirme Adımı

Bir sonraki aşamada eklenmesi önerilen özellik:

```
Sonraki Namaza Kalan Süre
```

Örnek:

```
İkindiye Kalan Süre

01:12:45
```

Bu özellik uygulamayı **gerçek bir ezan vakti uygulamasına yaklaştıran ilk advanced adımdır.**

---