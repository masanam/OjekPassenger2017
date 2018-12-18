package ojekkeren.ojekkeren;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class ApiRequestActivity extends ActionBarActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Context context;

    private LatLng oldLatLng;

    ArrayList<LatLng> markerPoints;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton currentlocation = (FloatingActionButton) findViewById(R.id.currentlocation);
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        currentlocation.setImageDrawable(getResources().getDrawable(R.drawable.btn_use_current_location));

        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        acTextView.setAdapter(new SuggestionAdapter(this, acTextView.getText().toString()));

        acTextView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long id) {
                final String selection = (String) arg0.getItemAtPosition(index);

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    String newUrl = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD-OkGXSpHVQJdvwZ7H4k_ziof5NW-Vx74&sensor=false&address=" + selection;
                    new GeoCodeGetLatLang().execute(newUrl);
                } else {
                    Log.w("No Network ", "No Network Available");
                }

            }
        });

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.setMyLocationEnabled(true);

        // Check if we were successful in obtaining the map.
        if (mMap != null) {

            mMap.clear();
            //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Criteria criteria = new Criteria();
            String provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            Location location = locationManager.getLastKnownLocation(provider);*/

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));
            } else {
                Toast.makeText(this, "GPS unavailable", Toast.LENGTH_LONG).show();
                /*LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));*/
            }

            /*if (location == null) {
                Toast.makeText(this, "Geo Coder Not Avaiable", Toast.LENGTH_LONG).show();
            }*/



        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ApiRequest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ojekkeren.ojekkeren/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ApiRequest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ojekkeren.ojekkeren/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class GeoCodeGetLatLang extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(urls[0], new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        Log.w("results ", String.valueOf(jsonArray.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return "True";
        }


    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (oldLatLng != null) {
            mMap.addMarker(new MarkerOptions().position(oldLatLng)).remove();
        } else {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            oldLatLng = userLocation;
            mMap.addMarker(new MarkerOptions().position(userLocation).title("It's Me!").snippet("Population: 4,137,400")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.burger)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public class SuggestionAdapter extends ArrayAdapter<String> {

        protected static final String TAG = "SuggestionAdapter";
        private List<String> suggestions;

        public SuggestionAdapter(Activity context, String nameFilter) {
            super(context, android.R.layout.simple_dropdown_item_1line);
            suggestions = new ArrayList<String>();
        }

        @Override
        public int getCount() {
            return suggestions.size();
        }

        @Override
        public String getItem(int index) {
            return suggestions.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        String search = constraint.toString().replace(" ", "%20");
                        try {
//                            URL js = new URL("http://192.168.1.105/jobbers/api.php?search="+temp);
                            URL js = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyD-OkGXSpHVQJdvwZ7H4k_ziof5NW-Vx74&input=" + search);
                            URLConnection jc = js.openConnection();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                            StringBuilder result = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            JSONObject jsonResponse = new JSONObject(result.toString());
                            JSONArray jsonArray = jsonResponse.getJSONArray("predictions");
                            suggestions.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject r = jsonArray.getJSONObject(i);
                                suggestions.add(r.getString("description"));
                            }
                            filterResults.values = suggestions;
                            filterResults.count = suggestions.size();
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence contraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return myFilter;
        }

    }

}

