package ojekkeren.ojekkeren;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by andi on 4/18/2016.
 */
public class FoodTokoDetailListAdapter extends ArrayAdapter<WarungPojo> {
    ArrayList<WarungPojo> warungPojox;

    public FoodTokoDetailListAdapter(Context context, ArrayList<WarungPojo> warungPojo) {
        super(context, 0, warungPojo);
        this.warungPojox = new ArrayList<WarungPojo>();
        this.warungPojox.addAll(warungPojo);
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

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation_Regular.ttf");

        WarungPojo warungPojo = warungPojox.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.foodlistquantity, parent, false);
        }

        NumberPicker numberPicker = (NumberPicker) convertView.findViewById(R.id.quantity);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
        numberPicker.setValue(0);

        numberPicker.setTag(warungPojo);

        TextView foodnameTextView = (TextView) convertView.findViewById(R.id.foodname);
        TextView descriptionText = (TextView) convertView.findViewById(R.id.description);
        TextView pricefood = (TextView) convertView.findViewById(R.id.pricefood);
        TextView idfood = (TextView) convertView.findViewById(R.id.idfood);
        TextView pricepure = (TextView) convertView.findViewById(R.id.pricepure);

        Switch addNoteButton = (Switch) convertView.findViewById(R.id.addNoteButton);

        final EditText addnote = (EditText) convertView.findViewById(R.id.addnote);
        addNoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){addnote.setVisibility(View.VISIBLE);}else{addnote.setVisibility(View.INVISIBLE);}
            }
        });




        foodnameTextView.setTypeface(face);
        descriptionText.setTypeface(face);
        pricefood.setTypeface(face);
        foodnameTextView.setShadowLayer(2, 1, 1, 0);
        foodnameTextView.setText(warungPojo.getFoodname());
        descriptionText.setText(warungPojo.getDescriptionFood());

        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        pricefood.setText(currencyFormatter.format(Double.parseDouble(warungPojo.getPrice())));
        pricepure.setText(warungPojo.getPrice());
        idfood.setText(warungPojo.getId());

        return convertView;
    }
}
