package com.example.indoorlocalization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoorlocalization.EntityClass.TestEntity;
import com.example.indoorlocalization.EntityClass.TrainingEntity;

import java.util.List;

public class LearnActivity extends AppCompatActivity {

    Spinner location, orientation;
    Button save, save_test;
    private WifiManager wifiManager;
    private String location_value, orientation_value;
    private int rssi_1 = 0, rssi_2 = 0, rssi_3 = 0, flag = 0, value = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_activity);

        location = (Spinner) findViewById(R.id.location);
        orientation = (Spinner) findViewById(R.id.orientation);

        ArrayAdapter<String> location_adapter = new ArrayAdapter<String>(LearnActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.locations));
        location_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(location_adapter);

        ArrayAdapter<String> orientation_adapter = new ArrayAdapter<String>(LearnActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.orientations));
        orientation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orientation.setAdapter(orientation_adapter);

        save = (Button) findViewById(R.id.save);
        save_test = (Button) findViewById(R.id.save_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location_value = location.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orientation_value = orientation.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = 1;
                wifi_manage();
            }
        });

        save_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = 2;
                wifi_manage();
            }
        });


    }

    protected void wifi_manage(){
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(LearnActivity.this, "Please enable the Wifi!", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        scanWifi();
        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(wifiScanReceiver != null && flag == 1) {
            unregisterReceiver(wifiScanReceiver);
        }
    }


    private void scanFailure(){
        Log.d("RUN", "Failed");
        Toast.makeText(LearnActivity.this, "Please Wait for 2 minutes! Then Press the Button again.", Toast.LENGTH_SHORT).show();
        List<ScanResult> results = wifiManager.getScanResults();
    }

    private void scanWifi() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
        flag = 1;
    }

    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scan_wifi_entry();

            } else {
                scanFailure();
            }

        }
    };

    private void scan_wifi_entry(){
        List<ScanResult> results = wifiManager.getScanResults();
        WifiInfo info = wifiManager.getConnectionInfo();
        Log.d("RUN", String.valueOf(value));
        for (ScanResult scanResult : results) {
            if (scanResult.BSSID.equals("30:b5:c2:70:9d:e8")) {
                rssi_1 = scanResult.level;
            } else if (scanResult.BSSID.equals("fe:aa:b6:46:9c:51")) {
                rssi_2 = scanResult.level;
            } else if (scanResult.BSSID.equals("8a:bb:c9:71:22:2d")) {
                rssi_3 = scanResult.level;
            }
        }
        if (value == 1) {
            TrainingEntity trainingEntity = new TrainingEntity(location_value, orientation_value, rssi_1, rssi_2, rssi_3);
            RoomDatabaseActivity.getInstance(getApplicationContext()).trainingDao().train_insert(trainingEntity);
            Log.d("RUN", "Training Data Added " + rssi_1 + " " + rssi_2 + " " + rssi_3);
            Toast.makeText(LearnActivity.this, "Training Data Added", Toast.LENGTH_SHORT).show();
        }
        else if (value == 2){
            TestEntity testEntity = new TestEntity(location_value, orientation_value, rssi_1, rssi_2, rssi_3);
            RoomDatabaseActivity.getInstance(getApplicationContext()).testDao().test_insert(testEntity);
            Log.d("RUN", "Test Data Added " + rssi_1 + " " + rssi_2 + " " + rssi_3);
            Toast.makeText(LearnActivity.this, "Test Data Added", Toast.LENGTH_SHORT).show();
        }
    }
}
