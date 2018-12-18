package ojekkeren.ojekkeren;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by andi on 4/20/2016.
 */
public class SummaryFoodDeliveryList extends ArrayAdapter<FoodCart> {

    ArrayList<FoodCart> foodCarts;
    public Context Context;

    public SummaryFoodDeliveryList(Context context, ArrayList<FoodCart> foodCarts) {
        super(context, 0, foodCarts);
        this.foodCarts = new ArrayList<FoodCart>();
        this.foodCarts.addAll(foodCarts);
    }

    public String getServerAPIServer(){
        Context context = getContext();
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
    public View getView(int position, View convertView, final ViewGroup parent) {

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation_Regular.ttf");

        String base_url = getServerAPIServer();

        final FoodCart foodCart = foodCarts.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.summary_food_delivery_list, parent, false);
        }
        TextView foodId = (TextView) convertView.findViewById(R.id.foodidList);
        foodId.setText(foodCart.getId());


        TextView foodname = (TextView) convertView.findViewById(R.id.namaFood);
        foodname.setText(foodCart.getFoodname());

        TextView price = (TextView) convertView.findViewById(R.id.price);
        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        price.setText("@"+currencyFormatter.format(Double.parseDouble(foodCart.getPrice())));

        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        quantity.setText(String.valueOf(foodCart.getQuantity()));
        /*quantity.setMaxValue(100);
        quantity.setMinValue(0);
        quantity.setValue(foodCart.getQuantity());

        quantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    adb.show();
                }

                *//*TextView foodCost = (TextView) convertView.findViewById(R.id.foodcost);
                Log.w("Food Cost ",foodCost.getText().toString());*//*

                *//*Locale locale = new Locale("id", "ID");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                Number number = null;
                try {
                    currencyFormatter.parse(getLatestPrice.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double dClone = number.doubleValue();
                Log.w("WCLONE", String.valueOf(dClone));*//*
            }
        });*/

        EditText hasNote = (EditText) convertView.findViewById(R.id.thenote);
        if(foodCart.getAddNote().equals("No Note")){
            hasNote.setHint(foodCart.getAddNote());
        }else{
            hasNote.setText(foodCart.getAddNote());
        }



        return convertView;
    }
}
