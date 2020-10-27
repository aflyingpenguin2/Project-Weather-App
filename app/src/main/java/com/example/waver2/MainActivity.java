package com.example.waver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textViewCity, textViewTemp, textViewTempMax, textViewTempMin, textViewUpdatedAt, textViewStatus, textViewSunrise, textViewSunset, textViewWind, textViewPressure, textViewHumidity;
    LinearLayout linearLayoutNext7Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        Bundle bundle = getIntent().getExtras();
        String city = "";
        if (bundle != null) {
            city = bundle.getString("city");
        }

        getCurrentWeather(city);
    }

    private void initialize() {
        textViewCity = findViewById(R.id.textview_city);
        textViewUpdatedAt = findViewById(R.id.textview_updated_at);
        imageView = findViewById(R.id.imageView);
        textViewStatus = findViewById(R.id.textview_status);
        textViewTemp = findViewById(R.id.textview_temp);
        textViewTempMax = findViewById(R.id.textview_temp_max);
        textViewTempMin = findViewById(R.id.textview_temp_min);
        textViewSunrise = findViewById(R.id.textview_sunrise);
        textViewSunset = findViewById(R.id.textview_sunset);
        textViewWind = findViewById(R.id.textview_wind);
        textViewPressure = findViewById(R.id.textview_pressure);
        textViewHumidity = findViewById(R.id.textview_humidity);

        linearLayoutNext7Day = findViewById(R.id.next7day_layout);
    }

    public void getCurrentWeather(String data) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Hanoi&units=metric&appid=42ec86b3582ea33c87ab2eaa703f6272";
        if (data != "") {
            url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=42ec86b3582ea33c87ab2eaa703f6272";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //calling api

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

                    //find city, country
                    String country = sys.getString("country");
                    String city = jsonObject.getString("name");
                    textViewCity.setText(city + ", " + country);

                    //updated at
                    Long updatedAt = jsonObject.getLong("dt");
                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    textViewUpdatedAt.setText(updatedAtText);

                    //bind image icon
                    String img = weather.getString("icon");
                    Picasso.get().load("http://openweathermap.org/img/wn/"+img+"@4x.png").into(imageView);

                    //status
                    String status = convert(weather.getString("description"));
                    textViewStatus.setText(status);

                    //find temperature, min and max
                    String temperature = main.getString("temp") + "°C";
                    String temperature_min = "Min Temp: " + main.getString("temp_min") + "°C";
                    String temperature_max = "Max Temp: " + main.getString("temp_max") + "°C";
                    textViewTemp.setText(temperature);
                    textViewTempMax.setText(temperature_max);
                    textViewTempMin.setText(temperature_min);

                    //sunrise
                    Long sunrise = sys.getLong("sunrise");
                    textViewSunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));

                    //sunset
                    Long sunset = sys.getLong("sunset");
                    textViewSunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));

                    //wind
                    String windSpeed = wind.getString("speed");
                    textViewWind.setText(windSpeed);

                    //pressure
                    String pressure = main.getString("pressure");
                    textViewPressure.setText(pressure);

                    //humidity
                    String humidity = main.getString("humidity");
                    textViewHumidity.setText(humidity);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void getNext7daysWeather(String data) {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Hanoi&units=metric&cnt=7&appid=42ec86b3582ea33c87ab2eaa703f6272";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public String convert(String str) {
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z')
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }
        String st = new String(ch);
        return st;
    }

    public void showWeathernext7days(View view) {
        linearLayoutNext7Day.setVisibility(View.VISIBLE);
    }
}