package ojekkeren.ojekkeren;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by andi on 4/29/2016.
 */
public class OrdersDataAdapter extends ArrayAdapter<OrdersData> {

    ArrayList<OrdersData> ordersData;

    public OrdersDataAdapter(Context context, ArrayList<OrdersData> ordersDatas) {
        super(context, 0, ordersDatas);
        this.ordersData = new ArrayList<OrdersData>();
        this.ordersData.addAll(ordersDatas);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        String base_url = getServerAPIServer();

        OrdersData ordersDataView = ordersData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_orders, parent, false);
        }

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation_Regular.ttf");
        TextView typeorder = (TextView) convertView.findViewById(R.id.typeorder);
        typeorder.setTypeface(face);
        TextView fromandto = (TextView) convertView.findViewById(R.id.fromandto);
        fromandto.setTypeface(face);
        TextView ordertime = (TextView) convertView.findViewById(R.id.ordertime);
        ordertime.setTypeface(face);
        TextView price = (TextView) convertView.findViewById(R.id.priceOrders);
        price.setTypeface(face);

        if(ordersDataView.getTypeorder().equals("3")){
            typeorder.setText("Food Delivery Service");
        }else if(ordersDataView.getTypeorder().equals("2")){
            typeorder.setText("Send Document Service");
        }else if(ordersDataView.getTypeorder().equals("1")){
            typeorder.setText("Ride Service");
        }

        fromandto.setText(ordersDataView.getFrom()+" - "+ordersDataView.getTo());
        ordertime.setText(ordersDataView.getOrdertime()+" (Status : Waiting for Driver)");

        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        price.setText(currencyFormatter.format(Double.parseDouble(ordersDataView.getPrice())));

        return convertView;
    }
}
