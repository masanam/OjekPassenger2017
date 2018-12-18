package ojekkeren.ojekkeren;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

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

public class PlacePickerDestination extends ActionBarActivity implements OnMapReadyCallback, OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener  {

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
        setContentView(R.layout.activity_place_picker_destination);
        pilihAlamat = (TextView) findViewById(R.id.pilihalamat);
        latlng = (TextView) findViewById(R.id.latlng);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14));
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
                if(getDest.getStringExtra("isGoSend") != null){
                    if(getDest.getStringExtra("isGoSend") == "1")
                    {
                        Intent intent = new Intent(PlacePickerDestination.this, GoSendActivity.class);
                        String message = pilihAlamat.getText().toString();
                        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
                        intent.putExtra("ALAMAT_DESTINASI", message);
                        intent.putExtra("LATLNG_DEST", latlng.getText().toString());

                        intent.putExtra("FROMTEXT", getDest.getStringExtra("fromText"));
                        startActivity(intent);
                        finish();
                    }
                }else
                {
                    Intent intent = new Intent(PlacePickerDestination.this, GoRideMenu.class);
                    String message = pilihAlamat.getText().toString();
                    overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
                    intent.putExtra("ALAMAT_DESTINASI", message);
                    intent.putExtra("LATLNG_DEST", latlng.getText().toString());

                    intent.putExtra("FROMTEXT", getDest.getStringExtra("fromText"));
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Jakarta, and move the camera.
        LatLng sydney = new LatLng(-6.121435, 106.774124);
        /*mMap.addMarker(new MarkerOptions().position(sydney).snippet("Description"));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

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
                api.getAddressName(cameraPosition.target.latitude+","+cameraPosition.target.longitude, new Callback<GeocodePojoClass>() {

                    @Override
                    public void success( GeocodePojoClass geocodePojoClasses, Response response) {
                        String[] dapatAlamat = geocodePojoClasses.getResults().get(0).getFormatted_address().split(",");
                        Double latx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLat();
                        Double longx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLng();
                        notifyPlace.setPadding(10, 10, 10, 70);
                        notifyPlace.setBackgroundColor(0xffffffff);
                        notifyPlace.setText(dapatAlamat[0]);
                        notifyPlace.setAlpha((float) 0.8);
                        pilihAlamat.setText(dapatAlamat[0]);
                        latlng.setText(latx+","+longx);
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
        super.onBackPressed();finish();
    }
}
