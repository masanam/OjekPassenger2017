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

import java.util.ArrayList;

/**
 * Created by andi on 4/17/2016.
 */
public class DeliveryCategoryAdapterList extends ArrayAdapter<WarungCategory> {

    ArrayList<WarungCategory> warungPojox;

    public DeliveryCategoryAdapterList(Context context, ArrayList<WarungCategory> warungCategories) {
        super(context, 0, warungCategories);
        this.warungPojox = new ArrayList<WarungCategory>();
        this.warungPojox.addAll(warungCategories);
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

        WarungCategory warungCategory = warungPojox.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.categorylayout, parent, false);
        }

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation_Regular.ttf");
        ImageView imageWarung = (ImageView) convertView.findViewById(R.id.warungImage);
        TextView textView = (TextView) convertView.findViewById(R.id.catname);
        textView.setTypeface(face);

        textView.setText(warungCategory.getName());

        Picasso.with(getContext()).load(base_url + "/img/" + warungCategory.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageWarung);

        return convertView;
    }

}