package com.example.multimediaapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HaritaFragment extends Fragment implements OnMapReadyCallback {
//havuzlama!! ve konv katmanlarını sekil ile anlatırız teorik soru matlab kitabı derin öğrenme yeri 6-7-8-9---19.sayfaya kadar olan kısmı iyi oku tabloları çiz rakamları anla
    private GoogleMap googleMap;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_harita, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.my_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        return view;
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());


                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(myLocation)
                            .title("Benim Konumum");
                    googleMap.addMarker(markerOptions);


                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
                }
            });
        } else {

            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (googleMap != null) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(myLocation)
                                        .title("Benim Konumum");
                                googleMap.addMarker(markerOptions);
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
                            }
                        });
                    }
                }
            } else {

                Toast.makeText(requireContext(), "Konum izni verilmedi.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}