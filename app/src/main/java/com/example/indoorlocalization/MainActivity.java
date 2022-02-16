package com.example.indoorlocalization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView list_RecyclerView;
    private MyAdapter adapter;
    private WifiManager wifiManager;
    ArrayList<RecyclerData_Wifi> wifi_list = new ArrayList<>();
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    private Button learn, test;
    private BarChart barChart;
    private int i = 0, flag = 0, permission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request_permission();
        list_RecyclerView = (RecyclerView) findViewById(R.id.rec_view);
        list_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MyAdapter(wifi_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        list_RecyclerView.setAdapter(adapter);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Please enable the Wifi!", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        scanWifi();
        boolean success = wifiManager.startScan();
        if(!success){
            scanFailure();
        }

        learn = (Button) findViewById(R.id.learn);
        test = (Button) findViewById(R.id.test);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                startActivityForResult(intent,1);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocateActivity.class);
                startActivityForResult(intent,2);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void request_permission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, 100);
            return;
        }
    }

    protected void onPause() {
        super.onPause();
        if(wifiScanReceiver!=null){
            unregisterReceiver(wifiScanReceiver);
        }
    }

    private void scanFailure(){
        Log.d("RUN", "Failed");
        if(permission != 2) {
            Toast.makeText(MainActivity.this, "Please Wait for 2 minutes!", Toast.LENGTH_SHORT).show();
            List<ScanResult> results = wifiManager.getScanResults();
        }
    }

    private void scanWifi() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
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
        barChart = (BarChart) findViewById(R.id.bar_chart);;
        i = 0;
        wifi_list.clear();
        entries.clear();
        labels.clear();
        barChart.invalidate();
        barChart.clear();

        for (ScanResult scanResult : results) {
            RecyclerData_Wifi data = new RecyclerData_Wifi(String.valueOf(scanResult.SSID), String.valueOf(scanResult.level));
            int value = scanResult.level + 100;
            entries.add(new BarEntry(value,i));
            labels.add(scanResult.SSID);
            i += 1;
            wifi_list.add(data);
            adapter.notifyDataSetChanged();
        }

        barChart.setDrawBarShadow(false);
        BarDataSet barDataSet = new BarDataSet(entries, "Strength");
        BarData barData = new BarData(labels, barDataSet);
        Legend legend = barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        barChart.getAxisLeft().setAxisMinValue(0f);
        barChart.getAxisRight().setAxisMinValue(0f);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelsToSkip(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(5);
        xAxis.setLabelRotationAngle(-90f);
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        barChart.setData(barData);
        barDataSet.setBarSpacePercent(10f);
        barChart.setDescription("Available Wifi's and their Strengths (dBm)");
        barChart.setDescriptionPosition(540f,45f);
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barChart.animateY(1000);
        xAxis.setDrawGridLines(false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission = 1;
                    Toast.makeText(MainActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // The user disallowed the requested permission.
                    permission = 2;
                    Toast.makeText(MainActivity.this, "Please Grant Permission. It is needed for Access Points.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.DetailsViewHolder>{

        private ArrayList<RecyclerData_Wifi> access_network_holder;

        public MyAdapter(ArrayList<RecyclerData_Wifi> access_network_holder){
            this.access_network_holder = access_network_holder;
        }

        @NonNull
        @Override
        public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_detail, parent, false);
            return new DetailsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
            holder.name.setText(access_network_holder.get(position).getName());
            holder.rssi.setText(String.valueOf(Integer.parseInt(access_network_holder.get(position).getRssi())+100) + " dBm");
            RecyclerData_Wifi item = access_network_holder.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return access_network_holder.size();
        }

        private class DetailsViewHolder extends RecyclerView.ViewHolder {

            private RecyclerData_Wifi wifi_data;
            TextView name, rssi;
            public DetailsViewHolder(@NonNull View itemView){
                super(itemView);
                name = itemView.findViewById(R.id.name);
                rssi = itemView.findViewById(R.id.rssi);
            }

            public void bind(RecyclerData_Wifi data){
                wifi_data = data;
            }

        }
    }

}