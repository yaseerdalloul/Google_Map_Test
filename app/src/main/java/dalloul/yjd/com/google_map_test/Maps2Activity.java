package dalloul.yjd.com.google_map_test;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Maps2Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location Location;
    String lll;
    double lat;
    double lng;
    private Polyline mPoly;
    EditText TFaddress;
    TextView TFaddress2,txt11;
    EditText location_tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        TFaddress=findViewById(R.id.TFaddress);
        TFaddress2=findViewById(R.id.TFaddress2);
        txt11=findViewById(R.id.txt11);
        location_tf = (EditText) findViewById(R.id.TFaddress);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


         List<LatLng> path = this.getSamplePoly();
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(this.getLineWidth());
            mPoly = mMap.addPolyline(opts);
        }

        LatLng pos = new LatLng(31.515667187852863, 34.44741155952216);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.515667187852863, 34.44741155952216), 7));
        mMap.addMarker(new MarkerOptions().position(new LatLng(31.515667187852863, 34.44741155952216)).title("Test"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.addPolyline(new PolylineOptions().add(pos,new LatLng(31.515667187852863, 106.44741155952216),
                                                        new LatLng(31.515667187852863, 106.44741155952216),
                pos )
        );

        if (ActivityCompat.checkSelfPermission(Maps2Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Maps2Activity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        Geocoder gc = new Geocoder(this);
        try {


            //List<Address> list = gc.getFromLocationName(String.valueOf(Location), 1);
            List<Address> list = gc.getFromLocationName("", 1);
            Address address = list.get(0);
            lll = address.getSubLocality();
            lat = address.getLatitude();
            lng = address.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkerOptions options = new MarkerOptions()
                .title(lll)
                .position(new LatLng(lat, lng));
        mMap.addMarker(options);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //  mMap.addMarker(new MarkerOptions().position(new LatLng(31.70480307, 35.0079)).title(showDialog(latLng)+""));
                mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng)));
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                TFaddress2.setText( latLng.latitude+"");
                txt11.setText( latLng.longitude+"");


            }
        });
    }



    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    public void onSearch(View view) {

        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location_tf.getText().toString()));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }

//    public void onZoom(View view) {
//        if (view.getId() == R.id.Bzoomin) {
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//        }
//        if (view.getId() == R.id.Bzoomout) {
//            mMap.animateCamera(CameraUpdateFactory.zoomOut());
//        }
//    }

    public void changeType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    private List<LatLng> getSamplePoly () {
        List<LatLng> path = new ArrayList();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD3vxW2r3IGa03JwBnvWYhWNiDbMfTtFlg")
                .build();

        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.381624,2.176058", "41.380503,2.177116");
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("test", ex.getLocalizedMessage());
        }

        return path;
    }
    private int getLineWidth() {
        float zoom = mMap.getCameraPosition().zoom;
        float a = (zoom-12>0 ? (zoom-12)*(zoom-12) : 1);
        return Math.round(a);
    }



}
