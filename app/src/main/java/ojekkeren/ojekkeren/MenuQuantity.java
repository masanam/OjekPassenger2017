package ojekkeren.ojekkeren;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MenuQuantity extends ActionBarActivity {

    private ProgressDialog pDialog;
    private List<HashMap<String,String>> bufferOrder = new ArrayList<>();
    private FoodCarts foodCarts;

    public String getServerAPIServer(){
        Context context = getBaseContext();
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
        setContentView(R.layout.activity_menu_quantity);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sansation_Regular.ttf");

        pDialog = new ProgressDialog(MenuQuantity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        final ImageView imageWarung = (ImageView) findViewById(R.id.warungImage2);
        final TextView headerTokoTitle = (TextView) findViewById(R.id.headerTitleToko);
        headerTokoTitle.setTypeface(face);
        final TextView alamatTokoTitle = (TextView) findViewById(R.id.deskripsiTokoTitle);
        alamatTokoTitle.setTypeface(face);
        final ListView listView = (ListView) findViewById(R.id.listViewFoods);

        final ImageButton orderFoodConfirmationButt = (ImageButton) findViewById(R.id.orderFoodConfirmation);

        final TextView idToko = (TextView) findViewById(R.id.idToko);
        final TextView lat = (TextView) findViewById(R.id.lat);
        final TextView lng = (TextView) findViewById(R.id.lng);





        final String base_url = getServerAPIServer();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        final Intent intent = getIntent();

        GetBrowseTokoListREST getBrowseTokoListREST = restAdapter.create(GetBrowseTokoListREST.class);

        getBrowseTokoListREST.getTokoDetailsById("getTokoDetailsById", intent.getStringExtra("id"), new Callback<WarungPojo>() {

            @Override
            public void success(WarungPojo warungPojos, Response response) {
                if (warungPojos != null) {
                    Picasso.with(MenuQuantity.this).load(base_url + "/img/" + warungPojos.getPic()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageWarung);
                    headerTokoTitle.setText(warungPojos.getNamawarung().toUpperCase());
                    alamatTokoTitle.setText(warungPojos.getDesc() + ", " + intent.getStringExtra("distance")+" Km");
                    idToko.setText(warungPojos.getId());
                    lat.setText(warungPojos.getLat());
                    lng.setText(warungPojos.getLng());

                    pDialog.cancel();
                }else{
                    Log.w("getTokoDetailsById = "+intent.getStringExtra("id"),"NULL");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("RetrofitError", error.getMessage());
                pDialog.cancel();
            }
        });


        orderFoodConfirmationButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(MenuQuantity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

                bufferOrder.clear();

                boolean orderQuantityNull = false;
                int countingNull = 0;
                int num_of_visible_view=listView.getLastVisiblePosition() -
                        listView.getFirstVisiblePosition();
                for (int i = 0; i <= num_of_visible_view; i++) {
                    View view = listView.getChildAt(i);
                    NumberPicker quantity = (NumberPicker) view.findViewById(R.id.quantity);
                    if(quantity.getValue() == 0){
                        countingNull++;
                    }
                    EditText addNote = (EditText) view.findViewById(R.id.addnote);
                    TextView foodname = (TextView) view.findViewById(R.id.foodname);

                    TextView idfood = (TextView) view.findViewById(R.id.idfood);
                    TextView pricepure = (TextView) view.findViewById(R.id.pricepure);


                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    if (addNote.getText() != null) {
                        hashMap.put(foodname.getText().toString() + "|" + idfood.getText().toString() + "|" + pricepure.getText().toString() + "|" + addNote.getText().toString(), String.valueOf(quantity.getValue()));
                        bufferOrder.add(hashMap);
                    } else {
                        hashMap.put(foodname.getText().toString() + "|" + idfood.getText().toString() + "|" + pricepure.getText().toString() + "|", String.valueOf(quantity.getValue()));
                        bufferOrder.add(hashMap);
                    }
                    Log.w("Items ", foodname.getText().toString() + " Jumlah : " + quantity.getValue() + " (Add Note : " + addNote.getText().toString() + ")");
                }

                if(countingNull == (listView.getAdapter().getCount())){
                    orderQuantityNull = true;
                }



                if(orderQuantityNull == true){
                    AlertDialog.Builder adb = new AlertDialog.Builder(MenuQuantity.this);
                    adb.setTitle("Quantity?");
                    adb.setMessage("Please select the Quantity.. ");
                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    adb.show();
                    pDialog.cancel();

                }else{
                    Intent intent = new Intent(MenuQuantity.this, GoFoodDelivery.class);

                    Intent currIntent = getIntent();

                    foodCarts = new FoodCarts(bufferOrder);
                    intent.putExtra("foodCart", foodCarts);
                    intent.putExtra("distanceToko", currIntent.getStringExtra("distance"));
                    intent.putExtra("tokoName", headerTokoTitle.getText().toString());
                    intent.putExtra("lat", lat.getText().toString());
                    intent.putExtra("lng", lng.getText().toString());
                    intent.putExtra("idToko", idToko.getText().toString());

                    pDialog.cancel();
                    startActivity(intent);
                }



                /*DBOrder dbOrder = new DBOrder(MenuQuantity.this);

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
                orderPojo.setPrice(String.valueOf(thePrice));*/


            }
        });


        GetBrowseTokoListREST getFoodList = restAdapter.create(GetBrowseTokoListREST.class);

        getFoodList.getFoodListByTokoId("getListingFoodByTokoId", intent.getStringExtra("id"), new Callback<List<WarungPojo>>() {

            @Override
            public void success(List<WarungPojo> warungPojos, Response response) {
                if (warungPojos != null) {
                    ArrayList<WarungPojo> warungPojoArrayList = new ArrayList<WarungPojo>();
                    for(int i=0 ; i<warungPojos.size();i++){
                        warungPojoArrayList.add(warungPojos.get(i));
                    }
                    FoodTokoDetailListAdapter adapter = new FoodTokoDetailListAdapter(MenuQuantity.this,warungPojoArrayList);
                    listView.setDivider(null);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("RetrofitError", error.getMessage());
            }
        });

    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_quantity, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
