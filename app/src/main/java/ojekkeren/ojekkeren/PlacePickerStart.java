package ojekkeren.ojekkeren;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.*;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlacePickerStart extends ActionBarActivity implements LocationListener, OnMapReadyCallback, OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener  {

    private LatLng oldLatLng;
    private MapView mapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private GeocodePojoClass geocodePojoClasses;
    private TextView notifyPlace;
    private TextView pilihAlamat;
    private TextView latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_place_auto_complete_maps);
        pilihAlamat = (TextView) findViewById(R.id.pilihalamat);
        latlng = (TextView) findViewById(R.id.latlng);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API).build();

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
//                Log.w("Place", "Place: " + place.getName());
                /*mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title((String) place.getName()));*/
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14));
            }

            @Override
            public void onError(Status status) {

            }
        });


        ImageView pilihLokasi = (ImageView) findViewById(R.id.pakaiLokasi);
        pilihLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getDest = getIntent();
                Intent intent = new Intent(PlacePickerStart.this, GoRideMenu.class);
                String message = pilihAlamat.getText().toString();
                overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
                intent.putExtra("ALAMAT_BERANGKAT_DARI", message);
                intent.putExtra("LATLNG_FROM", latlng.getText().toString());


                intent.putExtra("DESTTEXT", getDest.getStringExtra("destText"));
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
        Criteria criteria = new Criteria();
        String provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
        android.location.Location location = locationManager.getLastKnownLocation(provider);

        LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());*/
        LatLng sydney = new LatLng(-6.121435, 106.774124);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        notifyPlace = (TextView) findViewById(R.id.infoLocation);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                //Creating a rest adapter
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint("http://maps.googleapis.com/")
                        .build();

                //Creating an object of our api interface
                GeoCodeAPI api = adapter.create(GeoCodeAPI.class);

                //Defining the method
                api.getAddressName(cameraPosition.target.latitude + "," + cameraPosition.target.longitude, new Callback<GeocodePojoClass>() {

                    @Override
                    public void success(GeocodePojoClass geocodePojoClasses, Response response) {
                        String[] dapatAlamat = geocodePojoClasses.getResults().get(0).getFormatted_address().split(",");
                        Double latx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLat();
                        Double longx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLng();
                        notifyPlace.setPadding(10, 10, 10, 70);
                        notifyPlace.setBackgroundColor(0xffffffff);
                        notifyPlace.setText(dapatAlamat[0]);
                        notifyPlace.setAlpha((float) 0.8);
                        pilihAlamat.setText(dapatAlamat[0]);
                        latlng.setText(latx + "," + longx);
                        Log.w("Body ", geocodePojoClasses.getResults().get(0).getFormatted_address());
                       /* response.toString();
                        Log.w("Body ",geocodePojoClasses.getResults().get(0).getFormatted_address());*/
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //you can handle the errors here
                        Log.w("GEOCODE ERROR", error.getMessage());
                    }
                });


            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.w("On Location Changes : ",String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
        if(oldLatLng != null){
            mMap.addMarker(new MarkerOptions().position(oldLatLng)).remove();
        }else {
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            oldLatLng = userLocation;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Activity activity;
        activity = this;
        this.finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PlacePickerStart Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ojekkeren.ojekkeren/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PlacePickerStart Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ojekkeren.ojekkeren/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }
}
