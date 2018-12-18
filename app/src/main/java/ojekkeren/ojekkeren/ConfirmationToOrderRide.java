package ojekkeren.ojekkeren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class ConfirmationToOrderRide extends ActionBarActivity {

    private ProgressDialog pDialog;
    final private String phoneNumCurrent = null;
    public String theDuration;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public String getServerAPIServer() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_to_order_ride);
        pDialog = new ProgressDialog(ConfirmationToOrderRide.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        Intent prevIntent = getIntent();

        TextView startAddress = (TextView) findViewById(R.id.startAddress);
        TextView destAddress = (TextView) findViewById(R.id.destAddress);
        TextView ItemToDeliver = (TextView) findViewById(R.id.ItemToDeliver);
        TextView distanceDanTotal = (TextView) findViewById(R.id.distance);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Cash", "Credit", "Point"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Spinner passenger = (Spinner)findViewById(R.id.spinner2);
        String[] items2 = new String[]{"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        passenger.setAdapter(adapter2);

        final DBOrder dbOrder = new DBOrder(ConfirmationToOrderRide.this);
        final OrderPojo orderPojo = dbOrder.getTheLatestOrderData();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.boxitemdeliver1);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.boxitemdeliver2);

        if (!orderPojo.getType().equals("2")) {

            linearLayout.setVisibility(View.GONE);

            linearLayout2.setVisibility(View.GONE);
        }

        if (orderPojo.getType().equals("3")) {
            linearLayout.setVisibility(View.VISIBLE);

            linearLayout2.setVisibility(View.VISIBLE);

            TextView labelLoc = (TextView) findViewById(R.id.labelLocation);
            labelLoc.setText("Restaurant Name");
            startAddress.setText(orderPojo.getAddressFrom());
            ItemToDeliver.setText(orderPojo.getItemToDeliver());
        } else {
            startAddress.setText(orderPojo.getFrom() + " , " + orderPojo.getAddressFrom());
            destAddress.setText(orderPojo.getTo() + " , " + orderPojo.getAddressTo());
        }


        if (orderPojo.getType().equals("2")) {
            ItemToDeliver.setText(orderPojo.getItemToDeliver());
        }
        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);


        if (orderPojo.getType().equals("3")) {
            distanceDanTotal.setText(orderPojo.getDistance() + " KM , " + orderPojo.getPrice());
        } else {
            String[] splitPrice = orderPojo.getPrice().split("\\.");
            //double duration = (float) (Float.parseFloat(orderPojo.getDistance()) * 3.95);
            //String theDuration = String.format("%.0f", duration);
            distanceDanTotal.setText(orderPojo.getDistance() + " KM, " +theDuration+" "+ currencyFormatter.format(Double.parseDouble(splitPrice[0])));
        }

//        distanceDanTotal.setText(orderPojo.getDistance() + " KM , " + orderPojo.getPrice() );
        destAddress.setText(orderPojo.getTo() + ", " + orderPojo.getAddressTo());

        pDialog.cancel();
        final String base_url = getServerAPIServer();


        final OrderPojo orderPojo2 = orderPojo;


        ImageButton toFinalConfirmationOrder = (ImageButton) findViewById(R.id.toFinalConfirmationOrder);
        toFinalConfirmationOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dbOrder.setOrderStatusTo2LastOrder();

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(base_url)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setClient(new OkClient(new OkHttpClient()))
                        .build();

                final OrderAPI orderAPI = restAdapter.create(OrderAPI.class);

                DBAccount dbAccountConn = new DBAccount(ConfirmationToOrderRide.this);

                String thePhoneNum = null;
                if (dbAccountConn.getCurrentMemberDetails().getIsLogged() != null) {
                    MemberAkun memberAkunGetPhone = dbAccountConn.getCurrentMemberDetails();

                    if (memberAkunGetPhone.getIsLogged().equals("1")) {
                        thePhoneNum = memberAkunGetPhone.getPhoneNumber();
                    }
                }

                Log.w("Type", orderPojo2.getType());


                if (orderPojo2.getType().equals("2")) {
                    orderAPI.setOrder("setOrder",
                            thePhoneNum,
                            orderPojo2.getFrom(),
                            orderPojo2.getTo(),
                            orderPojo2.getDistance(),
                            orderPojo2.getPrice(),
                            orderPojo2.getLatFrom() + "," + orderPojo2.getLongFrom(),
                            orderPojo2.getLatTo() + "," + orderPojo2.getLongTo(),
                            orderPojo2.getAddressFrom(),
                            orderPojo2.getAddressTo(),
                            orderPojo2.getItemToDeliver(),
                            orderPojo2.getType(),
                            orderPojo2.getOrderTime(),
                            new Callback<RegisterResponsePojo>() {

                                @Override
                                public void success(RegisterResponsePojo registerResponsePojo, Response response) {
                                    if (registerResponsePojo.getResponse().equals("SUCCESS")) {
                                        //masukan database API
                                        Intent intent = new Intent(ConfirmationToOrderRide.this, FinalConfirmationOrder.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (registerResponsePojo.getResponse().equals("USER NOT EXISTS")) {
                                        Toast.makeText(ConfirmationToOrderRide.this, "Please check your connection.. #USER_NOT_EXISTS", Toast.LENGTH_LONG).show();
                                        if (orderPojo2.getType().equals("2")) {
                                            Intent intent = new Intent(ConfirmationToOrderRide.this, GoSendActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        if (orderPojo2.getType().equals("1")) {
                                            Intent intent = new Intent(ConfirmationToOrderRide.this, GoRideMenu.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            }
                    );
                } else if (orderPojo2.getType().equals("3")) {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint("http://maps.googleapis.com/")
                            .build();

                    //Creating an object of our api interface
                    GeoCodeAPI api = adapter.create(GeoCodeAPI.class);

                    //Defining the method
                    final String finalThePhoneNum = thePhoneNum;
                    api.getAddressName(orderPojo2.getLatFrom() + "," + orderPojo2.getLongFrom(), new Callback<GeocodePojoClass>() {

                        @Override
                        public void success(GeocodePojoClass geocodePojoClasses, Response response) {
//                            String[] dapatAlamat = geocodePojoClasses.getResults().get(0).getFormatted_address().split(",");
                            double latx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLat();
                            double longx = geocodePojoClasses.getResults().get(0).getGeometry().getLocation().getLng();

                            orderAPI.setOrder("setOrder",
                                    finalThePhoneNum,
                                    geocodePojoClasses.getResults().get(0).getFormatted_address(),
                                    orderPojo2.getTo(),
                                    orderPojo2.getDistance(),
                                    orderPojo2.getPrice(),
                                    orderPojo2.getLatFrom() + "," + orderPojo2.getLongFrom(),
                                    orderPojo2.getLatTo() + "," + orderPojo2.getLongTo(),
                                    orderPojo2.getAddressFrom(),
                                    orderPojo2.getAddressTo(),
                                    orderPojo2.getItemToDeliver(),
                                    orderPojo2.getType(),
                                    orderPojo2.getOrderTime(),
                                    new Callback<RegisterResponsePojo>() {

                                        @Override
                                        public void success(RegisterResponsePojo registerResponsePojo, Response response) {
                                            if (registerResponsePojo.getResponse().equals("SUCCESS")) {
                                                //masukan database API
                            /*masukan order food*/
                                                OrderFoodPojo arrEntryFoodPojo = new OrderFoodPojo();
                                                List<OrderFoodPojo> arrEntryPojoGroup = dbOrder.getLatestFoodByOrderId(Integer.valueOf(dbOrder.getLatestOrderId()));


                                                for (int t = 0; t < arrEntryPojoGroup.size(); t++) {
                                                    orderAPI.setOrderFood("setOrderFood",
                                                            registerResponsePojo.getOrderid(),
                                                            String.valueOf(arrEntryPojoGroup.get(t).getFoodid()),
                                                            String.valueOf(arrEntryPojoGroup.get(t).getQuantity()),
                                                            arrEntryPojoGroup.get(t).getFoodName(),
                                                            arrEntryPojoGroup.get(t).getNote(),
                                                            new Callback<RegisterResponsePojo>() {

                                                                @Override
                                                                public void success(RegisterResponsePojo registerResponsePojo, Response response) {
                                                                    if (registerResponsePojo.getResponse().equals("SUCCESS")) {
                                                                        Intent intent = new Intent(ConfirmationToOrderRide.this, FinalConfirmationOrder.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else if (registerResponsePojo.getResponse().equals("USER NOT EXISTS")) {
                                                                        Toast.makeText(ConfirmationToOrderRide.this, "Please check your connection.. #USER_NOT_EXISTS", Toast.LENGTH_LONG).show();
                                                                        if (orderPojo2.getType().equals("2")) {
                                                                            Intent intent = new Intent(ConfirmationToOrderRide.this, GoSendActivity.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                        if (orderPojo2.getType().equals("1")) {
                                                                            Intent intent = new Intent(ConfirmationToOrderRide.this, GoRideMenu.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void failure(RetrofitError error) {
                                                                    Log.w("Error Message", error.getResponse().getReason().toString());
                                                                    Log.w("Error Message", error.getMessage());
                                                                }
                                                            }
                                                    );
                                                }
                                            } else if (registerResponsePojo.getResponse().equals("USER NOT EXISTS")) {
                                                Toast.makeText(ConfirmationToOrderRide.this, "Please check your connection.. #USER_NOT_EXISTS", Toast.LENGTH_LONG).show();
                                                if (orderPojo2.getType().equals("2")) {
                                                    Intent intent = new Intent(ConfirmationToOrderRide.this, GoSendActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                if (orderPojo2.getType().equals("1")) {
                                                    Intent intent = new Intent(ConfirmationToOrderRide.this, GoRideMenu.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.w("Error Message", error.getResponse().getReason().toString());
                                            Log.w("Error Message", error.getMessage());
                                        }
                                    }
                            );
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //you can handle the errors here
                            Log.w("GEOCODE ERROR", error.getMessage());
                        }
                    });
                } else {
                    orderAPI.setOrder("setOrder",
                            thePhoneNum,
                            orderPojo2.getFrom(),
                            orderPojo2.getTo(),
                            orderPojo2.getDistance(),
                            orderPojo2.getPrice(),
                            orderPojo2.getLatFrom() + "," + orderPojo2.getLongFrom(),
                            orderPojo2.getLatTo() + "," + orderPojo2.getLongTo(),
                            orderPojo2.getAddressFrom(),
                            orderPojo2.getAddressTo(),
                            "",
                            orderPojo2.getType(),
                            orderPojo2.getOrderTime(),
                            new Callback<RegisterResponsePojo>() {

                                @Override
                                public void success(RegisterResponsePojo registerResponsePojo, Response response) {
                                    if (registerResponsePojo.getResponse().equals("SUCCESS")) {
                                        //masukan database API
                                        Intent intent = new Intent(ConfirmationToOrderRide.this, FinalConfirmationOrder.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (registerResponsePojo.getResponse().equals("USER NOT EXISTS")) {
                                        Toast.makeText(ConfirmationToOrderRide.this, "Please check your connection.. #USER_NOT_EXISTS", Toast.LENGTH_LONG).show();
                                        if (orderPojo2.getType().equals("1")) {
                                            Intent intent = new Intent(ConfirmationToOrderRide.this, GoRideMenu.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            }
                    );
                }

            }
        });
        pDialog.cancel();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void success(DirectionResultPojos directionResultPojos, Response response) {
        ArrayList<LatLng> routelist = new ArrayList<LatLng>();
        double startLat = 0, startLong = 0;
        double endLat = 0, endLong = 0;

        if (directionResultPojos.getRoutes().size() > 0) {
            ArrayList<LatLng> decodelist;
            Route routeA = directionResultPojos.getRoutes().get(0);
            //   Log.i("zacharia", "Legs length : " + routeA.getLegs().size());
            if (routeA.getLegs().size() > 0) {
                List<Steps> steps = routeA.getLegs().get(0).getSteps();
                Steps step;
                ojekkeren.ojekkeren.Location location;
                theDuration = String.valueOf(routeA.getLegs().get(0).getDuration().getText());
                String polyline;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DBAccount dbAccount = new DBAccount(this);

        if (dbAccount.getCurrentMemberDetails().getIsLogged() != null) {
            MemberAkun akunLoggedCheck = dbAccount.getCurrentMemberDetails();

            Log.w("MENU STAT", akunLoggedCheck.getIsLogged());
            if (akunLoggedCheck.getIsLogged().equals("0")) {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                }
            } else if (akunLoggedCheck.getIsLogged().equals("1")) {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(true);
                    menu.findItem(R.id.actionLogin).setVisible(false);
                    Log.w("Logged ", akunLoggedCheck.getIsLogged());
                }
            } else {
                if (menu != null) {
                    menu.findItem(R.id.actionLogout).setVisible(false);
                    menu.findItem(R.id.actionLogin).setVisible(true);
                }
            }
        } else {
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

        if (id == R.id.actionLogout) {
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.actionLogin) {
            DBAccount dbAccount = new DBAccount(this);
            dbAccount.setLogout();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ConfirmationToOrderRide Page", // TODO: Define a title for the content shown.
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
                "ConfirmationToOrderRide Page", // TODO: Define a title for the content shown.
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
}
