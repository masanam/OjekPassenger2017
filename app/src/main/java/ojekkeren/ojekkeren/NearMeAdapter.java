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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andi on 4/13/2016.
 */
public class NearMeAdapter extends ArrayAdapter<WarungPojo> {

    ArrayList<WarungPojo> warungPojox;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private String lat,lng;

    public NearMeAdapter(Context context, ArrayList<WarungPojo> warungPojo) {
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

        String base_url = getServerAPIServer();

        WarungPojo warungPojo = warungPojox.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.near_me_adapter_view, parent, false);
        }

        ImageView imageWarung = (ImageView) convertView.findViewById(R.id.warungImage);
        TextView namaWarung = (TextView) convertView.findViewById(R.id.namaWarung);
        namaWarung.setTypeface(face);
        TextView theId = (TextView) convertView.findViewById(R.id.theId);
        TextView theDistance = (TextView) convertView.findViewById(R.id.theDistance);
        theDistance.setTypeface(face);

        Picasso.with(getContext()).load(base_url + "/img/" + warungPojo.getPic()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageWarung);
        namaWarung.setText(warungPojo.getNamawarung() + " , " + warungPojo.getDistance() + " KM");
        theId.setText(warungPojo.getId());
        theDistance.setText(warungPojo.getDistance());


        return convertView;
    }

}
