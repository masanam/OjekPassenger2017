package ojekkeren.ojekkeren;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GoFoodDelivery  extends ActionBarActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private LayoutInflater mLayoutInflater = null;
    float longitudeLineDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_go_food_delivery);

        final EditText myEditTextTo = (EditText) findViewById(R.id.placePickerDestination);

        final EditText myEditTextFromAddress = (EditText) findViewById(R.id.myEditTextFromAddress);
        final EditText myEditTextToAddress = (EditText) findViewById(R.id.myEditTextToAddress);
        final EditText myEditItemToDeliver = (EditText) findViewById(R.id.itemToDeliver);

        /*hidden element (counting purpose)*/
        final TextView foodCostPriceHidden = (TextView) findViewById(R.id.foodCostHidden);
        final TextView DeliveryCostPriceHidden = (TextView) findViewById(R.id.deliveryCostHidden);
        final TextView totalhargaPriceHidden = (TextView) findViewById(R.id.totalCostHidden);

        final TextView totalHargaText = (TextView) findViewById(R.id.foodcost);
        final TextView totalHargaDelivery = (TextView) findViewById(R.id.deliverycost);
        final TextView totalPrice = (TextView) findViewById(R.id.totalprice);

        final ListView listView = (ListView) findViewById(R.id.menuOrders);
        ArrayList<FoodCart> warungPojoArrayList = new ArrayList<FoodCart>();
        //loop get data from intent
        final Intent intent = getIntent();

        FoodCarts foodCarts = intent.getExtras().getParcelable("foodCart");

        for(int i=0; i < foodCarts.orders.size(); i++){
            FoodCart foodCart = new FoodCart();
            HashMap<String, String> throwit =  foodCarts.orders.get(i);
            Set keyIte = throwit.keySet();
            Iterator keyVal = keyIte.iterator();
            Object keys =keyVal.next();
            String vals = throwit.get(keys);
            String[] getNote = keys.toString().split("\\|");

            if(Integer.parseInt(vals) > 0){
                if(getNote.length > 3){
                    if(Integer.parseInt(vals) > 0){
                        foodCart.setFoodname(getNote[0]);
                        foodCart.setId(getNote[1]);
                        foodCart.setPrice(getNote[2]);
                        foodCart.setQuantity(Integer.parseInt(vals));
                        if(getNote[3] != null){
                            foodCart.setAddNote(getNote[3]);
                        }else{
                            foodCart.setAddNote("No Note");
                        }
                    }
                }else{
                    if(Integer.parseInt(vals) > 0){
                        foodCart.setFoodname(getNote[0]);
                        foodCart.setId(getNote[1]);
                        foodCart.setPrice(getNote[2]);
                        foodCart.setQuantity(Integer.parseInt(vals));
                        foodCart.setAddNote("No Note");
                    }
                }




                Log.w("VALUESSSSSS ", keys+" = "+vals);
                warungPojoArrayList.add(foodCart);
            }
        }

        Integer totalhargaFood = 0;
        for(int p=0; p < warungPojoArrayList.size(); p++){
            totalhargaFood += (Integer.parseInt(warungPojoArrayList.get(p).getPrice())*warungPojoArrayList.get(p).getQuantity());
        }

        foodCostPriceHidden.setText(String.valueOf(totalhargaFood));



        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        Integer foodCostFee = Integer.valueOf(foodCostPriceHidden.getText().toString());
        totalHargaText.setText(currencyFormatter.format(Double.parseDouble(String.valueOf(foodCostFee))));

        float[] results = new float[1];
        double startLat = 0,startLong = 0;
        double endLat = 0, endLong = 0;

            /*Toko Location*/
        startLat = Double.parseDouble(intent.getStringExtra("lat"));
        startLong = Double.parseDouble(intent.getStringExtra("lng"));




        if(intent.getStringExtra("ALAMAT_DESTINASI")!= null){
            myEditTextTo.setText(intent.getStringExtra("ALAMAT_DESTINASI"));

            myEditTextToAddress.setText(intent.getStringExtra("ADDTOTEXT"));


                 /*Tujuan*/
            Log.w("Intent from go choser : ",intent.toString());
            endLat = Double.parseDouble(intent.getStringExtra("THE_LAT_DEST"));
            endLong = Double.parseDouble(intent.getStringExtra("THE_LONG_DEST"));

            android.location.Location.distanceBetween(startLat, startLong,
                    endLat, endLong,
                    results);

            longitudeLineDistance = results[0];

            String base_url = getServerAPIServer();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(base_url)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            rateOjeckApi reqinterface2 = restAdapter.create(rateOjeckApi.class);

            reqinterface2.getRate("getRate", new Callback<rateOjek>() {
                @Override
                public void success(rateOjek rateOjek, Response response) {
                    Locale locale = new Locale("id", "ID");
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                    double harga = ((int) longitudeLineDistance / 1000) * (double) rateOjek.getRate();
                    DeliveryCostPriceHidden.setText(String.valueOf(harga));
                    totalHargaDelivery.setText(currencyFormatter.format(harga));

                    if(!foodCostPriceHidden.getText().toString().equals("0")){
                        double countingPrice = harga+Double.parseDouble(foodCostPriceHidden.getText().toString());
                        totalPrice.setText(currencyFormatter.format(countingPrice));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.w("Fatal ", error.getMessage());
                }
            });
        }

        TextView header_from = (TextView) findViewById(R.id.header_from);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sansation_Regular.ttf");

        myEditTextTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GoFoodDeliveryChooserToMap.class);
                overridePendingTransition(R.anim.fab_in, R.anim.fab_out);
                /*pegang from dan to nya*/
                intent.putExtra("destAddText", myEditTextToAddress.getText().toString());


                 /*harus membawa details DEST karena mauFROM akan muncul dna bawa intent*/
                Intent currentIntent = getIntent();
                FoodCarts foodCarts = currentIntent.getExtras().getParcelable("foodCart");
                intent.putExtra("lat", currentIntent.getStringExtra("lat"));
                intent.putExtra("lng", currentIntent.getStringExtra("lng"));
                intent.putExtra("idToko", currentIntent.getStringExtra("idToko"));
                intent.putExtra("tokoName", currentIntent.getStringExtra("tokoName"));

                intent.putExtra("foodCart", foodCarts);
                startActivity(intent);
            }
        });

        SummaryFoodDeliveryList adapter = new SummaryFoodDeliveryList(GoFoodDelivery.this,warungPojoArrayList);
        listView.setDivider(null);
        listView.setAdapter(adapter);

        ImageButton nextlogin = (ImageButton) findViewById(R.id.nextlogin);
        nextlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), LoginActivity.class);
                overridePendingTransition(R.anim.fab_in, R.anim.fab_out);

                final EditText myEditTextTo = (EditText) findViewById(R.id.placePickerDestination);
                final EditText myEditTextToAddress = (EditText) findViewById(R.id.myEditTextToAddress);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                final Intent currentIntent = getIntent();

                if (!myEditTextTo.getText().toString().matches("")) {
                    //Creating a rest adapter
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint("http://maps.googleapis.com/")
                            .build();

                    //Creating an object of our api interface
                    GeoCodeAPI api = adapter.create(GeoCodeAPI.class);

                    //Defining the method
                    api.getAddressName(currentIntent.getStringExtra("lat")+","+currentIntent.getStringExtra("lng"), new Callback<GeocodePojoClass>() {

                        @Override
                        public void success( GeocodePojoClass geocodePojoClasses, Response response) {
//                            geocodePojoClasses.getResults().get(0).getFormatted_address()
                            OrderPojo orderPojo = new OrderPojo();
                            orderPojo.setAddressFrom(currentIntent.getStringExtra("tokoName"));
                            orderPojo.setAddressTo(myEditTextToAddress.getText().toString());
                            orderPojo.setFrom(geocodePojoClasses.getResults().get(0).getFormatted_address());
                            orderPojo.setTo(myEditTextTo.getText().toString());
                            orderPojo.setLatFrom(currentIntent.getStringExtra("lat"));
                            orderPojo.setLongFrom(currentIntent.getStringExtra("lng"));
                            orderPojo.setLatTo(currentIntent.getStringExtra("THE_LAT_DEST"));
                            orderPojo.setLongTo(currentIntent.getStringExtra("THE_LONG_DEST"));
                            orderPojo.setDistance(String.valueOf(((int) longitudeLineDistance / 1000)));
                            orderPojo.setPrice(totalPrice.getText().toString());

                            String strBuilder = "";

                            for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                                View view = listView.getChildAt(i);
                                TextView quantityFood = (TextView) view.findViewById(R.id.quantity);
                                EditText hasNoteFood = (EditText) view.findViewById(R.id.thenote);
                                TextView theFoodName = (TextView) view.findViewById(R.id.namaFood);
                                TextView thePriceFood = (TextView) view.findViewById(R.id.price);

                                strBuilder += theFoodName.getText().toString()+"("+quantityFood.getText().toString()+"x) \n\n";
                            }

                            orderPojo.setItemToDeliver(strBuilder);

                            DBAccount dbAccountConn = new DBAccount(GoFoodDelivery.this);

                            if (dbAccountConn.getCurrentMemberDetails().getIsLogged() != null) {
                                MemberAkun memberAkunGetPhone = dbAccountConn.getCurrentMemberDetails();

                                if (memberAkunGetPhone.getIsLogged().equals("1")) {
                                    orderPojo.setPhoneNum(memberAkunGetPhone.getPhoneNumber());
                                } else {
                                    orderPojo.setPhoneNum("0");
                                }
                            }

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            orderPojo.setOrderTime(dateFormat.format(date));

                    /*gO fOOD  = 3*/
                            orderPojo.setType("3");

                    /*save on this temporary*/
                            orderPojo.setStatus("3");

                            DBOrder dbOrder = new DBOrder(GoFoodDelivery.this);
                            dbOrder.insertOrderTemporary(orderPojo);


                            List<OrderFoodPojo> orderFoodPojo = new ArrayList<OrderFoodPojo>();
                            int num_of_visible_view=listView.getLastVisiblePosition() -
                                    listView.getFirstVisiblePosition();
                            for (int i = 0; i <= num_of_visible_view; i++) {
                                OrderFoodPojo arrEntryFoodPojo = new OrderFoodPojo();
                                View view = listView.getChildAt(i);
                                TextView quantity = (TextView) view.findViewById(R.id.quantity);
                                EditText hasNote = (EditText) view.findViewById(R.id.thenote);
                                TextView foodid = (TextView) view.findViewById(R.id.foodidList);
                                TextView namaFood = (TextView) view.findViewById(R.id.namaFood);

                                arrEntryFoodPojo.setOrderid(dbOrder.getLatestOrderId());
                                arrEntryFoodPojo.setQuantity(Integer.parseInt(quantity.getText().toString()));
                                arrEntryFoodPojo.setFoodid(Integer.parseInt(foodid.getText().toString()));
                                arrEntryFoodPojo.setFoodName(namaFood.getText().toString());
                                arrEntryFoodPojo.setNote(hasNote.getText().toString());
                                orderFoodPojo.add(arrEntryFoodPojo);
                            }
                            dbOrder.insertOrderTemporaryFood(orderFoodPojo);
                            startActivity(intent);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //you can handle the errors here
                            Log.w("GEOCODE ERROR", error.getMessage());
                        }
                    });



                } else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(GoFoodDelivery.this);
                    adb.setTitle("Destination?");
                    adb.setMessage("Please choose destination.. ");
                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    adb.show();
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
            dbAccount.setLogout();
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
