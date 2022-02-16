package com.example.indoorlocalization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.indoorlocalization.EntityClass.TestEntity;
import com.example.indoorlocalization.EntityClass.TrainingEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LocateActivity extends AppCompatActivity implements CalculateDistance {

    private Button locate_simple, locate_knn;
    FragmentManager fm;
    private WifiManager wifiManager;
    private int rssi_1 = 0, rssi_2 = 0, rssi_3 = 0;
    private String location_value = " ", orientation_value = " ";
    private int flag = 0, choice = 0;
    LocateSimpleFragment locateSimpleFragment = new LocateSimpleFragment();
    LocateKNNFragment locateKNNFragment = new LocateKNNFragment();
    int choose = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        choice = 1;
        locate_simple = (Button)findViewById(R.id.locate_simple);
        locate_knn = (Button)findViewById(R.id.locate_knn);
        locate_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 1;
                if (choose == 0) {
                    loadFragment(locateSimpleFragment);
                }
                else{
                    Toast.makeText(LocateActivity.this, "Please Wait for 2 minutes! Then Press the Button again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locate_knn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 2;
                if (choose == 0) {
                    loadFragment(locateKNNFragment);
                }
                else{
                    Toast.makeText(LocateActivity.this, "Please Wait for 2 minutes! Then Press the Button again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment){
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(LocateActivity.this, "Please enable the Wifi!", Toast.LENGTH_LONG).show();
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
        if(wifiScanReceiver!=null && flag == 1) {
            unregisterReceiver(wifiScanReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void scanFailure(){
        choose = 1;
        Toast.makeText(LocateActivity.this, "Please Wait for 2 minutes! Then Press the Button again.", Toast.LENGTH_SHORT).show();
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
        choose = 0;
        List<ScanResult> results = wifiManager.getScanResults();
        WifiInfo info = wifiManager.getConnectionInfo();
        double min_distance = Double.POSITIVE_INFINITY;
        for (ScanResult scanResult : results) {
            if (scanResult.BSSID.equals("30:b5:c2:70:9d:e8")) {
                rssi_1 = scanResult.level;
            } else if (scanResult.BSSID.equals("fe:aa:b6:46:9c:51")) {
                rssi_2 = scanResult.level;
            } else if (scanResult.BSSID.equals("8a:bb:c9:71:22:2d")) {
                rssi_3 = scanResult.level;
            }
        }
        Log.d("RUN", "Current Location RSSI values " + rssi_1 + " " + rssi_2 + " " + rssi_3);
        List<TrainingEntity> data = RoomDatabaseActivity.getInstance(getApplicationContext()).trainingDao().getValues();
        HashMap<Integer, Double> distance_map = new HashMap<>();
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                int value_1 = data.get(i).getRssi_1();
                int value_2 = data.get(i).getRssi_2();
                int value_3 = data.get(i).getRssi_3();
                double distance_value = distance(rssi_1, value_1, rssi_2, value_2, rssi_3, value_3);
                distance_map.put(i, distance_value);
            }
        }
        else{
            Toast.makeText(LocateActivity.this, "No Training Data Found!", Toast.LENGTH_SHORT).show();
        }

        HashMap<Integer, Double> sorted_distance_map = sort_data(distance_map);
        List<TestEntity> test_data = RoomDatabaseActivity.getInstance(getApplicationContext()).testDao().get_testValues();
        HashMap<Integer, HashMap<Integer, Double>> distance_test_map = new HashMap<>();
        if (test_data.size() > 0){
            for (int j = 0; j < test_data.size(); j++){

                int test_value_1 = test_data.get(j).getRssi_1();
                int test_value_2 = test_data.get(j).getRssi_2();
                int test_value_3 = test_data.get(j).getRssi_3();
                HashMap<Integer, Double> test_map = new HashMap<>();

                for (int i = 0; i < data.size(); i++){
                    int value_1 = data.get(i).getRssi_1();
                    int value_2 = data.get(i).getRssi_2();
                    int value_3 = data.get(i).getRssi_3();
                    double distance_value = distance(test_value_1, value_1, test_value_2, value_2, test_value_3, value_3);
                    test_map.put(i, distance_value);
                }
                test_map = sort_data(test_map);
                distance_test_map.put(j, test_map);
            }
        }
        else{
            Toast.makeText(LocateActivity.this, "No Test Data Found!", Toast.LENGTH_SHORT).show();
        }

        Bundle bundle_1 = new Bundle();
        bundle_1.putSerializable("Simple", sorted_distance_map);
        locateSimpleFragment.setArguments(bundle_1);

        Bundle bundle_2 = new Bundle();
        bundle_2.putSerializable("KNN Value", distance_test_map);
        bundle_2.putSerializable("Current Distance", sorted_distance_map);
        locateKNNFragment.setArguments(bundle_2);
    }

    @Override
    public double distance(int x1, int y1, int x2, int y2, int x3, int y3) {
        double first = Math.pow(x1 - y1, 2);
        double second = Math.pow(x2 - y2, 2);
        double third = Math.pow(x3 - y3, 2);
        double eucledian_distance = Math.sqrt(first + second + third);
        return eucledian_distance;
    }

    public HashMap<Integer, Double> sort_data (HashMap<Integer, Double> map){
        List<Map.Entry<Integer, Double> > stored_list = new LinkedList<Map.Entry<Integer, Double> >(map.entrySet());
        Collections.sort(stored_list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<Integer, Double> sorted_map = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> data : stored_list){
            sorted_map.put(data.getKey(), data.getValue());
        }
        return sorted_map;
    }
}
