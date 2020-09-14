package com.example.snailmap.ui.dashboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.snailmap.MainActivity;
import com.example.snailmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.Collection;

public class DashboardFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    public DashboardFragment() {

    }

    Button reload;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        reload = (Button) root.findViewById(R.id.button2);
        if (getActivity() != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            //System.out.println(mapFragment);
            if (mapFragment != null) {
                //System.out.println("111");
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        LatLng perth = new LatLng(-31.953512, 	115.857048);
                        mMap.setMinZoomPreference(5);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(perth));
                        reload.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                addheatmap();

                            }
                        });

                    }
                });
            }

        }

        return root;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    int[] colors = {
            Color.GREEN,    // green
            Color.YELLOW,    // yellow
            Color.rgb(255,165,0), //Orange
            Color.RED,              //red
            Color.rgb(153,50,204), //dark orchid
            Color.rgb(165,42,42) //brown
    };

    float[] startpoints = {
            0.5f,    //0-50
            1f,   //51-100
            1.5f,   //101-150
            2f,   //151-200
            2.5f,    //201-300
            3f      //301-500
    };


    public void addheatmap() {
        ArrayList<WeightedLatLng> arr = new ArrayList<WeightedLatLng>();
        final long[] numberofsnails = new long[1];
        final double[] longitude = new double[1];
        final double[] latitude = new double[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> itemRef = db.collection("snail-detection")
                .whereEqualTo("HaveSnail", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                numberofsnails[0] = (long) document.get("NumberofSnails");
                                longitude[0] = (double) document.get("longitude");
                                latitude[0] = (double) document.get("latitude");
                                Log.d("TAG", longitude[0] + " " + latitude[0] + " " + numberofsnails[0]);
                                ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
                                list.add(new WeightedLatLng(new LatLng(latitude[0], longitude[0]), numberofsnails[0]));
                                buildheatmap(list);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });
    }


    private void buildheatmap(ArrayList list){
        Gradient gradient = new Gradient(colors,startpoints);
        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .radius(20)
                .gradient(gradient)
                .build();
        TileOverlayOptions tileoverlayoptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
        TileOverlay tileoverlay = mMap.addTileOverlay(tileoverlayoptions);
        tileoverlay.clearTileCache();
        Toast.makeText(getActivity(),"added heatmap",Toast.LENGTH_SHORT).show();
    }
}