package ojekkeren.ojekkeren;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Handler;

public class FinalConfirmationOrder extends ActionBarActivity  implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_confirmation_order);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

        TextView gobackindex = (TextView) findViewById(R.id.gobackindex);
        gobackindex.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        ImageView imageComplete = (ImageView) findViewById(R.id.imageComplete);
        imageComplete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), StarRatingDriver.class);
                    startActivity(myIntent);
                }
               // Intent intent = new Intent(v.getContext(), StarRatingDriver.class);
               // Toast.makeText(FinalConfirmationOrder.this, "You clicked on ImageView", Toast.LENGTH_LONG).show();
        });

        ImageView imageCancel = (ImageView) findViewById(R.id.imageCancel);
        imageCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
            // Intent intent = new Intent(v.getContext(), StarRatingDriver.class);
            // Toast.makeText(FinalConfirmationOrder.this, "You clicked on ImageView", Toast.LENGTH_LONG).show();
        });




        final DBOrder dbOrder = new DBOrder(FinalConfirmationOrder.this);
        final OrderPojo orderPojo = dbOrder.getTheLatestOrderData();

        String base_url = "http://maps.googleapis.com/";

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        DirectionInterfaceREST reqinterface = restAdapter.create(DirectionInterfaceREST.class);

        String id = String.valueOf(orderPojo.getId());
        String status = orderPojo.getStatus();
        final String driverNik = orderPojo.getDriverid();
        String alamat = orderPojo.getFrom();

        final Intent intent = getIntent();
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("driverNik", driverNik));
        params.add(new BasicNameValuePair("status", status));
        params.add(new BasicNameValuePair("From", alamat));
        Log.w("param", String.valueOf(params));

        reqinterface.getJson(orderPojo.getFrom(), orderPojo.getTo(), new Callback<DirectionResultPojos>() {


            @Override
            public void success(DirectionResultPojos directionResultPojos, Response response) {
                mMap.clear();
                ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                double startLat = 0, startLong = 0;
                double endLat = 0, endLong = 0;
                String theDuration = null;

                if (directionResultPojos.getRoutes().size() > 0) {
                    ArrayList<LatLng> decodelist;
                    Route routeA = directionResultPojos.getRoutes().get(0);

                    if (routeA.getLegs().size() > 0) {
                        List<Steps> steps = routeA.getLegs().get(0).getSteps();
                        Steps step;
                        ojekkeren.ojekkeren.Location location;
                        String polyline;
                        theDuration = String.valueOf(routeA.getLegs().get(0).getDuration().getText());
                        for (int i = 0; i < steps.size(); i++) {

                            step = steps.get(i);
                            location = step.getStart_location();
                            if (i == 0) {
                                startLat = location.getLat();
                                startLong = location.getLng();
                            }
                            routelist.add(new LatLng(location.getLat(), location.getLng()));
                            polyline = step.getPolyline().getPoints();
                            decodelist = RouteDecode.decodePoly(polyline);
                            routelist.addAll(decodelist);
                            location = step.getEnd_location();
                            routelist.add(new LatLng(location.getLat(), location.getLng()));

                            endLat = location.getLat();
                            endLong = location.getLng();
                        }
                    }
                }

                if (routelist.size() > 0) {
                    PolylineOptions rectLine = new PolylineOptions().width(7).color(Color.GREEN);

                    for (int i = 0; i < routelist.size(); i++) {
                        rectLine.add(routelist.get(i));
                    }

                    mMap.addPolyline(rectLine);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(startLat, startLong)).title("Estimate Time").snippet(theDuration).icon(BitmapDescriptorFactory.fromResource(R.drawable.mbike)));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(endLat, endLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.redflag)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLat, startLong), 13));

                    try {
                        GMailSender sender = new GMailSender("masanam@gmail.com", "masanamoke");
                        sender.sendMail("Completed Order",
                                "Thank You for Your Order",
                                "masanam@gmail.com",
                                "masanam@yahoo.com");
                        Toast.makeText(FinalConfirmationOrder.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                        Toast.makeText(FinalConfirmationOrder.this, "There was a problem   sending the email.", Toast.LENGTH_LONG).show();
                    }

                    showDriversLoc();
                    new DapatkanData().execute();



                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }



    public void showDriversLoc(){
        String base_url = getServerAPIServer();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        DriverLocationRest reqinterface = restAdapter.create(DriverLocationRest.class);

        reqinterface.getAll("getDriversAround",new Callback<List<DriverLocationPojo>>() {

            @Override
            public void success(List<DriverLocationPojo> driverLocationPojo, Response response) {
                for(int i=0 ; i<driverLocationPojo.size();i++){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(driverLocationPojo.get(i).getLat()), Double.parseDouble(driverLocationPojo.get(i).getLng()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.mcycle)));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "FinalConfirmationOrder Page", // TODO: Define a title for the content shown.
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
                "FinalConfirmationOrder Page", // TODO: Define a title for the content shown.
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

// test//

    /**
     * Background Async Task to  Save product Details
     * */
   public class DapatkanData extends AsyncTask<String, String, String> {

        // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;

        final TextView textView8 = (TextView) findViewById(R.id.textView8);
        final TextView txtDriverInfo = (TextView) findViewById(R.id.DriverInfo);
        final LinearLayout btnCancel = (LinearLayout) findViewById(R.id.callLay);

        ProgressDialog pdLoading = new ProgressDialog(FinalConfirmationOrder.this);
        HttpURLConnection conn;
        URL url = null;
        Integer driverid;
        Integer status;
        Integer pid;
        String drivername;
        String nopol;
        String merkkendaraan;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://webtvasia.id/dashboard/api.php?task=getOrdersLast");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread

            pdLoading.dismiss();

            try {

                JSONArray jArray = new JSONArray(result);

                Log.d("JSON result", jArray.toString());
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    pid= json_data.getInt("orderid");
                    driverid = json_data.getInt("driverid");
                    status= json_data.getInt("status");
                    drivername= json_data.getString("drivername");
                    nopol = json_data.getString("nomorlain");
                    merkkendaraan= json_data.getString("merkkendaraan");
                }

                Log.d("JSON result", driverid.toString());

                if (driverid > 0 ){
                    txtDriverInfo.setVisibility(View.VISIBLE);
                    txtDriverInfo.setAlpha((float) 0.8);
                    txtDriverInfo.setText("Your Driver : \n"+ driverid.toString()+" - "+drivername+"\n"+merkkendaraan+" - "+nopol);
                    btnCancel.setVisibility(View.VISIBLE);

                    textView8.setText("Your driver "+drivername+" coming on 12 minutes ");
                } else
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new DapatkanData().execute();
                            Toast.makeText(FinalConfirmationOrder.this,"Trying to find the driver",Toast.LENGTH_SHORT).show();
                        }
                    }, 5000);
                }


            } catch (JSONException e) {
                Toast.makeText(FinalConfirmationOrder.this, e.toString(), Toast.LENGTH_LONG).show();

            }

        }

    }

    // test//

    public String getServerAPIServer(){
        Context context = getApplicationContext();
        String ServerAPI;
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ServerAPI = ai.metaData.get("ServerAPI").toString();
        return ServerAPI;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DBAccount dbAccount = new DBAccount(this);

        if(dbAccount.getCurrentMemberDetails().getIsLogged() != null){
            MemberAkun akunLoggedCheck = dbAccount.getCurrentMemberDetails();

            Log.w("MENU STAT",akunLoggedCheck.getIsLogged());
            if(akunLoggedCheck.getIsLogged().equals("0"))
            {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                }
            }else if(akunLoggedCheck.getIsLogged().equals("1")) {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(true);
                    menu.findItem(R.id.actionLogin).setVisible(false);
                    Log.w("Logged ",akunLoggedCheck.getIsLogged());
                }
            }else{
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                }
            }
        }else{
            if (menu != null) {
                menu.findItem(R.id.actionLogout).setVisible(false);
                menu.findItem(R.id.actionLogin).setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingAtivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.actionLogout){
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(id == R.id.actionLogin){
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
