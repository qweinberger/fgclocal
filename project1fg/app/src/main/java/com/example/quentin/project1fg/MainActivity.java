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
        com.google.android.gms.location.LocationListener
{

    // private LatLng mPosition;
    // private String mTitle;
    // private String mSnippet;

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationMarker, dataMarker1, dataMarker2, dataMarker3, dataMarker4, dataMarker5, dataMarker6, dataMarker7, dataMarker8, dataMarker9, dataMarker10, dataMarker11, dataMarker12, dataMarker13, dataMarker14, dataMarker15, dataMarker16, dataMarker17, dataMarker18, dataMarker19, dataMarker20, dataMarker21;

    public static final int REQUEST_LOCATION_CODE = 99;

    int sliderValue;
    int PROXIMITY_RADIUS;
    double latitude,longitude;

    SeekBar slider1;

    public static final String dataSendingKey = "com.example.MapsActivity.DATA_PILE";

    // Types
    // 1 = ag
    // 2 = construction
    // 3 = turf
    Machine data1 = new Machine(123456, "1710D", 900, "2012", 2, 41.541602, -90.519909, "Billy", "Appleseed", "Crane", 3.0, 3.0, "563-263-8794", "I love my crane!","Lifts heavy items and moves them.","Dented on the hood.");
    Machine data2 = new Machine(595746, "D170", 600, "2016", 3, 41.574307, -90.514361,"Cassidy", "Besterest", "Lawnmower", 4.0, 4.0, "563-677-2737", "I'm only on here to rent my mower.","Cuts grass with accuracy and precision.","N/A");
    Machine data3 = new Machine(623478, "850i", 630, "2014", 3, 41.511326, -90.536196, "Colton", "Darwin", "Gators", 2.5, 2.0, "563-893-6798", "I own a small golf course but i don't need my equipment all the time!","All purpose 4 wheel vehicles.","N/A");
    Machine data4 = new Machine(197563, "1200A", 789, "2012", 3, 41.571326, -90.526196, "Colton", "Darwin", "Bunker Rakes", 2.5, 3.0, "563-893-6798", "I own a small golf course but i don't need my equipment all the time!","Rakes the sand in golf courses.","Slight ruting.");
    Machine data5 = new Machine(789556, "c850", 1000, "2009", 1, 41.580819, -90.524351, "Varun", "Five", "Grain Trucks", 5.0, 5.0, "563-698-4927", "I don't really use my grain truck so it will frequently be available.","Vehicle that trasports grain after harvest.","N/A");
    Machine data6 = new Machine(976264, "312GR", 345, "2012", 2, 41.546447, -90.144739, "Jimmy", "Hendricks", "Skid Loader", 2.5, 2.0, "563-972-2794", "Call me for more info about my machines! Serious inquires only!","Multipurpose construction equipment.","N/A");
    Machine data7 = new Machine(543791, "460E ADT", 2500, "2013", 2, 41.576447, -90.249739, "Jimmy", "Hendricks", "Dump Trucks", 2.5, 3.0, "563-972-2794", "Call me for more info about my machines! Serious inquires only!","Unload materials.","The back of the truck can't go up.");
    Machine data8 = new Machine(236545, "324E", 920, "2012", 2, 41.412823, -90.432859, "Nick", "Hilton", "Skid Loader", 2.0, 1.0, "563-790-2567", "I live with my parents. Im looking to make some money for rent.","Multipurpose construction equipment.","N/A");
    Machine data9 = new Machine(795626, "D130", 890, "2008", 3, 41.552823, -90.481859, "Nick", "Hilton", "Lawnmower", 2.0, 3.0, "563-790-2567", "I live with my parents. Im looking to make some money for rent.","Cuts grass with accuracy and precision.","It can't go up steep hills.");
    Machine data10 = new Machine(732619, "950K", 273, "2012", 2, 41.573990, -90.599961, "Quentin", "Jenner", "Dozers", 3.0, 3.0, "563-679-9602", "hmu on snapchat for lipstick deals and dozers!","Pushes items down.","N/A");
    Machine data11 = new Machine(599243, "R4045", 1223, "2008", 2, 41.317258, -90.480150, "Jacob", "Kardashian", "Sprayer", 4.0, 4.0, "563-561-1212", "Im an American hero. Im an army vet looking to lend my machines!","Sprays chemicals and water on the field.","N/A");
    Machine data12 = new Machine(195565, "330G", 832, "2011", 2, 41.093258, -90.380150, "Jacob", "Kardashian", "Skid Loader", 4.0, 3.0, "563-561-1212", "Im an American hero. Im an army vet looking to lend my machines!","Multipurpose construction equipment.","The right skid doesnt stay on the track.");
    Machine data13 = new Machine(899331, "670G LC", 2100, "2002", 2, 41.437258, -90.440150, "Jacob", "Kardashian", "Excavator", 4.0, 5.0, "563-561-1212", "Im an American hero. Im an army vet looking to lend my machines!","Digs holes.","N/A");
    Machine data14 = new Machine(465652, "303D", 1298, "2001", 2, 41.553822, -90.454060, "Alexander", "Mc'Chikn", "Crane", 2.6, 3.0, "563-869-1049", "I have all the construction machines you need for your DIY's","Lifts heavy items and moves them.","N/A");
    Machine data15 = new Machine(303132, "RSX 860i", 785, "2005", 3, 41.033822, -90.015060, "Alexander", "Mc'Chikn", "Gators", 2.6, 3.0, "563-869-1049", "I have all the construction machines you need for your DIY's","All purpose 4 wheel vehicles.","N/A");
    Machine data16 = new Machine(800813, "410E", 229, "2012", 2, 41.553822, -90.495060, "Alexander", "Mc'Chikn", "Dump Trucks", 2.6, 2.0, "563-869-1049", "I have all the construction machines you need for your DIY's","Unload materials.","N/A");
    Machine data17 = new Machine(533459, "410L", 763, "2010", 1, 41.490373, -90.654335, "Clara", "Nett", "Backhoe", 2.5, 3.0, "563-179-6193", "Rent from my big farm. I have a variety of agriculture equipment.","Digging holes, moving objects, and more!","Doesn't have a roof anymore.");
    Machine data18 = new Machine(595453, "1210a", 200, "2016", 1, 41.090373, -90.112335, "Clara", "Nett", "Graincart", 2.5, 1.0, "563-179-6193", "Rent from my big farm. I have a variety of agriculture equipment.","Unloads grain into the field at a constant rate.","N/A");
    Machine data19 = new Machine(997666, "TS 4*2", 295, "2012", 3, 41.890373, -90.478335, "Clara", "Nett", "Gators", 2.5, 3.0, "563-179-6193", "Rent from my big farm. I have a variety of agriculture equipment.","All purpose 4 wheel vehicles.","N/A");
    Machine data20 = new Machine(226519, "W400", 860, "2013", 1, 41.190373, -90.592335, "Clara", "Nett", "Combine", 2.5, 4.0, "563-179-6193", "Rent from my big farm. I have a variety of agriculture equipment.","Harvest the crops out of the field.","One of the blades fell off.");
    Machine data21 = new Machine(139556, "314G", 150, "2012", 2, 41.470373, -90.468335, "Clara", "Nett", "Skid Loader", 2.5, 2.0, "563-179-6193", "Rent from my big farm. I have a variety of agriculture equipment.","Multipurpose construction equipment.","N/A");

    // Start of onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
                }
                else if (slideOffset < 0.50 ) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
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
        }
        else if (type == 2) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            snippetStr = "Construction";
        }
        else if (type == 3) {                                                                                  //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
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
        switch(requestCode) {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED) {
                        if(client == null) {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }

    // Called when the Google Map is ready to be run.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Button clearFilter = findViewById(R.id.clearFilters);
        Button agFilter = findViewById(R.id.agricultureFilter);
        Button constructionFilter = findViewById(R.id.constructionFilter);
        Button turfFilter = findViewById(R.id.turfFilter);
        ImageView arrow = findViewById(R.id.expandLessView);
        arrow.setImageResource(R.drawable.ic_expand_less_black_48dp);
        arrow.setColorFilter(getResources().getColor(R.color.johnDeereYellow));

        // Sets the map value, and sets up onInfoWindowClickListener (opening each marker's info).
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Sets all markers (can be more compact in the future).
        setMarker(data1, mMap, 1, dataMarker1);
        setMarker(data2, mMap, 2, dataMarker2);
        setMarker(data3, mMap, 3, dataMarker3);
        setMarker(data4, mMap, 4, dataMarker4);
        setMarker(data5, mMap, 5, dataMarker5);
        setMarker(data6, mMap, 6, dataMarker6);
        setMarker(data7, mMap, 7, dataMarker7);
        setMarker(data8, mMap, 8, dataMarker8);
        setMarker(data9, mMap, 9, dataMarker9);
        setMarker(data10, mMap, 10, dataMarker10);
        setMarker(data11, mMap, 11, dataMarker11);
        setMarker(data12, mMap, 12, dataMarker12);
        setMarker(data13, mMap, 13, dataMarker13);
        setMarker(data14, mMap, 14, dataMarker14);
        setMarker(data15, mMap, 15, dataMarker15);
        setMarker(data16, mMap, 16, dataMarker16);
        setMarker(data17, mMap, 17, dataMarker17);
        setMarker(data18, mMap, 18, dataMarker18);
        setMarker(data19, mMap, 19, dataMarker19);
        setMarker(data20, mMap, 20, dataMarker20);
        setMarker(data21, mMap, 21, dataMarker21);

        clearFilter.setEnabled(true);
        agFilter.setEnabled(true);
        constructionFilter.setEnabled(true);
        turfFilter.setEnabled(true);
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
        if(currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        Log.d("lat = ",""+latitude);
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        currentLocationMarker = mMap.addMarker(markerOptions);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, (com.google.android.gms.location.LocationListener) this);
        }
    }

    // For retrieving data (unused, was for testing).
    public void onClick(View v) {
        // Object dataTransfer[] = new Object[2];
        // GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch(v.getId()) {
          /*  case R.id.B_search:
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
                */
            /*case R.id.B_hopistals:

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
                */

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

    // For retrieving data from Google servers (unused).
    private String getUrl(double latitude , double longitude , String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBT5iEyi46P0X5IJUFPmeqgTe95tGVEcRY");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    public void filterMarkers(int callType, GoogleMap googleMap) {
        mMap = googleMap;
        if (callType == 0) {
            mMap.clear();
            setMarker(data1, mMap, 1, dataMarker1);
            setMarker(data2, mMap, 2, dataMarker2);
            setMarker(data3, mMap, 3, dataMarker3);
            setMarker(data4, mMap, 4, dataMarker4);
            setMarker(data5, mMap, 5, dataMarker5);
            setMarker(data6, mMap, 6, dataMarker6);
            setMarker(data7, mMap, 7, dataMarker7);
            setMarker(data8, mMap, 8, dataMarker8);
            setMarker(data9, mMap, 9, dataMarker9);
            setMarker(data10, mMap, 10, dataMarker10);
            setMarker(data11, mMap, 11, dataMarker11);
            setMarker(data12, mMap, 12, dataMarker12);
            setMarker(data13, mMap, 13, dataMarker13);
            setMarker(data14, mMap, 14, dataMarker14);
            setMarker(data15, mMap, 15, dataMarker15);
            setMarker(data16, mMap, 16, dataMarker16);
            setMarker(data17, mMap, 17, dataMarker17);
            setMarker(data18, mMap, 18, dataMarker18);
            setMarker(data19, mMap, 19, dataMarker19);
            setMarker(data20, mMap, 20, dataMarker20);
            setMarker(data21, mMap, 21, dataMarker21);
        }
        else if (callType == 1) {
            mMap.clear();
            setMarker(data5, mMap, 5, dataMarker5);
            setMarker(data17, mMap, 17, dataMarker17);
            setMarker(data18, mMap, 18, dataMarker18);
            setMarker(data20, mMap, 20, dataMarker20);
        }
        else if (callType == 2) {
            mMap.clear();
            setMarker(data1, mMap, 1, dataMarker1);
            setMarker(data6, mMap, 6, dataMarker6);
            setMarker(data7, mMap, 7, dataMarker7);
            setMarker(data8, mMap, 8, dataMarker8);
            setMarker(data10, mMap, 10, dataMarker10);
            setMarker(data11, mMap, 11, dataMarker11);
            setMarker(data12, mMap, 12, dataMarker12);
            setMarker(data13, mMap, 13, dataMarker13);
            setMarker(data14, mMap, 14, dataMarker14);
            setMarker(data16, mMap, 16, dataMarker16);
            setMarker(data21, mMap, 21, dataMarker21);
        }
        else if (callType == 3) {
            mMap.clear();
            setMarker(data2, mMap, 2, dataMarker2);
            setMarker(data3, mMap, 3, dataMarker3);
            setMarker(data4, mMap, 4, dataMarker4);
            setMarker(data9, mMap, 9, dataMarker9);
            setMarker(data15, mMap, 15, dataMarker15);
            setMarker(data19, mMap, 19, dataMarker19);
        }
    }

    // Don't worry about this (does stuff when Google Maps is connected).
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }

    // User permission for checking GPS stuff.
    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
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
            LatLng latLng = new LatLng(latitude , longitude);
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
            if (markerTag == 1) { serialNum = data1.getSerialNum(); model = data1.getModel(); hoursUsed = data1.gethUsed(); machineYear = data1.getMachineYear(); type = data1.getType(); latitude = data1.getLatitude(); longitude = data1.getLongitude(); firstName = data1.getFirstName(); lastName = data1.getLastName(); machineName = data1.getMachineName(); lenderRating = data1.getLenderRating(); machineRating = data1.getMachineRating(); lenderPhone = data1.getLenderPhone(); lenderBio = data1.getLenderBio(); machineBio = data1.getMachineBio(); machineIssues = data1.getMachineIssues(); }
            if (markerTag == 2) { serialNum = data2.getSerialNum(); model = data2.getModel(); hoursUsed = data2.gethUsed(); machineYear = data2.getMachineYear(); type = data2.getType(); latitude = data2.getLatitude(); longitude = data2.getLongitude(); firstName = data2.getFirstName(); lastName = data2.getLastName(); machineName = data2.getMachineName(); lenderRating = data2.getLenderRating(); machineRating = data2.getMachineRating(); lenderPhone = data2.getLenderPhone(); lenderBio = data2.getLenderBio(); machineBio = data2.getMachineBio(); machineIssues = data2.getMachineIssues(); }
            if (markerTag == 3) { serialNum = data3.getSerialNum(); model = data3.getModel(); hoursUsed = data3.gethUsed(); machineYear = data3.getMachineYear(); type = data3.getType(); latitude = data3.getLatitude(); longitude = data3.getLongitude(); firstName = data3.getFirstName(); lastName = data3.getLastName(); machineName = data3.getMachineName(); lenderRating = data3.getLenderRating(); machineRating = data3.getMachineRating(); lenderPhone = data3.getLenderPhone(); lenderBio = data3.getLenderBio(); machineBio = data3.getMachineBio(); machineIssues = data3.getMachineIssues(); }
            if (markerTag == 4) { serialNum = data4.getSerialNum(); model = data4.getModel(); hoursUsed = data4.gethUsed(); machineYear = data4.getMachineYear(); type = data4.getType(); latitude = data4.getLatitude(); longitude = data4.getLongitude(); firstName = data4.getFirstName(); lastName = data4.getLastName(); machineName = data4.getMachineName(); lenderRating = data4.getLenderRating(); machineRating = data4.getMachineRating(); lenderPhone = data4.getLenderPhone(); lenderBio = data4.getLenderBio(); machineBio = data4.getMachineBio(); machineIssues = data4.getMachineIssues(); }
            if (markerTag == 5) { serialNum = data5.getSerialNum(); model = data5.getModel(); hoursUsed = data5.gethUsed(); machineYear = data5.getMachineYear(); type = data5.getType(); latitude = data5.getLatitude(); longitude = data5.getLongitude(); firstName = data5.getFirstName(); lastName = data5.getLastName(); machineName = data5.getMachineName(); lenderRating = data5.getLenderRating(); machineRating = data5.getMachineRating(); lenderPhone = data5.getLenderPhone(); lenderBio = data5.getLenderBio(); machineBio = data5.getMachineBio(); machineIssues = data5.getMachineIssues(); }
            if (markerTag == 6) { serialNum = data6.getSerialNum(); model = data6.getModel(); hoursUsed = data6.gethUsed(); machineYear = data6.getMachineYear(); type = data6.getType(); latitude = data6.getLatitude(); longitude = data6.getLongitude(); firstName = data6.getFirstName(); lastName = data6.getLastName(); machineName = data6.getMachineName(); lenderRating = data6.getLenderRating(); machineRating = data6.getMachineRating(); lenderPhone = data6.getLenderPhone(); lenderBio = data6.getLenderBio(); machineBio = data6.getMachineBio(); machineIssues = data6.getMachineIssues(); }
            if (markerTag == 7) { serialNum = data7.getSerialNum(); model = data7.getModel(); hoursUsed = data7.gethUsed(); machineYear = data7.getMachineYear(); type = data7.getType(); latitude = data7.getLatitude(); longitude = data7.getLongitude(); firstName = data7.getFirstName(); lastName = data7.getLastName(); machineName = data7.getMachineName(); lenderRating = data7.getLenderRating(); machineRating = data7.getMachineRating(); lenderPhone = data7.getLenderPhone(); lenderBio = data7.getLenderBio(); machineBio = data7.getMachineBio(); machineIssues = data7.getMachineIssues(); }
            if (markerTag == 8) { serialNum = data8.getSerialNum(); model = data8.getModel(); hoursUsed = data8.gethUsed(); machineYear = data8.getMachineYear(); type = data8.getType(); latitude = data8.getLatitude(); longitude = data8.getLongitude(); firstName = data8.getFirstName(); lastName = data8.getLastName(); machineName = data8.getMachineName(); lenderRating = data8.getLenderRating(); machineRating = data8.getMachineRating(); lenderPhone = data8.getLenderPhone(); lenderBio = data8.getLenderBio(); machineBio = data8.getMachineBio(); machineIssues = data8.getMachineIssues(); }
            if (markerTag == 9) { serialNum = data9.getSerialNum(); model = data9.getModel(); hoursUsed = data9.gethUsed(); machineYear = data9.getMachineYear(); type = data9.getType(); latitude = data9.getLatitude(); longitude = data9.getLongitude(); firstName = data9.getFirstName(); lastName = data9.getLastName(); machineName = data9.getMachineName(); lenderRating = data9.getLenderRating(); machineRating = data9.getMachineRating(); lenderPhone = data9.getLenderPhone(); lenderBio = data9.getLenderBio(); machineBio = data9.getMachineBio(); machineIssues = data9.getMachineIssues(); }
            if (markerTag == 10) { serialNum = data10.getSerialNum(); model = data10.getModel(); hoursUsed = data10.gethUsed(); machineYear = data10.getMachineYear(); type = data10.getType(); latitude = data10.getLatitude(); longitude = data10.getLongitude(); firstName = data10.getFirstName(); lastName = data10.getLastName(); machineName = data10.getMachineName(); lenderRating = data10.getLenderRating(); machineRating = data10.getMachineRating(); lenderPhone = data10.getLenderPhone(); lenderBio = data10.getLenderBio(); machineBio = data10.getMachineBio(); machineIssues = data10.getMachineIssues(); }
            if (markerTag == 11) { serialNum = data11.getSerialNum(); model = data11.getModel(); hoursUsed = data11.gethUsed(); machineYear = data11.getMachineYear(); type = data11.getType(); latitude = data11.getLatitude(); longitude = data11.getLongitude(); firstName = data11.getFirstName(); lastName = data11.getLastName(); machineName = data11.getMachineName(); lenderRating = data11.getLenderRating(); machineRating = data11.getMachineRating(); lenderPhone = data11.getLenderPhone(); lenderBio = data11.getLenderBio(); machineBio = data11.getMachineBio(); machineIssues = data11.getMachineIssues(); }
            if (markerTag == 12) { serialNum = data12.getSerialNum(); model = data12.getModel(); hoursUsed = data12.gethUsed(); machineYear = data12.getMachineYear(); type = data12.getType(); latitude = data12.getLatitude(); longitude = data12.getLongitude(); firstName = data12.getFirstName(); lastName = data12.getLastName(); machineName = data12.getMachineName(); lenderRating = data12.getLenderRating(); machineRating = data12.getMachineRating(); lenderPhone = data12.getLenderPhone(); lenderBio = data12.getLenderBio(); machineBio = data12.getMachineBio(); machineIssues = data12.getMachineIssues(); }
            if (markerTag == 13) { serialNum = data13.getSerialNum(); model = data13.getModel(); hoursUsed = data13.gethUsed(); machineYear = data13.getMachineYear(); type = data13.getType(); latitude = data13.getLatitude(); longitude = data13.getLongitude(); firstName = data13.getFirstName(); lastName = data13.getLastName(); machineName = data13.getMachineName(); lenderRating = data13.getLenderRating(); machineRating = data13.getMachineRating(); lenderPhone = data13.getLenderPhone(); lenderBio = data13.getLenderBio(); machineBio = data13.getMachineBio(); machineIssues = data13.getMachineIssues(); }
            if (markerTag == 14) { serialNum = data14.getSerialNum(); model = data14.getModel(); hoursUsed = data14.gethUsed(); machineYear = data14.getMachineYear(); type = data14.getType(); latitude = data14.getLatitude(); longitude = data14.getLongitude(); firstName = data14.getFirstName(); lastName = data14.getLastName(); machineName = data14.getMachineName(); lenderRating = data14.getLenderRating(); machineRating = data14.getMachineRating(); lenderPhone = data14.getLenderPhone(); lenderBio = data14.getLenderBio(); machineBio = data14.getMachineBio(); machineIssues = data14.getMachineIssues(); }
            if (markerTag == 15) { serialNum = data15.getSerialNum(); model = data15.getModel(); hoursUsed = data15.gethUsed(); machineYear = data15.getMachineYear(); type = data15.getType(); latitude = data15.getLatitude(); longitude = data15.getLongitude(); firstName = data15.getFirstName(); lastName = data15.getLastName(); machineName = data15.getMachineName(); lenderRating = data15.getLenderRating(); machineRating = data15.getMachineRating(); lenderPhone = data15.getLenderPhone(); lenderBio = data15.getLenderBio(); machineBio = data15.getMachineBio(); machineIssues = data15.getMachineIssues(); }
            if (markerTag == 16) { serialNum = data16.getSerialNum(); model = data16.getModel(); hoursUsed = data16.gethUsed(); machineYear = data16.getMachineYear(); type = data16.getType(); latitude = data16.getLatitude(); longitude = data16.getLongitude(); firstName = data16.getFirstName(); lastName = data16.getLastName(); machineName = data16.getMachineName(); lenderRating = data16.getLenderRating(); machineRating = data16.getMachineRating(); lenderPhone = data16.getLenderPhone(); lenderBio = data16.getLenderBio(); machineBio = data16.getMachineBio(); machineIssues = data16.getMachineIssues(); }
            if (markerTag == 17) { serialNum = data17.getSerialNum(); model = data17.getModel(); hoursUsed = data17.gethUsed(); machineYear = data17.getMachineYear(); type = data17.getType(); latitude = data17.getLatitude(); longitude = data17.getLongitude(); firstName = data17.getFirstName(); lastName = data17.getLastName(); machineName = data17.getMachineName(); lenderRating = data17.getLenderRating(); machineRating = data17.getMachineRating(); lenderPhone = data17.getLenderPhone(); lenderBio = data17.getLenderBio(); machineBio = data17.getMachineBio(); machineIssues = data17.getMachineIssues(); }
            if (markerTag == 18) { serialNum = data18.getSerialNum(); model = data18.getModel(); hoursUsed = data18.gethUsed(); machineYear = data18.getMachineYear(); type = data18.getType(); latitude = data18.getLatitude(); longitude = data18.getLongitude(); firstName = data18.getFirstName(); lastName = data18.getLastName(); machineName = data18.getMachineName(); lenderRating = data18.getLenderRating(); machineRating = data18.getMachineRating(); lenderPhone = data18.getLenderPhone(); lenderBio = data18.getLenderBio(); machineBio = data18.getMachineBio(); machineIssues = data18.getMachineIssues(); }
            if (markerTag == 19) { serialNum = data19.getSerialNum(); model = data19.getModel(); hoursUsed = data19.gethUsed(); machineYear = data19.getMachineYear(); type = data19.getType(); latitude = data19.getLatitude(); longitude = data19.getLongitude(); firstName = data19.getFirstName(); lastName = data19.getLastName(); machineName = data19.getMachineName(); lenderRating = data19.getLenderRating(); machineRating = data19.getMachineRating(); lenderPhone = data19.getLenderPhone(); lenderBio = data19.getLenderBio(); machineBio = data19.getMachineBio(); machineIssues = data19.getMachineIssues(); }
            if (markerTag == 20) { serialNum = data20.getSerialNum(); model = data20.getModel(); hoursUsed = data20.gethUsed(); machineYear = data20.getMachineYear(); type = data20.getType(); latitude = data20.getLatitude(); longitude = data20.getLongitude(); firstName = data20.getFirstName(); lastName = data20.getLastName(); machineName = data20.getMachineName(); lenderRating = data20.getLenderRating(); machineRating = data20.getMachineRating(); lenderPhone = data20.getLenderPhone(); lenderBio = data20.getLenderBio(); machineBio = data20.getMachineBio(); machineIssues = data20.getMachineIssues(); }
            if (markerTag == 21) { serialNum = data21.getSerialNum(); model = data21.getModel(); hoursUsed = data21.gethUsed(); machineYear = data21.getMachineYear(); type = data21.getType(); latitude = data21.getLatitude(); longitude = data21.getLongitude(); firstName = data21.getFirstName(); lastName = data21.getLastName(); machineName = data21.getMachineName(); lenderRating = data21.getLenderRating(); machineRating = data21.getMachineRating(); lenderPhone = data21.getLenderPhone(); lenderBio = data21.getLenderBio(); machineBio = data21.getMachineBio(); machineIssues = data21.getMachineIssues(); }

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