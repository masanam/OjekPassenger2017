package ojekkeren.ojekkeren;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GoSendActivity extends ActionBarActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private String lat,lng;
    private LatLng oldLatLng;
    private String chooserFromAddress;
    private String chooserToAddress;
    private ProgressDialog pDialog;
    private String theDistance;
    private double thePrice;
    private boolean oneTimeDetect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_send);


        final EditText myEditTextFrom = (EditText) findViewById(R.id.placePickerStart);
        final EditText myEditTextTo = (EditText) findViewById(R.id.placePickerDestination);

        final EditText myEditTextFromAddress = (EditText) findViewById(R.id.myEditTextFromAddress);
        final EditText myEditTextToAddress = (EditText) findViewById(R.id.myEditTextToAddress);
        final EditText myEditItemToDeliver = (EditText) findViewById(R.id.itemToDeliver);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.setMyLocationEnabled(true);

        Intent intent = getIntent();

        if(intent.getStringExtra("ALAMAT_BERANGKAT_DARI")!= null){
            myEditTextFrom.setText(intent.getStringExtra("ALAMAT_BERANGKAT_DARI"));

            /*ini dari fromChooose jadi harus set intent simpan yang DEST */
            myEditTextTo.setText(intent.getStringExtra("DESTTEXT"));

            myEditTextFromAddress.setText(intent.getStringExtra("ADDFROMTEXT"));
            myEditTextToAddress.setText(intent.getStringExtra("ADDTOTEXT"));
            myEditItemToDeliver.setText(intent.getStringExtra("ITEMDELIVER"));

        }

        if(intent.getStringExtra("ALAMAT_DESTINASI")!= null){
            myEditTextTo.setText(intent.getStringExtra("ALAMAT_DESTINASI"));

            /*ini dari destChoose jadi harus set intent simpan yang FROM */
            myEditTextFrom.setText(intent.getStringExtra("FROMTEXT"));

            myEditTextFromAddress.setText(intent.getStringExtra("ADDFROMTEXT"));
            myEditTextToAddress.setText(intent.getStringExtra("ADDTOTEXT"));
            myEditItemToDeliver.setText(intent.getStringExtra("ITEMDELIVER"));
        }

        showDriversLoc();





        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        TextView header_from = (TextView) findViewById(R.id.header_from);




        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sansation_Regular.ttf");
        header_from.setTypeface(face);



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


        myEditTextFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlacePickerSendActStart.class);
                overridePendingTransition(R.anim.fab_in, R.anim.fab_out);
                intent.putExtra("destText", myEditTextTo.getText().toString());
                /*pegang from dan to nya*/
                intent.putExtra("fromAddText", myEditTextFromAddress.getText().toString());
                intent.putExtra("destAddText", myEditTextToAddress.getText().toString());
                intent.putExtra("itemDeliverIntentExtra", myEditItemToDeliver.getText().toString());

                /*harus membawa details DEST karena mauFROM akan muncul dna bawa intent*/
                Intent currentIntent = getIntent();
                if(currentIntent.getStringExtra("THE_LAT_DEST") != null){
                    intent.putExtra("THE_LAT_DEST", currentIntent.getStringExtra("THE_LAT_DEST"));
                    intent.putExtra("THE_LONG_DEST", currentIntent.getStringExtra("THE_LONG_DEST"));
                }
                startActivity(intent);
            }
        });

        myEditTextTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlacePickerSendEndAct.class);
                overridePendingTransition(R.anim.fab_in, R.anim.fab_out);
                intent.putExtra("fromText", myEditTextFrom.getText().toString());
                /*pegang from dan to nya*/
                intent.putExtra("fromAddText", myEditTextFromAddress.getText().toString());
                intent.putExtra("destAddText", myEditTextToAddress.getText().toString());
                intent.putExtra("itemDeliverIntentExtra", myEditItemToDeliver.getText().toString());

                 /*harus membawa details DEST karena mauFROM akan muncul dna bawa intent*/
                Intent currentIntent = getIntent();
                if(currentIntent.getStringExtra("THE_LAT_FROM") != null){
                    intent.putExtra("THE_LAT_FROM", currentIntent.getStringExtra("THE_LAT_FROM"));
                    intent.putExtra("THE_LONG_FROM", currentIntent.getStringExtra("THE_LONG_FROM"));
                }
                startActivity(intent);
            }
        });

        ImageButton nextlogin = (ImageButton) findViewById(R.id.nextlogin);
        nextlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                overridePendingTransition(R.anim.fab_in, R.anim.fab_out);

                EditText itemToDeliver = (EditText) findViewById(R.id.itemToDeliver);
                final EditText myEditTextFrom = (EditText) findViewById(R.id.placePickerStart);
                final EditText myEditTextTo = (EditText) findViewById(R.id.placePickerDestination);
                final EditText myEditTextFromAddress = (EditText) findViewById(R.id.myEditTextFromAddress);
                final EditText myEditTextToAddress = (EditText) findViewById(R.id.myEditTextToAddress);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                if (!itemToDeliver.getText().toString().matches("")) {

                    itemToDeliver = (EditText) findViewById(R.id.itemToDeliver);

                    Intent currentIntent = getIntent();
                    OrderPojo orderPojo = new OrderPojo();
                    orderPojo.setAddressFrom(myEditTextFromAddress.getText().toString());
                    orderPojo.setAddressTo(myEditTextToAddress.getText().toString());
                    orderPojo.setFrom(myEditTextFrom.getText().toString());
                    orderPojo.setTo(myEditTextTo.getText().toString());
                    orderPojo.setLatFrom(currentIntent.getStringExtra("THE_LAT_FROM"));
                    orderPojo.setLongFrom(currentIntent.getStringExtra("THE_LONG_FROM"));
                    orderPojo.setLatTo(currentIntent.getStringExtra("THE_LAT_DEST"));
                    orderPojo.setLongTo(currentIntent.getStringExtra("THE_LONG_DEST"));
                    orderPojo.setItemToDeliver(itemToDeliver.getText().toString());
                    orderPojo.setDistance(theDistance);
                    orderPojo.setPrice(String.valueOf(thePrice));

                    DBAccount dbAccountConn = new DBAccount(GoSendActivity.this);

                    if(dbAccountConn.getCurrentMemberDetails().getIsLogged() != null){
                        MemberAkun memberAkunGetPhone = dbAccountConn.getCurrentMemberDetails();

                        if(memberAkunGetPhone.getIsLogged().equals("1"))
                        {
                            orderPojo.setPhoneNum(memberAkunGetPhone.getPhoneNumber());
                        }else{
                            orderPojo.setPhoneNum("0");
                        }
                    }

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    orderPojo.setOrderTime(dateFormat.format(date));

                    /*Send Doc  = 2*/
                    orderPojo.setType("2");

                    /*save on this temporary*/
                    orderPojo.setStatus("3");

                    DBOrder dbOrder = new DBOrder(GoSendActivity.this);
                    dbOrder.insertOrderTemporary(orderPojo);
                    startActivity(intent);
                }else if(!myEditTextFromAddress.getText().toString().matches("")){
                    Toast toast = Toast.makeText(context, "From?", duration);
                    toast.show();
                    myEditTextFromAddress.setFocusable(true);
                }else if(!myEditTextToAddress.getText().toString().matches("")){
                    Toast toast = Toast.makeText(context, "Destination?", duration);
                    toast.show();
                    myEditTextToAddress.setFocusable(true);
                }else {
                    Toast toast = Toast.makeText(context, "You did not enter the item type", duration);
                    toast.show();
                    itemToDeliver.setFocusable(true);
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
//        Log.w("On Location Changes : ",String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
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
//        Log.w("PROVIDER STATUS : ","ENABLE "+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Log.w("PROVIDER STATUS : ","DISABLE "+provider);
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
    protected void onStart() {
        super.onStart();

        final EditText myEditTextFrom = (EditText) findViewById(R.id.placePickerStart);
        final EditText myEditTextTo = (EditText) findViewById(R.id.placePickerDestination);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView2);
        Intent intent = getIntent();

        if(!myEditTextFrom.getText().toString().matches("")){
            if(intent.getStringExtra("THE_LAT_FROM") != null){
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(intent.getStringExtra("THE_LAT_FROM")),Double.parseDouble(intent.getStringExtra("THE_LONG_FROM")))).icon(BitmapDescriptorFactory.fromResource(R.drawable.mbike)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(intent.getStringExtra("THE_LAT_FROM")),Double.parseDouble(intent.getStringExtra("THE_LONG_FROM"))), 17));
            }
        }
        if(!myEditTextFrom.getText().toString().matches("") && !myEditTextTo.getText().toString().matches("")){
            String base_url = "http://maps.googleapis.com/";

            rateOjeckApi reqinterface2;

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(base_url)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            DirectionInterfaceREST reqinterface = restAdapter.create(DirectionInterfaceREST.class);

            reqinterface.getJson(myEditTextFrom.getText().toString(), myEditTextTo.getText().toString(), new Callback<DirectionResultPojos>() {


                @Override
                public void success(DirectionResultPojos directionResultPojos, Response response) {
                    mMap.clear();
                    ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                    double startLat = 0,startLong = 0;
                    double endLat = 0,endLong = 0;

                    if(directionResultPojos.getRoutes().size()>0){
                        ArrayList<LatLng> decodelist;
                        Route routeA = directionResultPojos.getRoutes().get(0);
                        Log.i("zacharia", "Legs length : "+routeA.getLegs().size());
                        if(routeA.getLegs().size()>0){
                            List<Steps> steps= routeA.getLegs().get(0).getSteps();
                            Log.i("zacharia","Steps size :"+steps.size());
                            Steps step;
                            ojekkeren.ojekkeren.Location location;
                            String polyline;
                            for(int i=0 ; i<steps.size();i++){

                                step = steps.get(i);
                                location = step.getStart_location();
                                if(i==0){
                                    startLat =location.getLat();
                                    startLong = location.getLng();
                                }
                                /*if(i==(steps.size()))
                                {
                                    endLat =location.getLat();
                                    endLong = location.getLng();
                                }*/
                                routelist.add(new LatLng(location.getLat(), location.getLng()));
                                Log.i("zacharia", "Start Location :" + location.getLat() + ", " + location.getLng());
                                polyline = step.getPolyline().getPoints();
                                decodelist = RouteDecode.decodePoly(polyline);
                                routelist.addAll(decodelist);
                                location =step.getEnd_location();
                                routelist.add(new LatLng(location.getLat(), location.getLng()));
                                Log.i("zacharia","End Location :"+location.getLat() +", "+location.getLng());

                                endLat =location.getLat();
                                endLong = location.getLng();
                            }
                        }
                    }

                    if(routelist.size()>0){
                        PolylineOptions rectLine = new PolylineOptions().width(7).color(Color.GREEN);

                        for (int i = 0; i < routelist.size(); i++) {
                            rectLine.add(routelist.get(i));
                        }

                        // Adding route on the map
                        mMap.addPolyline(rectLine);
                       /* Log.w("Lat Route List 1", routelist.get(0).toString());
                        Log.w("Lat Route List 2", routelist.get(routelist.size()).toString());*/

                        mMap.addMarker(new MarkerOptions().position(new LatLng(startLat, startLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.mbike)));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(endLat, endLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.redflag)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLat, startLong), 12));
                        final TextView distanceAndRate = (TextView) findViewById(R.id.distanceRate);

                        float[] results = new float[1];
                        android.location.Location.distanceBetween(startLat, startLong,
                                endLat, endLong,
                                results);
                        final float longitudeLineDistance = results[0];
//                        Log.w("Distanece", String.valueOf((int) longitudeLineDistance/100));
                        distanceAndRate.setVisibility(View.VISIBLE);


                        String base_url = getServerAPIServer();
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(base_url)
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();

                        rateOjeckApi reqinterface2 = restAdapter.create(rateOjeckApi.class);

                        reqinterface2.getRate("getRate", new Callback<rateOjek>() {
                            @Override
                            public void success(rateOjek rateOjek, Response response) {
                                double harga = ((int) longitudeLineDistance / 1000)*(double) rateOjek.getRate();
                                thePrice = harga;
                                theDistance = String.valueOf((int) longitudeLineDistance / 1000);
                                Locale locale = new Locale("id", "ID");
                                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                                distanceAndRate.setAlpha((float) 0.8);
                                distanceAndRate.setText(String.valueOf((int) longitudeLineDistance / 1000) + " KM. " + currencyFormatter.format(harga));

                                //next button show
                                LinearLayout buttoncontainer = (LinearLayout) findViewById(R.id.buttoncontainer);
                                buttoncontainer.setVisibility(View.VISIBLE);

                                scrollView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                }, 600);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.w("Fatal ",error.getMessage());
                            }
                        });




//                        pDialog.cancel();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    final TextView distanceAndRate = (TextView) findViewById(R.id.distanceRate);
                    distanceAndRate.setVisibility(View.VISIBLE);
                    distanceAndRate.setAlpha((float) 0.8);
                    distanceAndRate.setText("Please check your connection.. and try again");
                    myEditTextFrom.setText("");
                    myEditTextTo.setText("");
                    Log.w("Error", error.getMessage().toString());
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){

    }


    /*ChooserFrom Button*/
    public void chooserFromMethod(View v){
        Intent intent = new Intent(v.getContext(), ChooserFromMap.class);
        startActivity(intent);
    }

    /*ChooserTo Button*/
    public void chooserToMethod(View v){
        Intent intent = new Intent(v.getContext(), ChooserToMap.class);
        startActivity(intent);
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
                    menu.findItem(R.id.actionOrders).setVisible(false);
                }
            }else if(akunLoggedCheck.getIsLogged().equals("1")) {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(true);
                    menu.findItem(R.id.actionLogin).setVisible(false);
                    menu.findItem(R.id.actionOrders).setVisible(true);
                    Log.w("Logged ",akunLoggedCheck.getIsLogged());
                }
            }else{
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                    menu.findItem(R.id.actionOrders).setVisible(false);
                }
            }
        }else{
            if (menu != null) {
                menu.findItem(R.id.actionLogout).setVisible(false);
                menu.findItem(R.id.actionLogin).setVisible(true);
                menu.findItem(R.id.actionOrders).setVisible(false);
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

        if(id == R.id.actionOrders){
            DBAccount dbAccount = new DBAccount(this);
            Intent intent = new Intent(getBaseContext(), Orders.class);
            startActivity(intent);
        }

        if(id == R.id.actionExit){
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
            System.exit(0);
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }



    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        Log.w("Re Enter Activity", "HAI TRUE");
    }
}

