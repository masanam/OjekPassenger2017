package ojekkeren.ojekkeren;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class GoFoodDeliveryChooserToMap extends ActionBarActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener  {

    private MapView mapView;
    private GoogleMap mMap;
    private LatLng oldLatLng;
    private GoogleApiClient mGoogleApiClient;
    private GeocodePojoClass geocodePojoClasses;
    private TextView notifyPlace;
    private TextView pilihAlamat;
    private TextView latlng;
    private TextView latnya;
    private TextView longnya;

    @Override
    public void onLocationChanged(android.location.Location location) {
        if(oldLatLng != null){
            mMap.addMarker(new MarkerOptions().position(oldLatLng)).remove();
        }else {
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            oldLatLng = userLocation;
//            mMap.addMarker(new MarkerOptions().position(userLocation).title("It's Me!"));i
            /*if(oneTimeDetect == false)
            {
                oneTimeDetect = true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
            }*/


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker_destination);
        pilihAlamat = (TextView) findViewById(R.id.pilihalamat);
        latlng = (TextView) findViewById(R.id.latlng);

        latnya = (TextView) findViewById(R.id.theLat);
        longnya = (TextView) findViewById(R.id.theLong);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Check if we were successful in obtaining the map.
        if (mMap != null) {


            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, this);
            Criteria criteria = new Criteria();
            String provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            android.location.Location location = locationManager.getLastKnownLocation(provider);

            if (location == null ) {
                Toast.makeText(this, "Geo Coder Not Avaiable", Toast.LENGTH_LONG).show();
            }else{
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));
            }

        }

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

                if(latlng.getText().toString().equals("")){
                    /*AlertDialog.Builder adb = new AlertDialog.Builder(getApplicationContext());
                    adb.setTitle("Destination Not Complete..");
                    adb.setMessage("Please wait until Maps Google Geocoding return Address as a result.. ");
                    adb.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    adb.show();
*/
                    Toast.makeText(getApplicationContext(), "Please wait until Maps Google Geocoding return Address as a result", Toast.LENGTH_LONG).show();
                }else{
                    Intent getDest = getIntent();
                    FoodCarts foodCarts = getDest.getExtras().getParcelable("foodCart");


                    Intent intent = new Intent(GoFoodDeliveryChooserToMap.this, GoFoodDelivery.class);
                    intent.putExtra("foodCart", foodCarts);
                    String message = pilihAlamat.getText().toString();
                    overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
                    intent.putExtra("ALAMAT_DESTINASI", message);
                    intent.putExtra("LATLNG_DEST", latlng.getText());
                    intent.putExtra("THE_LAT_DEST", latnya.getText());
                    intent.putExtra("THE_LONG_DEST", longnya.getText());
                    intent.putExtra("FROMTEXT", getDest.getStringExtra("fromText"));

                    intent.putExtra("ADDFROMTEXT", getDest.getStringExtra("fromAddText"));
                    intent.putExtra("ADDTOTEXT", getDest.getStringExtra("destAddText"));
                    intent.putExtra("ITEMDELIVER", getDest.getStringExtra("itemDeliverIntentExtra"));

                    Intent currentIntent = getIntent();
                    intent.putExtra("tokoName", currentIntent.getStringExtra("tokoName"));
                    intent.putExtra("THE_LAT_FROM", currentIntent.getStringExtra("THE_LAT_FROM"));
                    intent.putExtra("THE_LONG_FROM", currentIntent.getStringExtra("THE_LONG_FROM"));

                    intent.putExtra("lat", currentIntent.getStringExtra("lat"));
                    intent.putExtra("lng", currentIntent.getStringExtra("lng"));
                    intent.putExtra("idToko", currentIntent.getStringExtra("idToko"));

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
                        notifyPlace.setText(dapatAlamat[0]+ "," + dapatAlamat[1] + "," + dapatAlamat[2] + "," + dapatAlamat[3]);
                        notifyPlace.setAlpha((float) 0.8);
                        pilihAlamat.setText(dapatAlamat[0]+ "," + dapatAlamat[1] + "," + dapatAlamat[2] + "," + dapatAlamat[3]);
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
}
