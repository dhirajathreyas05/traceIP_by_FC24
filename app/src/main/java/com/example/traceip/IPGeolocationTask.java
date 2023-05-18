package com.example.traceip;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPGeolocationTask extends AsyncTask<String, Void, JSONObject> {

    private static final String API_URL = "https://api.ipgeolocation.io/ipgeo?apiKey=6f53a2b4ca8f432db5561e3a1063aec3&ip=";
    private static final String TAG = IPGeolocationTask.class.getSimpleName();

    private GeolocationListener listener;

    public IPGeolocationTask(GeolocationListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String ipAddress = params[0];
        String urlString = API_URL + ipAddress;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return new JSONObject(response.toString());
            } else {
                Log.e(TAG, "Error: " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (result != null) {
            String countryCode = result.optString("country_code2");
            String countryName = result.optString("country_name");
            String regionName = result.optString("state_prov");
            String cityName = result.optString("city");
            double latitude = result.optDouble("latitude");
            double longitude = result.optDouble("longitude");

            // Pass the geolocation information to the listener
            listener.onGeolocationReceived(countryCode, countryName, regionName, cityName, latitude, longitude);
        } else {
            listener.onGeolocationFailed();
        }
    }

    public interface GeolocationListener {
        void onGeolocationReceived(String countryCode, String countryName, String regionName, String cityName,
                                   double latitude, double longitude);

        void onGeolocationFailed();
    }
}