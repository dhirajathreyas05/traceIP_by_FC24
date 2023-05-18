package com.example.traceip;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.*;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements IPGeolocationTask.GeolocationListener {

    private EditText ipAddressEditText;
    private Button traceButton;
    private TextView countryCodeTextView;
    private TextView countryNameTextView;
    private TextView regionNameTextView;
    private TextView cityNameTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressEditText = findViewById(R.id.ip_address_edit_text);
        traceButton = findViewById(R.id.trace_button);
        countryCodeTextView = findViewById(R.id.country_code_text_view);
        countryNameTextView = findViewById(R.id.country_name_text_view);
        regionNameTextView = findViewById(R.id.region_name_text_view);
        cityNameTextView = findViewById(R.id.city_name_text_view);
        latitudeTextView = findViewById(R.id.latitude_text_view);
        longitudeTextView = findViewById(R.id.longitude_text_view);

        traceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = ipAddressEditText.getText().toString();

                if (!TextUtils.isEmpty(ipAddress)) {
                    IPGeolocationTask task = new IPGeolocationTask(MainActivity.this);
                    task.execute(ipAddress);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter an IP address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onGeolocationReceived(String countryCode, String countryName, String regionName, String cityName,
                                      double latitude, double longitude) {
        countryCodeTextView.setText("Country Code: " + countryCode);
        countryNameTextView.setText("Country Name: " + countryName);
        regionNameTextView.setText("Region Name: " + regionName);
        cityNameTextView.setText("City Name: " + cityName);
        latitudeTextView.setText("Latitude: " + latitude);
        longitudeTextView.setText("Longitude: " + longitude);
    }

    @Override
    public void onGeolocationFailed() {
        Toast.makeText(this, "Failed to retrieve geolocation information", Toast.LENGTH_SHORT).show();
    }
}
