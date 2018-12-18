package ojekkeren.ojekkeren;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryMenuNearMe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryMenuNearMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryMenuNearMe extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private String lat,lng;
    private LatLng oldLatLng;
    private MapView mMapView;
    private ProgressDialog pDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onResume() {
        super.onResume();
        Log.w("visiting","true near");
    }

    public DeliveryMenuNearMe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryMenuNearMe.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryMenuNearMe newInstance(String param1, String param2) {
        DeliveryMenuNearMe fragment = new DeliveryMenuNearMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public String getServerAPIServer(){
        Context context = getActivity();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Try to obtain the map from the SupportMapFragment.

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        final View rootView = inflater.inflate(R.layout.fragment_delivery_menu_near_me, container, false);
        final ListView listViewNearMe = (ListView) rootView.findViewById(R.id.listingWarung);

        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);

        Location location = null;

        if (mMap != null) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, this);
            Criteria criteria = new Criteria();
            String provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            location = locationManager.getLastKnownLocation(provider);

            if (location == null ) {
                Toast.makeText(getContext(), "Geo Coder Not Avaiable", Toast.LENGTH_LONG).show();
            }else{
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
            }
        }

        listViewNearMe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

                TextView getCart = (TextView) view.findViewById(R.id.theId);
                TextView getDistance = (TextView) view.findViewById(R.id.theDistance);
                Log.w("ID Listvoew Clicked",getCart.getText().toString());

                Intent intent = new Intent(getActivity(), MenuQuantity.class);
                intent.putExtra("id",getCart.getText().toString());
                intent.putExtra("distance",getDistance.getText().toString());

                pDialog.cancel();

                startActivity(intent);
            }
        });

        String base_url = getServerAPIServer();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        GetBrowseTokoListREST getBrowseTokoListREST = restAdapter.create(GetBrowseTokoListREST.class);

        if(location != null){
            getBrowseTokoListREST.getBrowseList("getBrowseWarungByLatLng",String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()), new Callback<List<WarungPojo>>() {


                @Override
                public void success(List<WarungPojo> warungPojos, Response response) {
                    ArrayList<WarungPojo> warungPojoArrayList = new ArrayList<WarungPojo>();
                    if(warungPojos !=null){
                        for(int i=0 ; i<warungPojos.size();i++){
                            warungPojoArrayList.add(warungPojos.get(i));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(warungPojos.get(i).getLat()), Double.parseDouble(warungPojos.get(i).getLng()))));
                        }
                        NearMeAdapter adapter = new NearMeAdapter(getActivity(),warungPojoArrayList);

                        ListView listView = (ListView) rootView.findViewById(R.id.listingWarung);
                        listView.setDivider(null);
                        listView.setAdapter(adapter);
                    }


                    pDialog.cancel();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.w("RetrofitError",error.getMessage());
                    pDialog.cancel();
                }
            });
        }



        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.w("LatLong ", String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
        /*if(oldLatLng != null){
            mMap.addMarker(new MarkerOptions().position(oldLatLng)).remove();
        }else {
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            oldLatLng = userLocation;
            mMap.addMarker(new MarkerOptions().position(userLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        }*/
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
