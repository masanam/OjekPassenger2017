package ojekkeren.ojekkeren;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
 * {@link OrdersInProgress.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrdersInProgress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersInProgress extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrdersInProgress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersInProgress.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersInProgress newInstance(String param1, String param2) {
        OrdersInProgress fragment = new OrdersInProgress();
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
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_orders_in_progress, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listingOrdersProgress);


        String base_url = getServerAPIServer();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        GetOrdersInProgressREST getOrdersInProgressREST = restAdapter.create(GetOrdersInProgressREST.class);

        final DBAccount dbAccount = new DBAccount(getActivity());
        String phoneNum = null;
        if(dbAccount.getCurrentMemberDetails().getIsLogged() != null){
            MemberAkun akunLoggedCheck = dbAccount.getCurrentMemberDetails();

            if(akunLoggedCheck.getIsLogged().equals("1"))
            {
                phoneNum = akunLoggedCheck.getPhoneNumber();
            }
        }

        getOrdersInProgressREST.getOrdersList("getOrderInProgress",phoneNum, new Callback<List<OrdersData>>() {

            @Override
            public void success(List<OrdersData> ordersDatas, Response response) {
                ArrayList<OrdersData> ordersData = new ArrayList<OrdersData>();
                if(ordersDatas.size() == 0){
                    TextView textView = (TextView) rootView.findViewById(R.id.nodataorders);
                    textView.setVisibility(View.VISIBLE);
                }else{
                    for (int i = 0; i < ordersDatas.size(); i++) {
                        ordersData.add(ordersDatas.get(i));
                    }
                    OrdersDataAdapter adapter = new OrdersDataAdapter(getActivity(),ordersData);
//                DeliveryCategoryAdapterList adapter = new DeliveryCategoryAdapterList(getActivity(), warungPojoArrayList);
                    listView.setAdapter(adapter);
                }

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
