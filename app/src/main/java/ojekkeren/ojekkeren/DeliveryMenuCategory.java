package ojekkeren.ojekkeren;

import android.app.TabActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryMenuCategory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryMenuCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryMenuCategory extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        Log.w("visiting","true category");
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    // Container Activity must implement this interface
    /*public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }*/

    public DeliveryMenuCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryMenuCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryMenuCategory newInstance(String param1, String param2) {
        DeliveryMenuCategory fragment = new DeliveryMenuCategory();
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
        Log.w("masuk","Category");
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_delivery_menu_category, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listingCategoryWarung);
        final ListView listViewNearMe = (ListView) rootView.findViewById(R.id.listingWarung);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.w("asdas", "asdasd");
                TabLayout host = (TabLayout) getActivity().findViewById(R.id.tabs);
                host.getTabAt(1).select();
            }
        });

        String base_url = getServerAPIServer();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        GetListingCategoryREST getListingCategoryREST = restAdapter.create(GetListingCategoryREST.class);

        getListingCategoryREST.getLists("getListingCategory", new Callback<List<WarungCategory>>() {

            @Override
            public void success(List<WarungCategory> warungCategories, Response response) {
                ArrayList<WarungCategory> warungPojoArrayList = new ArrayList<WarungCategory>();
                for (int i = 0; i < warungCategories.size(); i++) {
                    warungPojoArrayList.add(warungCategories.get(i));
                }
                DeliveryCategoryAdapterList adapter = new DeliveryCategoryAdapterList(getActivity(), warungPojoArrayList);


                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("RetrofitError", error.getMessage());
            }
        });


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
       /* if (context instanceof OnFragmentInteractionListener) {
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
