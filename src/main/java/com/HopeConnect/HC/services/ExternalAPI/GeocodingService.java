package com.HopeConnect.HC.services.ExternalAPI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeocodingService {

    private final String apiKey = "e958df22a8a74bf08e6165a4b13db0d5";

    public Map<String, Float> getGeocodingData(String address) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.opencagedata.com/geocode/v1/json?q=" + address +
                "&key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            JSONArray results = json.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject firstResult = results.getJSONObject(0);
                JSONObject geometry = firstResult.getJSONObject("geometry");
                double lat = geometry.getDouble("lat");
                double lng = geometry.getDouble("lng");

                // Convert latitude and longitude to integers
                float intLat = (float) lat;
                float intLng = (float) lng;

                // Store in a map
                Map<String, Float> coordinates = new HashMap<>();
                coordinates.put("lat", intLat);
                coordinates.put("lng", intLng);

                return coordinates;
            } else {
                throw new IOException("No results found");
            }
        }
    }
}