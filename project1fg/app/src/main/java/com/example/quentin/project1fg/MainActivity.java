package com.example.quentin.project1fg;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ImageVideoWrapperEncoder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.clustering.ClusterItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        //ClusterManager.OnClusterItemClickListener<clusterItem>,
        //GoogleMap.OnMapClickListener,
        //GoogleMap.InfoWindowAdapter,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    // private LatLng mPosition;
    // private String mTitle;
    // private String mSnippet;

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;

    int sliderValue;
    int PROXIMITY_RADIUS;
    double latitude, longitude;

    SeekBar slider1;

    public static final String dataSendingKey = "com.example.MapsActivity.DATA_PILE";

    // Start of onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("JDRent", "onPanelSlide, offset " + slideOffset);
                ImageView arrow = findViewById(R.id.expandLessView);
                if (slideOffset > 0.50) {
                    arrow.setImageDrawable(null);
                    arrow.setImageResource(R.drawable.ic_expand_more_black_48dp);
                    arrow.setColorFilter(getResources().getColor(R.color.johnDeereYellow));
                } else if (slideOffset < 0.50) {
                    arrow.setImageDrawable(null);
                    arrow.setImageResource(R.drawable.ic_expand_less_black_48dp);
                    arrow.setColorFilter(getResources().getColor(R.color.johnDeereYellow));
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("JDRent", "onPanelStateChanged " + newState);

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(COLLAPSED);
            }
        });


        slider1 = findViewById(R.id.seekBar1);
        PROXIMITY_RADIUS = 1609 * 29;

        slider1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 29;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                PROXIMITY_RADIUS = 1609 * progressChangedValue + 1609;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MapsActivity.this, "Search radius: " + (progressChangedValue + 1) + " miles",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //mMap.setOnMarkerClickListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }
    // End of onCreate

    // Sets up marker called based on data given (Machine object, map fragment, number, name).
    public void setMarker(Machine data, GoogleMap googleMap, int markerNum, Marker markerName) {

        mMap = googleMap;
        LatLng latLng = new LatLng(data.getLatitude(), data.getLongitude());

        int type = data.getType();

        // markerOptions.position(latLng);
        // MarkerOptions markerOptions = new MarkerOptions();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        String titleStr = (data.getMachineName());
        String snippetStr = "N/A";
        markerOptions.title(titleStr);

        // 1 = ag
        // 2 = construction
        // 3 = turf
        // Sets snippets based on type of machine.
        if (type == 1) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            snippetStr = "Agriculture";
        } else if (type == 2) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            snippetStr = "Construction";
        } else if (type == 3) {                                                                                  //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            markerOptions.icon(getMarkerIcon("#26BBD9"));
            snippetStr = "Turf";
        }
        markerOptions.snippet(snippetStr);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        markerName = mMap.addMarker(markerOptions);
        markerName.setTag(markerNum);
    }

    // For more marker colors
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    // When it's confirmed that the user allowed us to use their exact GPS location.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    // Called when the Google Map is ready to be run.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Sets the map value, and sets up onInfoWindowClickListener (opening each marker's info).
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    // Google API
    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }

    // GPS changes
    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        Log.d("lat = ", "" + latitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        currentLocationMarker = mMap.addMarker(markerOptions);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, (com.google.android.gms.location.LocationListener) this);
        }
    }

    // For retrieving data, filtering. KEPT TO REFERENCE AND/OR USE LATER.
    /*
    public void onClick(View v) {
        // Object dataTransfer[] = new Object[2];
        // GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch (v.getId()) {
            case R.id.B_search:
                EditText tf_location =  findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList;


                if(!location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                        if(addressList != null)
                        {
                            for(int i = 0;i<addressList.size();i++)
                            {
                                LatLng latLng = new LatLng(addressList.get(i).getLatitude() , addressList.get(i).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(location);
                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.B_hopistals:

                case R.id.B_hopistals:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
                break;

            case R.id.B_schools:
                mMap.clear();
                String school = "school";
                url = getUrl(latitude, longitude, school);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Schools", Toast.LENGTH_SHORT).show();
                break;
            case R.id.B_restaurants:
                mMap.clear();
                String resturant = "restuarant";
                url = getUrl(latitude, longitude, resturant);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
                break;


            case R.id.buttonProfile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            //case R.id.buttonMap:
            // N/A
            //case R.id.buttonNotifications:
            //Intent intentNotifications = new Intent(this, NotificationsActivity.class);
            //startActivity(intentNotifications);
            //break;
            case R.id.buttonSettings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.clearFilters:
                filterMarkers(0, mMap);
                Toast.makeText(this, "Filters cleared and reset.", Toast.LENGTH_SHORT).show();
                mLayout.setPanelState(COLLAPSED);
                //onLocationChanged(lastlocation);
                break;
            case R.id.agricultureFilter:
                filterMarkers(1, mMap);
                Toast.makeText(this, "Only showing Agriculture machines.", Toast.LENGTH_SHORT).show();
                mLayout.setPanelState(COLLAPSED);
                //onLocationChanged(lastlocation);
                break;
            case R.id.constructionFilter:
                filterMarkers(2, mMap);
                Toast.makeText(this, "Only showing Construction machines.", Toast.LENGTH_SHORT).show();
                mLayout.setPanelState(COLLAPSED);
                //onLocationChanged(lastlocation);
                break;
            case R.id.turfFilter:
                filterMarkers(3, mMap);
                Toast.makeText(this, "Only showing Turf machines.", Toast.LENGTH_SHORT).show();
                mLayout.setPanelState(COLLAPSED);
                //onLocationChanged(lastlocation);
                break;

        }
    }
    */


    // For retrieving data from Google servers (unused).
    /*
    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyBT5iEyi46P0X5IJUFPmeqgTe95tGVEcRY");

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }
    */

    // Don't worry about this (does stuff when Google Maps is connected).
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }

    // User permission for checking GPS stuff.
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // public LatLng getPosition() { return mPosition; }

    // public String getSnippet() { return mSnippet; }

    // When an InfoWindow of a marker is tapped.
    @Override
    public void onInfoWindowClick(Marker calledMarker) {

        // Makes sure the current location marker doesn't return anything (it will crash the app)
        if (currentLocationMarker == null) {
            Double latitude = -90.43;
            Double longitude = 41.5131;
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            currentLocationMarker = mMap.addMarker(markerOptions);
        }
        if (calledMarker.getTag() != currentLocationMarker.getTag()) {

            // Right now the data calling is rough, but if we refine it enough this could be rewritten
            // to look for a unique value that both a marker and a row of data could share, so when this
            // method is called, it would search for the matching data instead of the current way (since this is hard-coded).
            int serialNum = 1, hoursUsed = 1;
            String model = "N/A", machineYear = "N/A", firstName = "N/A", lastName = "N/A", machineName = "N/A", lenderPhone = "N/A", lenderBio = "N/A", machineBio = "N/A", machineIssues = "N/A";
            int type = 1;
            double latitude = 1.0, longitude = 1.0, lenderRating = 1.0, machineRating = 1.0;

            // Gets and shows the number of the marker called.
            Integer markerTag = (Integer) calledMarker.getTag();
            Toast.makeText(MapsActivity.this, "Hi, you called marker #" + markerTag, Toast.LENGTH_SHORT).show();

            // Sets all variables from corresponding data (rough, can change it to be 100x more compact in future).
            // --note to self, make dataPile straight in if instead of after datacalling (for polish)

            String dataPile = (serialNum + "_" + model + "_" + hoursUsed + "_" + machineYear + "_" + type + "_" + latitude + "_" + longitude + "_" + firstName + "_" + lastName + "_" + machineName + "_" + lenderRating + "_" + machineRating + "_" + lenderPhone + "_" + lenderBio + "_" + machineBio + "_" + machineIssues);
            Toast.makeText(this, dataPile, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TractorPage.class);
            intent.putExtra(dataSendingKey, dataPile);
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}