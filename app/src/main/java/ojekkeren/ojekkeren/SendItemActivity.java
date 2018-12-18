package ojekkeren.ojekkeren;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class SendItemActivity extends ActionBarActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_item);

        TextView header_from = (TextView) findViewById(R.id.header_from);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sansation_Regular.ttf");
        header_from.setTypeface(face);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.setMyLocationEnabled(true);

        // Check if we were successful in obtaining the map.
        if (mMap != null) {


           /* mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 16));
                    mMap.setOnMyLocationChangeListener(null);
                }
            });*/

            /*LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);

//            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("It's Me!"));
            if (location == null ) {
                Toast.makeText(this, "Geo Coder Not Avaiable", Toast.LENGTH_LONG).show();
                return;
            }else{
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
            }*/
            double lat = -6.176811;
            double lng = 106.790402;
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("It's Me!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
        }

        /*EditText myEditText = (EditText) findViewById(R.id.editText8);
        myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    Intent intent = new Intent(v.getContext(), ChooserFromMap.class);
                    startActivity(intent);
                }
            }
        });

        EditText myEditText = (EditText) findViewById(R.id.editText10);
        myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    Intent intent = new Intent(v.getContext(), ChooserToMap.class);
                    startActivity(intent);
                }
            }
        });*/

        EditText myEditTextFrom = (EditText) findViewById(R.id.editText8);
        myEditTextFrom.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), ChooserFromMap.class);
                startActivity(intent);
                return false;
            }
        });

        EditText myEditTextTo = (EditText) findViewById(R.id.editText10);
        myEditTextTo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), ChooserToMap.class);
                startActivity(intent);
                return false;
            }
        });

        ImageButton nextlogin = (ImageButton) findViewById(R.id.nextlogin);
        nextlogin.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_item, menu);
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
