package ojekkeren.ojekkeren;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class NearMeActivity extends ActionBarActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

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

        Button catbutt = (Button) findViewById(R.id.catbutt);
        catbutt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), GoFoodActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        ImageView imageView12 = (ImageView) findViewById(R.id.imageView12);
        imageView12.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), MenuQuantity.class);
                startActivity(intent);
                return false;
            }
        });

        ImageView imageView13 = (ImageView) findViewById(R.id.imageView13);
        imageView13.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(v.getContext(), MenuQuantity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_near_me, menu);
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
