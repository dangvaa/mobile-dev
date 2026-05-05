package com.example.recipesapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;

public class MapFragment extends Fragment {
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapview);
        Point krasnoyarskPoint = new Point(56.010569, 92.852572);
        mapView.getMap().move(
                new CameraPosition(krasnoyarskPoint, 11.0f, 0.0f, 0.0f)
        );

        addMarker(56.010569, 92.852572, "Центр Красноярска", "Театральная площадь");
        addMarker(55.994446, 92.797586, "ИКИТ", "Институт космических и информационных технологий");
        addMarker(55.995584, 92.797541, "ПИ", "Политехнический институт");
        addMarker(55.996782, 92.797712, "ИУБП", "Институт управления бизнес-процессами и экономики");
        addMarker(55.998776, 92.796993, "Командор", "Супермаркет");
        addMarker(55.996691, 92.791271, "Командор", "Супермаркет");
        return view;
    }

    private void addMarker(double lat, double lon, String title, String desc) {
        PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(new Point(lat, lon));
        placemark.setText(title);

        placemark.addTapListener((mapObject, point) -> {
            Toast.makeText(getContext(), desc, Toast.LENGTH_LONG).show();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}