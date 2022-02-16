package com.example.indoorlocalization;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.indoorlocalization.EntityClass.TestEntity;
import com.example.indoorlocalization.EntityClass.TrainingEntity;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class LocateKNNFragment extends Fragment {

    View view;
    private TextView k_view, location;
    private LineChart lineChart;
    private LineData lineData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.locate_knn_fragment, container,false);
        k_view = (TextView) view.findViewById(R.id.k_v);
        location = (TextView) view.findViewById(R.id.location_value);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);

        List<TrainingEntity> data = RoomDatabaseActivity.getInstance(getActivity()).trainingDao().getValues();
        HashMap<Integer, Double> distance_map = new HashMap<>();
        List<TestEntity> test_data = RoomDatabaseActivity.getInstance(getActivity()).testDao().get_testValues();
        HashMap<Integer, HashMap<Integer, Double>> distance_test_map = new HashMap<>();

        Bundle bundle = this.getArguments();
        if(bundle.getSerializable("KNN Value") != null){
            distance_test_map = (HashMap<Integer, HashMap<Integer, Double>>) bundle.getSerializable("KNN Value");
        }
        if(bundle.getSerializable("Current Distance") != null){
            distance_map = (HashMap<Integer, Double>) bundle.getSerializable("Current Distance");
        }

        ArrayList<Entry> test_error = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        Double min_error = Double.POSITIVE_INFINITY;
        int k_value = 0;
        if (test_data.size() > 0){
            for (int k = 1; k < 6; k++){
                int error = 0;
                for (int i = 0; i < test_data.size(); i++) {
                    HashMap<String, Integer> count = new HashMap<>();
                    for (int j = 0; j < k; j++){
                       int position = (Integer) distance_test_map.get(i).keySet().toArray()[j];
                       String location = data.get(position).getLocation();
                       if (count.containsKey(location)){
                           count.put(location, count.get(location)+1);
                       }
                       else{
                           count.put(location, 1);
                       }
                    }
                    count = sort_data(count);
                    if (!(((String) count.keySet().toArray()[0]).equals(test_data.get(i).getLocation()))){
                        error += 1;
                    }
                }
                test_error.add(new Entry(error,k-1));
                values.add(String.valueOf(k));
                if (error < min_error){
                    k_value = k;
                    min_error = Double.valueOf(error);
                }
            }
        }
        else{
            Toast.makeText(getActivity(), "No Test Data Found!", Toast.LENGTH_SHORT).show();
        }
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);
        LineDataSet lineDataSet = new LineDataSet(test_error, "Error vs K");
        lineDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineChart.setDescription("Optimal K value with Minimal Error");
        lineChart.setDescriptionPosition(500f,-20f);
        lineData = new LineData(values,lineDataSet);
        lineDataSet.setCircleColor(Color.DKGRAY);
        lineDataSet.setColor(Color.DKGRAY);
        lineChart.setData(lineData);

        k_view.setText(String.valueOf(k_value));
        HashMap<String, Integer> count_1 = new HashMap<>();
        for (int j = 0; j < k_value; j++){
            int position = (Integer) distance_map.keySet().toArray()[j];
            String location = data.get(position).getLocation();
            if (count_1.containsKey(location)){
                count_1.put(location, count_1.get(location)+1);
            }
            else{
                count_1.put(location, 1);
            }
        }
        count_1 = sort_data(count_1);
        if(distance_map.keySet().toArray().length > 0) {
            String location_1 = (String) count_1.keySet().toArray()[0];
            location.setText(location_1);
        }
        else{
            Toast.makeText(getActivity(), "No Train Data Found!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public HashMap<String, Integer> sort_data (HashMap<String, Integer> map){
        List<Map.Entry<String, Integer> > stored_list = new LinkedList<Map.Entry<String, Integer> >(map.entrySet());
        Collections.sort(stored_list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<String, Integer> sorted_map = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> data : stored_list){
            sorted_map.put(data.getKey(), data.getValue());
        }
        return sorted_map;
    }
}
