package ojekkeren.ojekkeren;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andi on 4/22/2016.
 */
public class FoodCarts implements Parcelable {

    List<HashMap<String,String>> orders;

    protected FoodCarts(Parcel in) {
        orders = in.readArrayList(null);
    }

    public static final Creator<FoodCarts> CREATOR = new Creator<FoodCarts>() {
        @Override
        public FoodCarts createFromParcel(Parcel in) {
            return new FoodCarts(in);
        }

        @Override
        public FoodCarts[] newArray(int size) {
            return new FoodCarts[size];
        }
    };

    public FoodCarts(List<HashMap<String,String>> order) {
        orders = order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(orders);
    }
}
