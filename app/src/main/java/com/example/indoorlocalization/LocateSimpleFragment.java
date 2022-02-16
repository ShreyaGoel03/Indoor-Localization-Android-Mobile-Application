package com.example.indoorlocalization;

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

import com.example.indoorlocalization.EntityClass.TrainingEntity;

import java.util.HashMap;
import java.util.List;

public class LocateSimpleFragment extends Fragment {
    View view;
    String location_value, orientation_value;
    TextView location, orientation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.locate_simple_fragment, container, false);
        location = (TextView) view.findViewById(R.id.location_v);
        orientation = (TextView) view.findViewById(R.id.orientation_v);
        List<TrainingEntity> data = RoomDatabaseActivity.getInstance(getActivity()).trainingDao().getValues();
        HashMap<Integer, Double> distance_map = new HashMap<>();
        Bundle bundle = this.getArguments();
        if(bundle.getSerializable("Simple") != null){
            distance_map = (HashMap<Integer, Double>) bundle.getSerializable("Simple");
        }
        if(distance_map.keySet().toArray().length > 0){
            int position = (Integer) distance_map.keySet().toArray()[0];
            location_value = data.get(position).getLocation();
            orientation_value = data.get(position).getOrientation();
            location.setText(location_value);
            orientation.setText(orientation_value);
        }
        else{
            Toast.makeText(getActivity(), "No Train Data Found!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
