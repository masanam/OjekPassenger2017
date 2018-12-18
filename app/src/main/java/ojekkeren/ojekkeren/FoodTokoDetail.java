package ojekkeren.ojekkeren;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FoodTokoDetail extends AppCompatActivity {

    private ProgressDialog pDialog;

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
        setContentView(R.layout.activity_food_toko_detail);

        pDialog = new ProgressDialog(FoodTokoDetail.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        String base_url = getServerAPIServer();

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
                    toolBarLayout.setTitle(warungPojos.getNamawarung().substring(0, 16) + ".. - " + intent.getStringExtra("distance") + " Km");
//                    descText.setText(warungPojos.getDesc().substring(0, 50));
                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("RetrofitError", error.getMessage());
            }
        });


        toolBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
