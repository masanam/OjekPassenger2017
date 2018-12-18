package ojekkeren.ojekkeren;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.*;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlacePickerRideEnd extends ActionBarActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener  {

    private MapView mapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private GeocodePojoClass geocodePojoClasses;
    private TextView notifyPlace;
    private TextView pilihAlamat;
    private TextView latlng;
    private TextView latnya;
    private TextView longnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker_ride_end);
        pilihAlamat = (TextView) findViewById(R.id.pilihalamat);
        latlng = (TextView) findViewById(R.id.latlng);

        latnya = (TextView) findViewById(R.id.theLat);
        longnya = (TextView) findViewById(R.id.theLong);


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API).build();

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
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
                Intent intent = new Intent(PlacePickerRideEnd.this, GoRideMenu.class);
                String message = pilihAlamat.getText().toString();
                overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
                intent.putExtra("ALAMAT_DESTINASI", message);
                intent.putExtra("LATLNG_DEST", latlng.getText());
                intent.putExtra("THE_LAT_DEST", latnya.getText());
                intent.putExtra("THE_LONG_DEST", longnya.getText());
                intent.putExtra("FROMTEXT", getDest.getStringExtra("fromText"));

                intent.putExtra("ADDFROMTEXT", getDest.getStringExtra("fromAddText"));
                intent.putExtra("ADDTOTEXT", getDest.getStringExtra("destAddText"));

                Intent currentIntent = getIntent();
                intent.putExtra("THE_LAT_FROM", currentIntent.getStringExtra("THE_LAT_FROM"));
                intent.putExtra("THE_LONG_FROM", currentIntent.getStringExtra("THE_LONG_FROM"));

                startActivity(intent);
                finish();

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Jakarta, and move the camera.
       // LatLng sydney = new LatLng(-6.202008, 106.829135);
        /*mMap.addMarker(new MarkerOptions().position(sydney).snippet("Description"));
        *         LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        Criteria criteria = new Criteria();
        String provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
        android.location.Location location = locationManager.getLastKnownLocation(provider);

        LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));        * */
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        android.location.Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(-6.202008, 106.829135), 13));
        }

        notifyPlace = (TextView) findViewById(R.id.infoLocation);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                notifyPlace.setText("Loading..");
                notifyPlace.setText("Loading....");
                notifyPlace.setText("Loading......");

                //Creating a rest adapter
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint("http://maps.googleapis.com/")
                        .build();

                //Creating an object of our api interface
                GeoCodeAPI api = adapter.create(GeoCodeAPI.class);

                //Defining the method
                api.getAddressName(cameraPosition.target.latitude+","+cameraPosition.target.longitude, new Callback<GeocodePojoClass>() {

                    @Override
                    public void success( GeocodePojoClass geocodePojoClasses, Response response) {
                        String[] dapatAlamat = geocodePojoClasses.getResults().get(0).getFormatted_address().split(",");
                        double latx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLat();
                        double longx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLng();
                        notifyPlace.setPadding(10, 10, 10, 70);
                        notifyPlace.setBackgroundColor(0xffffffff);
                        notifyPlace.setText(dapatAlamat[0] + "," + dapatAlamat[3]);
                        notifyPlace.setAlpha((float) 0.8);
                        pilihAlamat.setText(dapatAlamat[0] + "," + dapatAlamat[3]);
                        latlng.setText(latx+","+longx);
                        latnya.setText(String.valueOf(latx));
                        longnya.setText(String.valueOf(longx));
                        Log.w("LAT DAN LONG ", latnya.getText().toString() + ", " + longnya.getText().toString());
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

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
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
        super.onBackPressed();finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PlacePickerRideEnd Page", // TODO: Define a title for the content shown.
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
                "PlacePickerRideEnd Page", // TODO: Define a title for the content shown.
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

    @Override
    public void onLocationChanged(Location location) {

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
}
